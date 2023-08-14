package com.hummer.cloud.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aliyuncs.exceptions.ClientException;
import com.hummer.cloud.mapper.*;
import com.hummer.cloud.mapper.ext.ExtAccountMapper;
import com.hummer.cloud.mapper.ext.ExtCloudProjectMapper;
import com.hummer.common.core.constant.CloudAccountConstants;
import com.hummer.common.core.constant.ResourceOperation;
import com.hummer.common.core.constant.ResourceTypeConstants;
import com.hummer.common.core.domain.*;
import com.hummer.common.core.domain.request.account.CloudAccountRequest;
import com.hummer.common.core.domain.request.account.CreateCloudAccountRequest;
import com.hummer.common.core.domain.request.account.UpdateCloudAccountRequest;
import com.hummer.common.core.dto.*;
import com.hummer.common.core.exception.HRException;
import com.hummer.common.core.i18n.Translator;
import com.hummer.common.core.utils.*;
import com.hummer.system.api.IOperationLogService;
import com.hummer.system.api.model.LoginUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.alibaba.fastjson.JSON.parseArray;
import static com.alibaba.fastjson.JSON.parseObject;

/**
 * @author harris
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CloudProjectService {

    @Autowired
    private ExtCloudProjectMapper extCloudProjectMapper;
    @Autowired
    private CloudProjectMapper cloudProjectMapper;
    @Autowired
    private CloudProjectLogMapper cloudProjectLogMapper;
    @Autowired
    private CloudGroupMapper cloudGroupMapper;
    @Autowired
    private CloudGroupLogMapper cloudGroupLogMapper;
    @Autowired
    private CloudProcessMapper cloudProcessMapper;
    @Autowired
    private CloudProcessLogMapper cloudProcessLogMapper;
    @Autowired
    private CloudTaskMapper cloudTaskMapper;
    @Autowired
    private CloudTaskItemMapper cloudTaskItemMapper;
    @DubboReference
    private IOperationLogService operationLogService;

    public List<CloudProjectDTO> getCloudProjectDTOs(CloudProject cloudProject) {
        return extCloudProjectMapper.getCloudProjectDTOs(cloudProject);
    }

    public CloudProjectDTO projectById(String projectId) {
        CloudProject cloudProject = new CloudProject();
        cloudProject.setId(projectId);
        List<CloudProjectDTO> list = extCloudProjectMapper.getCloudProjectDTOs(cloudProject);
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return new CloudProjectDTO();
        }
    }

    public void deletes(List<String> ids, LoginUser loginUser) throws Exception {
        ids.forEach(id -> {
            try {
                deleteProject(id, loginUser);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    public void deleteProject(String projectId, LoginUser loginUser) {

        CloudProject cloudProject = cloudProjectMapper.selectByPrimaryKey(projectId);
        cloudProjectMapper.deleteByPrimaryKey(projectId);

        CloudProjectLogExample cloudProjectLogExample = new CloudProjectLogExample();
        cloudProjectLogExample.createCriteria().andProjectIdEqualTo(projectId);
        cloudProjectLogMapper.deleteByExample(cloudProjectLogExample);

        CloudGroupExample cloudGroupExample = new CloudGroupExample();
        cloudGroupExample.createCriteria().andProjectIdEqualTo(projectId);
        cloudGroupMapper.deleteByExample(cloudGroupExample);

        CloudGroupLogExample cloudGroupLogExample = new CloudGroupLogExample();
        cloudGroupLogExample.createCriteria().andProjectIdEqualTo(projectId);
        cloudGroupLogMapper.deleteByExample(cloudGroupLogExample);

        CloudProcessExample cloudProcessExample = new CloudProcessExample();
        cloudProcessExample.createCriteria().andProjectIdEqualTo(projectId);
        cloudProcessMapper.deleteByExample(cloudProcessExample);

        CloudProcessLogExample cloudProcessLogExample = new CloudProcessLogExample();
        cloudProcessLogExample.createCriteria().andProjectIdEqualTo(projectId);
        cloudProcessLogMapper.deleteByExample(cloudProcessLogExample);

        operationLogService.log(loginUser, projectId, cloudProject.getAccountName(), ResourceTypeConstants.CLOUD_PROJECT.name(), ResourceOperation.DELETE, "i18n_delete_cloud_project");
    }

    public List<CloudGroupDTO> getCloudGroupDTOs(CloudGroup cloudGroup) {
        return extCloudProjectMapper.getCloudGroupDTOs(cloudGroup);
    }

    public CloudGroupDTO groupById(String groupId) {
        CloudGroup cloudGroup = new CloudGroup();
        cloudGroup.setId(groupId);
        List<CloudGroupDTO> list = extCloudProjectMapper.getCloudGroupDTOs(cloudGroup);
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return new CloudGroupDTO();
        }
    }

    public void deleteGroups(List<String> ids, LoginUser loginUser) throws Exception {
        ids.forEach(id -> {
            try {
                deleteGroup(id, loginUser);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    public void deleteGroup(String groupId, LoginUser loginUser) {

        CloudGroup cloudGroup = cloudGroupMapper.selectByPrimaryKey(groupId);
        cloudGroupMapper.deleteByPrimaryKey(groupId);

        CloudGroupLogExample cloudGroupLogExample = new CloudGroupLogExample();
        cloudGroupLogExample.createCriteria().andGroupIdEqualTo(groupId);
        cloudGroupLogMapper.deleteByExample(cloudGroupLogExample);

        operationLogService.log(loginUser, groupId, cloudGroup.getAccountName(), ResourceTypeConstants.CLOUD_GROUP.name(), ResourceOperation.DELETE, "i18n_delete_cloud_project");
    }

}