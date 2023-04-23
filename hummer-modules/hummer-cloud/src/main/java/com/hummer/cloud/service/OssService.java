package com.hummer.cloud.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hummer.cloud.mapper.*;
import com.hummer.cloud.mapper.ext.ExtCloudTaskMapper;
import com.hummer.cloud.mapper.ext.ExtOssMapper;
import com.hummer.cloud.oss.config.OssManager;
import com.hummer.cloud.oss.constants.OSSConstants;
import com.hummer.cloud.oss.controller.request.OssBucketRequest;
import com.hummer.cloud.oss.controller.request.OssRequest;
import com.hummer.cloud.oss.dto.*;
import com.hummer.cloud.oss.provider.OssProvider;
import com.hummer.common.core.constant.CloudAccountConstants;
import com.hummer.common.core.constant.CloudTaskConstants;
import com.hummer.common.core.constant.ResourceOperation;
import com.hummer.common.core.constant.ResourceTypeConstants;
import com.hummer.common.core.domain.*;
import com.hummer.common.core.domain.request.cloudTask.ManualRequest;
import com.hummer.common.core.domain.request.excel.ExcelExportRequest;
import com.hummer.common.core.domain.request.resource.ResourceRequest;
import com.hummer.common.core.domain.request.rule.RuleGroupRequest;
import com.hummer.common.core.dto.ExportDTO;
import com.hummer.common.core.dto.ResourceDTO;
import com.hummer.common.core.dto.RuleGroupDTO;
import com.hummer.common.core.dto.ValidateDTO;
import com.hummer.common.core.exception.HRException;
import com.hummer.common.core.handler.ResultHolder;
import com.hummer.common.core.i18n.Translator;
import com.hummer.common.core.utils.*;
import com.hummer.system.api.IOperationLogService;
import com.hummer.system.api.model.LoginUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author harris
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OssService {

    @Autowired
    private OssMapper ossMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private ExtOssMapper extOssMapper;
    @Autowired
    private OssBucketMapper ossBucketMapper;
    @Autowired
    private CommonThreadPool commonThreadPool;
    @Autowired
    private OssLogMapper ossLogMapper;
    @Autowired
    private ProxyMapper proxyMapper;
    @Autowired
    private RuleGroupMapper ruleGroupMapper;
    @Autowired
    private CloudTaskMapper cloudTaskMapper;
    @Autowired
    private ExtCloudTaskMapper extCloudTaskMapper;

    @DubboReference
    private IOperationLogService operationLogService;

    private static final String BASE_CANNED_ACL_TYPE = "cannedACL";
    private static final String BASE_STORAGE_CLASS_TYPE = "storageClass";
    private static final String BASE_REGION_DIC = "support/regions/";
    private static final String BASE_CREDENTIAL_DIC = "support/credential/";
    private static final String BASE_CANNED_ACL_DIC = "support/cannedACL/";
    private static final String BASE_STORAGE_CLASS_DIC = "support/storageClass/";
    private static final String JSON_EXTENSION = ".json";

    public List<OssDTO> ossList(OssRequest request) {
        return extOssMapper.ossList(request);
    }

    public List<OssBucketDTO> ossBucketList(OssBucketRequest request) {
        return extOssMapper.ossBucketList(request);
    }

    public boolean validate(List<String> ids) {
        ids.forEach(id -> {
            try {
                ValidateDTO validate = validate(id);
                if(!validate.isFlag()) throw new HRException(Translator.get("failed_oss"));
            } catch (Exception e) {
                throw new HRException(e.getMessage());
            }
        });
        return true;
    }

    public ValidateDTO validate(String id) {
        OssWithBLOBs oss = ossMapper.selectByPrimaryKey(id);
        //检验账号的有效性
        ValidateDTO valid = validateAccount(oss);
        if (valid.isFlag()) {
            oss.setStatus(CloudAccountConstants.Status.VALID.name());
        } else {
            oss.setStatus(CloudAccountConstants.Status.INVALID.name());
        }
        ossMapper.updateByPrimaryKeySelective(oss);
        return valid;
    }

    private ValidateDTO validateAccount(OssWithBLOBs account) {
        ValidateDTO validateDTO = new ValidateDTO();
        validateDTO.setName(account.getName());
        try {
            Proxy proxy = new Proxy();
            if (account.getProxyId() != null) proxy = proxyMapper.selectByPrimaryKey(account.getProxyId());
            validateDTO.setFlag(PlatformUtils.validateCredential(BeanUtils.copyBean(new AccountWithBLOBs(), account), proxy));
            validateDTO.setMessage(String.format("Verification oss account status: [%s], oss account: [%s], plugin: [%s]", validateDTO.isFlag(), account.getName(), account.getPluginName()));
            return validateDTO;
        } catch (Exception e) {
            validateDTO.setMessage(String.format("HRException in verifying oss account, oss account: [%s], plugin: [%s], error information:%s", account.getName(), account.getPluginName(), e.getMessage()));
            validateDTO.setFlag(false);
            LogUtil.error(String.format("HRException in verifying oss account, oss account: [%s], plugin: [%s], error information:%s", account.getName(), account.getPluginName(), e.getMessage()), e);
            return validateDTO;
        }
    }

    public List<OssWithBLOBs> allList() {
        OssExample example = new OssExample();
        example.createCriteria().andStatusEqualTo(CloudAccountConstants.Status.VALID.name());
        return ossMapper.selectByExampleWithBLOBs(example);
    }

    public List<AccountWithBLOBs> getCloudAccountList() {
        AccountExample example = new AccountExample();
        List<String> values = new ArrayList<>();
        values.add(OSSConstants.aws);
        values.add(OSSConstants.aliyun);
        values.add(OSSConstants.baidu);
        values.add(OSSConstants.huawei);
        values.add(OSSConstants.huoshan);
        values.add(OSSConstants.tencent);
        values.add(OSSConstants.qingcloud);
        values.add(OSSConstants.qiniu);
        values.add(OSSConstants.ucloud);
        values.add(OSSConstants.jdloud);
        example.createCriteria().andStatusEqualTo(CloudAccountConstants.Status.VALID.name()).andPluginIdIn(values);
        return accountMapper.selectByExampleWithBLOBs(example);
    }

    public String strategy(String accountId) throws Exception {
        Account account = accountMapper.selectByPrimaryKey(accountId);
        OssProvider ossProvider = getOssProvider(account.getPluginId());
        String script = ossProvider.policyModel();
        return script;
    }

    public String getCredential(String accountId) {
        Account account = accountMapper.selectByPrimaryKey(accountId);
        try {
            return ReadFileUtils.readConfigFile(BASE_CREDENTIAL_DIC, account.getPluginId(), JSON_EXTENSION);
        } catch (Exception e) {
            LogUtil.error("Error getting credential parameters: " + account.getPluginId(), e);
            HRException.throwException(Translator.get("i18n_ex_plugin_get"));
        }
        return Translator.get("i18n_ex_plugin_get");
    }

    public OssWithBLOBs addOss(OssWithBLOBs request, LoginUser loginUser) throws Exception {
        OssWithBLOBs oss = ossMapper.selectByPrimaryKey(request.getId());
        if (oss != null) {
            editOss(request, loginUser);
        } else {
            request.setCreateTime(System.currentTimeMillis());
            request.setUpdateTime(System.currentTimeMillis());
            request.setCreator(loginUser.getUserId());
            request.setSyncStatus(CloudTaskConstants.TASK_STATUS.APPROVED.name());
            ossMapper.insertSelective(request);
            validate(request.getId());
        }
        return request;
    }

    public OssWithBLOBs editOss(OssWithBLOBs request, LoginUser loginUser) throws Exception {
        request.setUpdateTime(System.currentTimeMillis());
        request.setCreator(loginUser.getUserId());
        request.setSyncStatus(CloudTaskConstants.TASK_STATUS.APPROVED.name());
        ossMapper.updateByPrimaryKeySelective(request);
        validate(request.getId());
        return request;
    }

    public void deleteOss(String ossId) {
        ossMapper.deleteByPrimaryKey(ossId);
        OssLogExample example = new OssLogExample();
        example.createCriteria().andOssIdEqualTo(ossId);
        ossLogMapper.deleteByExample(example);
    }

    public void deleteAccounts(List<String> ids) throws Exception {
        ids.forEach(id -> {
            try {
                deleteOss(id);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    public List<OssLogWithBLOBs> getLogList(String ossId) {
        OssLogExample example = new OssLogExample();
        example.createCriteria().andOssIdEqualTo(ossId);
        return ossLogMapper.selectByExampleWithBLOBs(example);
    }

    public void batch(String id, LoginUser loginUser) throws Exception {
        OssWithBLOBs oss = ossMapper.selectByPrimaryKey(id);
        oss.setSyncStatus(OSSConstants.SYNC_STATUS.APPROVED.name());
        ossMapper.updateByPrimaryKeySelective(oss);
        OssLogExample example = new OssLogExample();
        example.createCriteria().andOssIdEqualTo(id);
        ossLogMapper.deleteByExample(example);
        saveLog(id, "i18n_start_oss_sync", "", true, 0, loginUser);
    }

    public void syncBatch(String id, LoginUser loginUser) throws Exception {
        OssWithBLOBs oss = ossMapper.selectByPrimaryKey(id);
        try {
            syncResource(oss, loginUser);
        } catch (Exception e) {
            e.printStackTrace();
            oss.setSyncStatus(OSSConstants.SYNC_STATUS.ERROR.name());
            ossMapper.updateByPrimaryKeySelective(oss);
            saveLog(oss.getId(), "i18n_operation_ex" + ": " + e.getMessage(), e.getMessage(), false, 0, loginUser);
            LogUtil.error(String.format("Failed to synchronize cloud account: %s", oss.getName()), e);
        }
    }

    private void syncResource(OssWithBLOBs oss, LoginUser loginUser) throws Exception {
        if (!accountService.validate(oss.getId()).isFlag()) {
            oss.setSyncStatus(OSSConstants.SYNC_STATUS.ERROR.name());
            ossMapper.updateByPrimaryKeySelective(oss);
            saveLog(oss.getId(), "i18n_operation_ex" + ": " + "failed_oss", "failed_oss", false, 0, loginUser);
            return;
        }
        operationLogService.log(null, oss.getId(), oss.getName(), ResourceTypeConstants.OSS.name(), ResourceOperation.SYNC, "i18n_start_oss_sync");
        ossMapper.updateByPrimaryKeySelective(oss);
        syncBucketInfo(oss, loginUser);
    }

    public void syncBucketInfo(final OssWithBLOBs oss, LoginUser loginUser) throws Exception {
        commonThreadPool.addTask(() -> {
            try {
                doSyncBucketInfo(oss, loginUser);
            } catch (Exception e) {
                e.printStackTrace();
                oss.setSyncStatus(OSSConstants.SYNC_STATUS.ERROR.name());
                oss.setUpdateTime(System.currentTimeMillis());
                ossMapper.updateByPrimaryKeySelective(oss);
                saveLog(oss.getId(), "i18n_operation_ex" + ": " + e.getMessage(), e.getMessage(), false, 0, loginUser);
            }
        });
    }

    public void doSyncBucketInfo(OssWithBLOBs oss, LoginUser loginUser) throws Exception {
        Integer sum = fetchOssBucketList(oss);
        oss.setSyncStatus(OSSConstants.SYNC_STATUS.FINISHED.name());
        oss.setUpdateTime(System.currentTimeMillis());
        oss.setSum(sum);
        ossMapper.updateByPrimaryKeySelective(oss);
        saveLog(oss.getId(), "i18n_end_oss_sync", "", true, sum, loginUser);
    }

    public Integer fetchOssBucketList(OssWithBLOBs oss) throws Exception {
        try {
            OssProvider ossProvider = getOssProvider(oss.getPluginId());
            return saveOssBuckets(ossProvider, oss);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("Failed to get the bucket information: " + oss.getName(), e);
            HRException.throwException(Translator.get("i18n_get_bucket_info_failed") + e.getMessage());
            throw e;
        }
    }

    public OssProvider getOssProvider(String pluginId) throws Exception {
        OssProvider ossProvider = (OssProvider) OssManager.getOssProviders().get(pluginId);
        if (ossProvider == null) {
            throw new Exception(String.format("Unsupported plugin: %s", pluginId));
        }
        return ossProvider;
    }

    private Integer saveOssBuckets(OssProvider ossProvider, OssWithBLOBs ossAccount) throws Exception {
        try {
            List<OssBucket> list = ossProvider.getOssBucketList(ossAccount);
            OssBucketExample bucketExample = new OssBucketExample();
            bucketExample.createCriteria().andIdEqualTo(ossAccount.getId());
            OssBucket bucket = new OssBucket();
            bucket.setSyncFlag(true);
            ossBucketMapper.updateByExampleSelective(bucket, bucketExample);
            list.forEach(newBucket -> {
                newBucket.setSyncFlag(false);
                newBucket.setCreateTime(System.currentTimeMillis());
                OssBucketExample example = new OssBucketExample();
                example.createCriteria()
                        .andOssIdEqualTo(newBucket.getOssId())
                        .andLocationEqualTo(newBucket.getLocation())
                        .andBucketNameEqualTo(newBucket.getBucketName());
                if (ossBucketMapper.selectByExample(example).size() > 0) {
                    ossBucketMapper.updateByExampleSelective(newBucket, example);
                } else {
                    newBucket.setId(UUIDUtil.newUUID());
                    ossBucketMapper.insertSelective(newBucket);
                }
            });

            bucketExample = new OssBucketExample();
            bucketExample.createCriteria()
                    .andOssIdEqualTo(ossAccount.getId())
                    .andSyncFlagEqualTo(true);
            ossBucketMapper.deleteByExample(bucketExample);
            return list.size();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }

    }

    void saveLog(String ossId, String operation, String output, boolean result, Integer sum, LoginUser loginUser) {
        OssLogWithBLOBs ossLog = new OssLogWithBLOBs();
        String operator = "system";
        try {
            if (loginUser != null) {
                operator = loginUser.getUserId();
            }
        } catch (Exception e) {
            //防止单元测试无session
        }
        ossLog.setOperator(operator);
        ossLog.setOssId(ossId);
        ossLog.setCreateTime(System.currentTimeMillis());
        ossLog.setOperation(operation);
        ossLog.setOutput(output);
        ossLog.setResult(result);
        ossLog.setSum(sum);
        ossLogMapper.insertSelective(ossLog);

    }

    public List<BucketObjectDTO> getObjects(String bucketId, String prefix) throws Exception {
        OssBucket bucket = getBucketByPrimaryKey(bucketId);
        OssWithBLOBs oss = getAccountByPrimaryKey(bucket.getOssId());
        OssProvider ossProvider = (OssProvider) OssManager.getOssProviders().get(oss.getPluginId());
        List<BucketObjectDTO> bucketObjects = ossProvider.getBucketObjects(bucket, oss, prefix);
        ossBucketMapper.updateByPrimaryKeySelective(bucket);
        return bucketObjects;
    }

    private OssBucket getBucketByPrimaryKey(String bucketId) throws Exception {
        OssBucket bucket = ossBucketMapper.selectByPrimaryKey(bucketId);
        if (bucket == null) {
            throw new Exception("Parameter is null.");
        }
        return bucket;
    }

    private OssWithBLOBs getAccountByPrimaryKey(String ossId) throws Exception {
        OssWithBLOBs account = ossMapper.selectByPrimaryKey(ossId);
        return account;
    }

    public FilterInputStream downloadObject(String bucketId, String objectId) throws Exception {
        OssBucket bucket = getBucketByPrimaryKey(bucketId);
        OssWithBLOBs oss = getAccountByPrimaryKey(bucket.getOssId());
        OssProvider ossProvider = (OssProvider) OssManager.getOssProviders().get(oss.getPluginId());
        return ossProvider.downloadObject(bucket, oss, objectId);
    }

    public void create(OssBucket params, LoginUser loginUser) throws Exception {
        if (null == params || StringUtils.isBlank(params.getOssId()) || StringUtils.isBlank(params.getBucketName())) {
            HRException.throwException("Parameter is null.");
        }
        OssWithBLOBs account = getAccountByPrimaryKey(params.getOssId());
        OssProvider ossProvider = getOssProvider(account.getPluginId());
        createBucket(ossProvider, account, params, loginUser);
    }

    public ResultHolder delete(List<String> ids, LoginUser loginUser) {
        ResultHolder resultHolder = new ResultHolder();
        if (CollectionUtils.isEmpty(ids)) {
            resultHolder.setSuccess(false);
            resultHolder.setMessage("Unselected record");
        }

        OssBucketExample example = new OssBucketExample();
        example.createCriteria().andIdIn(ids);
        List<OssBucket> buckets = ossBucketMapper.selectByExample(example);
        int count = 0;
        StringBuilder message = new StringBuilder();
        for (final OssBucket bucket : buckets) {
            operationLogService.log(loginUser, bucket.getId(), bucket.getBucketName(), ResourceTypeConstants.OSS.name(), ResourceOperation.DELETE, "i18n_delete_bucket");
            try {
                OssWithBLOBs account = getAccountByPrimaryKey(bucket.getOssId());
                if (null != account && !OSSConstants.ACCOUNT_STATUS.VALID.equals(account.getStatus())) {
                    throw new Exception(Translator.get("i18n_invalid_cloud_account"));
                }
                OssProvider ossProvider = getOssProvider(account.getPluginId());
                if (ossProvider.doesBucketExist(account, bucket)) {
                    ossProvider.deleteBucket(account, bucket);
                }
                if (!String.valueOf(bucket.getId()).isEmpty()) {
                    ossBucketMapper.deleteByPrimaryKey(bucket.getId());
                }
            } catch (Exception e) {
                LogUtil.error("Failed to delete the bucket: " + bucket.getBucketName(), e);
                count++;
                message.append(String.format("Bucket %s delete failed：%s \n", bucket.getBucketName(), e.getMessage()));
            }
        }
        if (count > 0) {
            resultHolder.setSuccess(false);
            resultHolder.setMessage(message.toString());
        }

        return resultHolder;
    }

    public OssBucket createBucket(OssProvider cloudprovider, OssWithBLOBs account, OssBucket params, LoginUser loginUser) {
        OssBucket result = null;
        try {
            result = cloudprovider.createBucket(account, params);
            if (result == null) {
                throw new Exception(Translator.get("i18n_creation_returns_empty"));
            }
            if (result.getStorageClass() == null) {
                result.setStorageClass("");
            }
            result.setId(UUIDUtil.newUUID());
            ossBucketMapper.insertSelective(result);
            operationLogService.log(loginUser, result.getId(), result.getBucketName(), ResourceTypeConstants.OSS.name(), ResourceOperation.CREATE, "i18n_create_bucket");
            return result;
        } catch (Exception e) {
            LogUtil.error("Failed to create the bucket: " + params.getBucketName(), e);
            HRException.throwException(Translator.get("i18n_create_bucket_failed") + "：" + e.getMessage());
        }
        return result;
    }

    public void createDir(String bucketId, String dir) throws Exception {
        OssBucket bucket = getBucketByPrimaryKey(bucketId);
        OssWithBLOBs account = getAccountByPrimaryKey(bucket.getOssId());
        OssProvider ossProvider = getOssProvider(account.getPluginId());
        ossProvider.createDir(bucket, account, dir);
    }

    public void deleteObject(String bucketId, String objectId) throws Exception {
        deleteObjects(bucketId, Arrays.asList(objectId));
    }

    public void deleteObjects(String bucketId, List<String> objectIds) throws Exception {
        OssBucket bucket = getBucketByPrimaryKey(bucketId);
        OssWithBLOBs account = getAccountByPrimaryKey(bucket.getOssId());
        OssProvider ossProvider = getOssProvider(account.getPluginId());
        ossProvider.deletetObjects(bucket, account, objectIds);
    }

    public BucketKeyValueItem bucketAddforOssId(String ossId) throws Exception {
        BucketKeyValueItem bucketKeyValueItem = new BucketKeyValueItem();
        OssWithBLOBs oss = getAccountByPrimaryKey(ossId);
        bucketKeyValueItem.setShowLocation(true);
        bucketKeyValueItem.setLocationList(getOssRegions(ossId));
        if (StringUtils.equals(oss.getPluginId(), OSSConstants.aws)) {
            bucketKeyValueItem.setShowCannedAcl(true);
            bucketKeyValueItem.setCannedAclList(getParamList(ossId, BASE_CANNED_ACL_TYPE));
        } else if (StringUtils.equals(oss.getPluginId(), OSSConstants.aliyun)) {
            bucketKeyValueItem.setShowCannedAcl(true);
            bucketKeyValueItem.setShowStorageClass(true);
            bucketKeyValueItem.setCannedAclList(getParamList(ossId, BASE_CANNED_ACL_TYPE));
            bucketKeyValueItem.setStorageList(getParamList(ossId, BASE_STORAGE_CLASS_TYPE));
        } else if (StringUtils.equals(oss.getPluginId(), OSSConstants.tencent)) {
            bucketKeyValueItem.setShowCannedAcl(true);
            bucketKeyValueItem.setCannedAclList(getParamList(ossId, BASE_CANNED_ACL_TYPE));
        } else if (StringUtils.equals(oss.getPluginId(), OSSConstants.huawei)) {
            bucketKeyValueItem.setShowCannedAcl(true);
            bucketKeyValueItem.setShowStorageClass(true);
            bucketKeyValueItem.setCannedAclList(getParamList(ossId, BASE_CANNED_ACL_TYPE));
            bucketKeyValueItem.setStorageList(getParamList(ossId, BASE_STORAGE_CLASS_TYPE));
        } else if (StringUtils.equals(oss.getPluginId(), OSSConstants.baidu)) {
            bucketKeyValueItem.setShowCannedAcl(true);
            bucketKeyValueItem.setShowStorageClass(true);
            bucketKeyValueItem.setCannedAclList(getParamList(ossId, BASE_CANNED_ACL_TYPE));
            bucketKeyValueItem.setStorageList(getParamList(ossId, BASE_STORAGE_CLASS_TYPE));
        } else if (StringUtils.equals(oss.getPluginId(), OSSConstants.ucloud)) {
            bucketKeyValueItem.setShowCannedAcl(true);
            bucketKeyValueItem.setShowStorageClass(true);
            bucketKeyValueItem.setCannedAclList(getParamList(ossId, BASE_CANNED_ACL_TYPE));
            bucketKeyValueItem.setStorageList(getParamList(ossId, BASE_STORAGE_CLASS_TYPE));
        } else if (StringUtils.equals(oss.getPluginId(), OSSConstants.qingcloud)) {
            bucketKeyValueItem.setShowCannedAcl(false);
            bucketKeyValueItem.setShowStorageClass(false);
            bucketKeyValueItem.setCannedAclList(getParamList(ossId, BASE_CANNED_ACL_TYPE));
            bucketKeyValueItem.setStorageList(getParamList(ossId, BASE_STORAGE_CLASS_TYPE));
        } else if (StringUtils.equals(oss.getPluginId(), OSSConstants.qiniu)) {
            bucketKeyValueItem.setShowCannedAcl(true);
            bucketKeyValueItem.setShowStorageClass(false);
            bucketKeyValueItem.setCannedAclList(getParamList(ossId, BASE_CANNED_ACL_TYPE));
            bucketKeyValueItem.setStorageList(getParamList(ossId, BASE_STORAGE_CLASS_TYPE));
        } else if (StringUtils.equals(oss.getPluginId(), OSSConstants.huoshan)) {
            bucketKeyValueItem.setShowCannedAcl(true);
            bucketKeyValueItem.setShowStorageClass(true);
            bucketKeyValueItem.setCannedAclList(getParamList(ossId, BASE_CANNED_ACL_TYPE));
            bucketKeyValueItem.setStorageList(getParamList(ossId, BASE_STORAGE_CLASS_TYPE));
        }
        return bucketKeyValueItem;
    }

    public List<OssRegion> getOssRegions(String ossId) throws Exception {
        OssWithBLOBs account = getAccountByPrimaryKey(ossId);
        OssProvider ossProvider = getOssProvider(account.getPluginId());
        return ossProvider.getOssRegions(account);
    }

    public List<KeyValueItem> getParamList(String ossId, String type) throws Exception {
        return getParamList(ossMapper.selectByPrimaryKey(ossId), type);
    }

    public List<KeyValueItem> getParamList(OssWithBLOBs ossAccount, String type) throws Exception {
        String path = "";
        switch (type) {
            case BASE_CANNED_ACL_TYPE:
                path = BASE_CANNED_ACL_DIC;
                break;
            case BASE_STORAGE_CLASS_TYPE:
                path = BASE_STORAGE_CLASS_DIC;
                break;
            default:
                HRException.throwException("Illegal parameter");
        }

        try {
            if (type.equals(BASE_STORAGE_CLASS_TYPE)) {
                if (ossAccount.getPluginId().equals(OSSConstants.aws) ||
                        ossAccount.getPluginId().equals(OSSConstants.tencent) ||
                        ossAccount.getPluginId().equals(OSSConstants.huoshan) ||
                        ossAccount.getPluginId().equals(OSSConstants.qingcloud) ||
                        ossAccount.getPluginId().equals(OSSConstants.qiniu) ||
                        ossAccount.getPluginId().equals(OSSConstants.ucloud)) {
                    return new ArrayList<KeyValueItem>();
                }
            }
            return getParams(ossAccount, path);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("Getting parameters failed: " + ossAccount.getPluginName(), e);
            throw new Exception("Failed to get parameters" + e.getMessage());
        }
    }

    private List<KeyValueItem> getParams(OssWithBLOBs ossAccount, String path) throws Exception {
        String result = ReadFileUtils.readConfigFile(path, ossAccount.getPluginId(), JSON_EXTENSION);
        return new Gson().fromJson(result, new TypeToken<ArrayList<KeyValueItem>>() {
        }.getType());
    }

    public List<RuleGroup> groups(String pluginId) {
        RuleGroupExample example = new RuleGroupExample();
        example.createCriteria().andPluginIdEqualTo(pluginId).andLevelEqualTo("对象存储");
        List<RuleGroup> groups = ruleGroupMapper.selectByExample(example);
        return groups;
    }

    public List<RuleGroupDTO> ruleGroupList(RuleGroupRequest request) {
        return extOssMapper.ruleGroupList(request);
    }

    /**
     * 导出excel
     */
    @SuppressWarnings(value = {"unchecked", "deprecation", "serial"})
    public byte[] exportGroupReport(ExcelExportRequest request, LoginUser loginUser) throws Exception {
        try {
            Map<String, Object> params = request.getParams();
            ResourceRequest resourceRequest = new ResourceRequest();
            if (MapUtils.isNotEmpty(params)) {
                org.apache.commons.beanutils.BeanUtils.populate(resourceRequest, params);
            }
            List<ExcelExportRequest.Column> columns = request.getColumns();
            List<ExportDTO> exportDTOs = searchGroupExportData(resourceRequest, request.getGroupId(), request.getAccountId());
            List<List<Object>> data = exportDTOs.stream().map(resource -> {
                return new ArrayList<Object>() {{
                    columns.forEach(column -> {
                        try {
                            switch (column.getKey()) {
                                case "auditName":
                                    add(resource.getFirstLevel() + "-" + resource.getSecondLevel());
                                    break;
                                case "basicRequirements":
                                    add(resource.getProject());
                                    break;
                                case "severity":
                                    add(resource.getSeverity());
                                    break;
                                case "hummerId":
                                    add(resource.getHummerId());
                                    break;
                                case "resourceName":
                                    add(resource.getResourceName());
                                    break;
                                case "resourceType":
                                    add(resource.getResourceType());
                                    break;
                                case "regionId":
                                    add(resource.getRegionId());
                                    break;
                                case "ruleName":
                                    add(resource.getRuleName());
                                    break;
                                case "ruleDescription":
                                    add(resource.getRuleDescription());
                                    break;
                                case "regionName":
                                    add(resource.getRegionName());
                                    break;
                                case "improvement":
                                    add(resource.getImprovement());
                                    break;
                                case "project":
                                    add(resource.getProject());
                                    break;
                                default:
                                    add(MethodUtils.invokeMethod(resource, "get" + StringUtils.capitalize(ExcelExportUtils.underlineToCamelCase(column.getKey()))));
                                    break;
                            }
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            LogUtil.error("export resource excel error: ", ExceptionUtils.getStackTrace(e));
                        }
                    });
                }};
            }).collect(Collectors.toList());
            operationLogService.log(loginUser, request.getAccountId(), "RESOURCE", ResourceTypeConstants.RESOURCE.name(), ResourceOperation.EXPORT, "i18n_export_report");
            return ExcelExportUtils.exportExcelData(Translator.get("i18n_scan_resource"), request.getColumns().stream().map(ExcelExportRequest.Column::getValue).collect(Collectors.toList()), data);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<ExportDTO> searchGroupExportData(ResourceRequest request, String groupId, String accountId) {
        return extOssMapper.searchGroupExportData(request, groupId, accountId);
    }

    public List<CloudTask> selectManualTasks(ManualRequest request) throws Exception {

        try {
            return extCloudTaskMapper.selectOssManualTasks(request);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<ResourceDTO> resourceList(ResourceRequest resourceRequest) {
        return extOssMapper.resourceList(resourceRequest, Arrays.asList(OSSConstants.SUPPORT_RESOURCE_TYPE));
    }

    private static String basePath = "/tmp/";

    public void uploadObject(String bucketId, String objectId, MultipartFile multipartFile, LoginUser loginUser) throws Exception {
        OssBucket bucket = getBucketByPrimaryKey(bucketId);
        OssWithBLOBs account = getAccountByPrimaryKey(bucket.getOssId());
        OssProvider ossProvider = getOssProvider(account.getPluginId());

        File localFile = new File(basePath + objectId);
        if (!localFile.getParentFile().exists())
        {
            localFile.getParentFile().mkdirs();
        }
        if (!localFile.exists())
        {
            localFile.createNewFile();
        }
        long size = multipartFile.getSize();
        multipartFile.transferTo(localFile.toPath().toAbsolutePath());
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(basePath + objectId);
                ossProvider.uploadFile(bucket, account, objectId, fis, size);
                operationLogService.log(loginUser, bucket.getBucketName(), objectId, ResourceTypeConstants.OSS.name(), ResourceOperation.UPLOAD, "i18n_upload_oss");
            } catch (Exception e) {
                LogUtil.error(String.format("Failed to upload file %s to %s, %s", objectId, bucket.getBucketName(), e.getMessage()));
                throw new RuntimeException("Failed to upload file");
            } finally {
                try {
                    if (fis != null)
                        fis.close();
                } catch (Exception ignore) {
                }
                localFile.deleteOnExit();
                if (localFile.exists()) {
                    localFile.delete();
                }
            }
    }

    public Map<String, Object> topInfo(Map<String, Object> params) {
        return extOssMapper.topInfo(params);
    }

    public List<Map<String, Object>> ossChart() {
        return extOssMapper.ossChart();
    }

    public List<Map<String, Object>> bucketChart() {
        return extOssMapper.bucketChart();
    }

    public List<Map<String, Object>> severityChart() {
        return extOssMapper.severityChart();
    }

}
