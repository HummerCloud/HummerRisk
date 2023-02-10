package com.hummer.common.core.oss.provider;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hummer.common.core.domain.OssBucket;
import com.hummer.common.core.domain.OssWithBLOBs;
import com.hummer.common.core.oss.constants.ObjectTypeConstants;
import com.hummer.common.core.oss.dto.*;
import com.hummer.common.core.proxy.tencent.QCloudCredential;
import com.hummer.common.core.utils.ReadFileUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.tencentcloudapi.common.Credential;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class QcloudProvider implements OssProvider {

    private static final String BASE_REGION_DIC = "support/regions/";
    private static final String JSON_EXTENSION = ".json";

    @Override
    public String policyModel() {
        return "{\n" +
                "    \"version\": \"2.0\",\n" +
                "    \"statement\": [\n" +
                "        {\n" +
                "            \"effect\": \"allow\",\n" +
                "            \"action\": [\n" +
                "                \"name/cos:*\"\n" +
                "            ],\n" +
                "            \"resource\": [\n" +
                "                \"qcs::cos:REGION:uid/UID:BUCKET_NAME\"\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }


    @Override
    public List<OssBucket> getOssBucketList(OssWithBLOBs ossAccount) throws Exception {
        List<OssBucket> resultList = new ArrayList<>();
        COSClient cosClient = getCosClient(ossAccount.getCredential(), null);
        List<Bucket> buckets = cosClient.listBuckets();
        for (Bucket bucket : buckets) {
            OssBucket tmpBucket = setBucket(bucket, ossAccount);
            setBucketAcl(tmpBucket, ossAccount);
            BucketMetric bucketMetric = getBucketMetric(tmpBucket, ossAccount);
            tmpBucket.setSize(SysListener.changeFlowFormat(bucketMetric.getSize()));
            tmpBucket.setObjectNumber(bucketMetric.getObjectNumber());
            tmpBucket.setStorageClass("N/A");
            resultList.add(tmpBucket);
        }
        cosClient.shutdown();
        return resultList;
    }

    private OssBucket setBucket(Bucket bucket, OssWithBLOBs ossAccount) throws Exception {
        OssBucket bucketDTO = new OssBucket();
        if (bucket.getCreationDate() != null) {
            bucketDTO.setCreateTime(bucket.getCreationDate().getTime());
        }
        bucketDTO.setBucketName(bucket.getName());
        bucketDTO.setOssId(ossAccount.getId());
        bucketDTO.setCannedAcl(bucket.getLocation());
        bucketDTO.setLocation(bucket.getLocation());
        bucketDTO.setDomainName(getDomainame(ossAccount, bucket.getLocation(), bucket.getName()));
        bucketDTO.setSize("0");
        bucketDTO.setObjectNumber(0L);
        return bucketDTO;
    }

    private String getDomainame(OssWithBLOBs ossAccount, String region, String bucketName) throws Exception {
        List<OssRegion> ossRegions = getOssRegions(ossAccount);
        for (OssRegion ossRegion : ossRegions) {
            if (ossRegion.getRegionId().equals(region)) {
                return ossRegion.getExtranetEndpoint().replace("<BucketName-APPID>", bucketName);
            }
        }
        return null;
    }

    public List<OssRegion> getOssRegions(OssWithBLOBs ossAccount) throws Exception {
        String result = ReadFileUtils.readConfigFile(BASE_REGION_DIC, ossAccount.getPluginId(), JSON_EXTENSION);
        return new Gson().fromJson(result, new TypeToken<ArrayList<OssRegion>>() {
        }.getType());
    }

    @Override
    public void deletetObjects(OssBucket bucket, OssWithBLOBs account, List<String> objectIds) throws Exception {
        COSClient cosClient = getCosClient(account.getCredential(), bucket.getLocation());
        for (String objectId : objectIds) {
            if(objectId.endsWith("/")){
                List<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();
                getObjectKeys(objectId, bucket.getBucketName(), keys, cosClient);
                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket.getBucketName());
                deleteObjectsRequest.setQuiet(false);
                deleteObjectsRequest.setKeys(keys);
                cosClient.deleteObjects(deleteObjectsRequest);
            }
            cosClient.deleteObject(new DeleteObjectRequest(bucket.getBucketName(), objectId));
        }
        cosClient.shutdown();
    }

    private void getObjectKeys(String objectId, String bucketName, List<DeleteObjectsRequest.KeyVersion> keys, COSClient cosClient){
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucketName);
        listObjectsRequest.setDelimiter("/");
        listObjectsRequest.setPrefix(objectId);
        boolean done = false;
        while (!done) {
            ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);
            if(objectListing.getObjectSummaries().size() > 0 || objectListing.getCommonPrefixes().size() > 0){
                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
                deleteObjectsRequest.setQuiet(false);
                for (COSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    keys.add(new DeleteObjectsRequest.KeyVersion(objectSummary.getKey()));
                }
                for (String objectSummary : objectListing.getCommonPrefixes()) {
                    keys.add(new DeleteObjectsRequest.KeyVersion(objectSummary));
                    getObjectKeys(objectSummary, bucketName, keys, cosClient);
                }
            }
            if (objectListing.getNextMarker() == null) {
                done = true;
                return;
            }
        }
    }

    @Override
    public void createDir(OssBucket bucket, OssWithBLOBs account, String dir) throws Exception {
        try {
            dir = dir.endsWith("/") ? dir : dir+ "/";
            COSClient cosClient = getCosClient(account.getCredential(), bucket.getLocation());
            String[] split = dir.split("/");
            String data = "";
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(0);
            for (String d : split) {
                data += d + "/";
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucket.getBucketName(), data, new ByteArrayInputStream("".getBytes()), objectMetadata);
                cosClient.putObject(putObjectRequest);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void uploadFile(OssBucket bucket, OssWithBLOBs account, String objectId, InputStream file, long size) throws Exception {
        COSClient cosClient = getCosClient(account.getCredential(), bucket.getLocation());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket.getBucketName(), objectId, file, objectMetadata);
        cosClient.putObject(putObjectRequest);
        cosClient.shutdown();
    }

    @Override
    public void deleteKey(OssBucket bucket, OssWithBLOBs account, String name) throws Exception {

    }


    private COSClient getCosClient(String credential, String region) {
        Credential object = JSON.parseObject(credential, Credential.class);
        COSCredentials cred = new BasicCOSCredentials(object.getSecretId(), object.getSecretKey());
        ClientConfig clientConfig;
        if (StringUtils.isEmpty(region)) {
            clientConfig = new ClientConfig(new Region("ap-guangzhou"));
        } else {
            clientConfig = new ClientConfig(new Region(region));
        }
        clientConfig.setConnectionTimeout(300 * 1000);
        clientConfig.setSocketTimeout(300 * 1000);
        return new COSClient(cred, clientConfig);
    }

    private void setBucketAcl(OssBucket bucket, OssWithBLOBs ossAccount) {
        COSClient cosClient = getCosClient(ossAccount.getCredential(), bucket.getLocation());
        AccessControlList accessControlList = cosClient.getBucketAcl(bucket.getBucketName());
        List<Grant> grants = accessControlList.getGrantsAsList();
        boolean Private = true;
        boolean PublicRead = false;
        boolean PublicWrite = false;
        for (Grant grant : grants) {
            if (grant.getPermission().name().equalsIgnoreCase("READ") && grant.getGrantee().toString().contains("AllUsers]")) {
                PublicRead = true;
                Private = false;
            }
            if (grant.getPermission().name().equalsIgnoreCase("WRITE") && grant.getGrantee().toString().contains("AllUsers]")) {
                PublicWrite = true;
                Private = false;
            }
        }
        if (Private) {
            bucket.setCannedAcl("Private");
        }
        if (PublicRead) {
            bucket.setCannedAcl("PublicRead");
        }
        if (PublicWrite && PublicRead) {
            bucket.setCannedAcl("PublicReadWrite");
        }

        cosClient.shutdown();
    }

    public BucketMetric getBucketMetric(OssBucket bucket, OssWithBLOBs account) throws Exception {
        Long size = 0L;
        QCloudCredential credential = JSON.parseObject(account.getCredential(), QCloudCredential.class);
        TreeMap<String, Object> params = new TreeMap<>();
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("startTime", df.format(date.getTime() - 7200000));
        params.put("endTime", df.format(date));
        params.put("namespace", "qce/cos");
        params.put("metricName", "std_storage");
        params.put("Region", bucket.getLocation());
        params.put("dimensions.0.name", "appid");
        params.put("dimensions.0.value", credential.getAPPID());
        params.put("dimensions.1.name", "bucket");
        params.put("dimensions.1.value", bucket.getBucketName());
        QcloudApiModuleCenter monitor = getModule(account.getCredential(), new Monitor());
        JSONObject resp = JSONObject.parseObject(monitor.call("GetMonitorData", params));
        if (resp.getInteger("code") == 0 && resp.getJSONArray("dataPoints").get(0) != null) {
            size = size + Long.parseLong(resp.getJSONArray("dataPoints").getString(0));
        }
        params.put("metricName", "sia_storage");
        resp = JSONObject.parseObject(monitor.call("GetMonitorData", params));
        if (resp.getInteger("code") == 0 && resp.getJSONArray("dataPoints").get(0) != null) {
            size = size + Long.parseLong(resp.getJSONArray("dataPoints").getString(0));
        }
        params.put("metricName", "arc_storage");
        resp = JSONObject.parseObject(monitor.call("GetMonitorData", params));
        if (resp.getInteger("code") == 0 && resp.getJSONArray("dataPoints").get(0) != null) {
            size = size + Long.parseLong(resp.getJSONArray("dataPoints").getString(0));
        }

        Long objNum = 0L;

        BucketMetric bucketMetric = new BucketMetric();
        bucketMetric.setSize(size.longValue());
        bucketMetric.setObjectNumber(objNum);
        return bucketMetric;
    }

    private QcloudApiModuleCenter getModule(String credential, Base module) {
        Credential object = JSON.parseObject(credential, Credential.class);
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        config.put("SecretId", object.getSecretId());
        config.put("SecretKey", object.getSecretKey());
        return new QcloudApiModuleCenter(module, config);
    }

    @Override
    public List<BucketObjectDTO> getBucketObjects(OssBucket bucket, OssWithBLOBs account, String prefix) {
        List<BucketObjectDTO> objects = new ArrayList<>();
        COSClient cosClient = getCosClient(account.getCredential(), bucket.getLocation());
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucket.getBucketName());
        listObjectsRequest.setDelimiter("/");
        if (StringUtils.isNotEmpty(prefix)) {
            listObjectsRequest.setPrefix(prefix);
        }

        boolean done = false;
        while (!done) {
            ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);
            if (objectListing.getNextMarker() == null) {
                done = true;
            }
            objects.addAll(convertToBucketFolder(bucket, objectListing.getCommonPrefixes(), prefix));
            objects.addAll(convertToBucketObject(bucket, objectListing.getObjectSummaries(), prefix));
        }
        cosClient.shutdown();
        List<BucketObjectDTO> bucketObjectDTOS = new ArrayList<>();
        for (BucketObjectDTO object : objects) {
            if (object.getObjectType().equals(ObjectTypeConstants.BACK.name())) {
                bucketObjectDTOS.add(0, object);
            } else {
                bucketObjectDTOS.add(object);
            }
        }
        return bucketObjectDTOS;
    }

    private List<BucketObjectDTO> convertToBucketFolder(OssBucket bucket, List<String> commonPrefixes, String prefix) {
        List<BucketObjectDTO> objects = new ArrayList<>();
        for (String commonPrefix : commonPrefixes) {
            BucketObjectDTO bucketObject = new BucketObjectDTO();
            bucketObject.setBucketId(bucket.getId());
            if (StringUtils.isNotEmpty(prefix)) {
                bucketObject.setObjectName(commonPrefix.substring(prefix.length()));
            } else {
                bucketObject.setObjectName(commonPrefix);
            }
            bucketObject.setId(commonPrefix);
            bucketObject.setObjectType(ObjectTypeConstants.DIR.name());
            objects.add(bucketObject);
        }
        return objects;
    }

    private List<BucketObjectDTO> convertToBucketObject(OssBucket bucket, List<COSObjectSummary> cosObjectSummaries, final String prefix) {
        List<BucketObjectDTO> objects = new ArrayList<>();
        if (ObjectUtils.isEmpty(cosObjectSummaries)) {
            BucketObjectDTO bucketObject = new BucketObjectDTO();
            bucketObject.setBucketId(bucket.getId());
            if (StringUtils.isNotEmpty(prefix)) {
                String[] dirs = prefix.split("/");
                if (dirs.length == 1) {
                    bucketObject.setId("/");
                    bucketObject.setObjectName(prefix);
                } else {
                    String lastDir = dirs[dirs.length - 1];
                    bucketObject.setId(prefix.substring(0, prefix.length() - lastDir.length() - 1));
                    bucketObject.setObjectName(lastDir + "/");
                }
                bucketObject.setObjectType(ObjectTypeConstants.BACK.name());
                objects.add(bucketObject);
            }
        } else {
            BucketObjectDTO bucketObject = new BucketObjectDTO();
            bucketObject.setBucketId(bucket.getId());

            if (StringUtils.isNotEmpty(prefix) && cosObjectSummaries.get(0).getKey().contains(prefix)) {
                String[] dirs = prefix.split("/");
                if (dirs.length == 1) {
                    bucketObject.setId("/");
                    bucketObject.setObjectName(prefix);
                } else {
                    String lastDir = dirs[dirs.length - 1];
                    bucketObject.setId(prefix.substring(0, prefix.length() - lastDir.length() - 1));
                    bucketObject.setObjectName(lastDir + "/");
                }
                bucketObject.setObjectType(ObjectTypeConstants.BACK.name());
                objects.add(bucketObject);
            }
        }

        for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {

            if (!cosObjectSummary.getKey().endsWith("/")) {
                BucketObjectDTO bucketObject = new BucketObjectDTO();
                bucketObject.setBucketId(bucket.getId());
                bucketObject.setObjectSize(SysListener.changeFlowFormat(cosObjectSummary.getSize()));
                bucketObject.setId(cosObjectSummary.getKey());
                if (StringUtils.isEmpty(prefix)) {
                    bucketObject.setObjectName(cosObjectSummary.getKey());
                } else {
                    bucketObject.setObjectName(cosObjectSummary.getKey().substring(prefix.length(), cosObjectSummary.getKey().length() - 1));
                }
                bucketObject.setStorageClass(cosObjectSummary.getStorageClass());
                bucketObject.setLastModified(cosObjectSummary.getLastModified().getTime());
                bucketObject.setObjectType(ObjectTypeConstants.FILE.name());
                objects.add(bucketObject);
            }

        }
        return objects;
    }

    @Override
    public FilterInputStream downloadObject(OssBucket bucket, OssWithBLOBs account, final String objectId) throws Exception {
        COSClient cosClient = getCosClient(account.getCredential(), bucket.getLocation());
        return cosClient.getObject(bucket.getBucketName(), objectId).getObjectContent();
    }

    @Override
    public boolean doesBucketExist(OssWithBLOBs ossAccount, OssBucket bucket) throws Exception {
        boolean exits = false;
        COSClient cosClient = getCosClient(ossAccount.getCredential(), bucket.getLocation());
        List<Bucket> buckets = cosClient.listBuckets();
        for (Bucket cosBucket : buckets) {
            if(cosBucket.getName().equals(bucket.getBucketName())){
                exits = true;
            }
        }
        cosClient.shutdown();
        return exits;
    }

    @Override
    public OssBucket createBucket(OssWithBLOBs ossAccount, OssBucket bucket) throws Exception {
        COSClient cosClient = getCosClient(ossAccount.getCredential(), bucket.getLocation());
        QCloudCredential qCloudCredential = JSON.parseObject(ossAccount.getCredential(), QCloudCredential.class);
        CreateBucketRequest createBucketRequest =  new CreateBucketRequest(bucket.getBucketName() + "-" + qCloudCredential.getAPPID());
        createBucketRequest.setCannedAcl(CannedAccessControlList.valueOf(bucket.getCannedAcl()));
        Bucket cosBucket = cosClient.createBucket(createBucketRequest);
        bucket.setCreateTime(System.currentTimeMillis());
        bucket.setBucketName(cosBucket.getName());
        bucket.setStorageClass("N/A");
        bucket.setDomainName(getDomainame(ossAccount, bucket.getLocation(), cosBucket.getName()));
        cosClient.shutdown();
        return bucket;
    }

    @Override
    public void deleteBucket(OssWithBLOBs ossAccount, OssBucket bucket) throws Exception {
        COSClient cosClient = getCosClient(ossAccount.getCredential(), bucket.getLocation());
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucket.getBucketName());
        ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);
        if(objectListing.getCommonPrefixes().size() > 0 || objectListing.getObjectSummaries().size() > 0){
            throw new Exception("Bucket is not empty. Please check whether the bucket contains undeleted objects!");
        }
        cosClient.deleteBucket(bucket.getBucketName());
        cosClient.shutdown();
    }

}

