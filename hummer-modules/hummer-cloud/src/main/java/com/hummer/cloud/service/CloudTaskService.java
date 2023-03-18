package com.hummer.cloud.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hummer.cloud.mapper.*;
import com.hummer.cloud.mapper.ext.ExtCloudTaskMapper;
import com.hummer.cloud.mapper.ext.ExtQuartzTaskMapper;
import com.hummer.common.core.constant.*;
import com.hummer.common.core.domain.*;
import com.hummer.common.core.domain.request.cloudTask.CloudQuartzRequest;
import com.hummer.common.core.domain.request.cloudTask.ManualRequest;
import com.hummer.common.core.dto.*;
import com.hummer.common.core.exception.HRException;
import com.hummer.common.core.i18n.Translator;
import com.hummer.common.core.utils.*;
import com.hummer.common.security.service.TokenService;
import com.hummer.quartz.service.QuartzManageService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.JSON.parseArray;

/**
 * @author harris
 */
@Service
public class CloudTaskService {

    @Autowired
    private CloudTaskMapper cloudTaskMapper;
    @Autowired
    private CloudTaskItemMapper cloudTaskItemMapper;
    @Autowired
    private ExtCloudTaskMapper extCloudTaskMapper;
    @Autowired
    private CloudTaskItemLogMapper cloudTaskItemLogMapper;
    @Autowired
    private CloudTaskItemResourceMapper cloudTaskItemResourceMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private QuartzManageService quartzManageService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ResourceRuleMapper resourceRuleMapper;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ResourceItemMapper resourceItemMapper;
    @Autowired
    private ProxyMapper proxyMapper;
    @Autowired
    private CloudAccountQuartzTaskMapper quartzTaskMapper;
    @Autowired
    private ExtQuartzTaskMapper extQuartzTaskMapper;
    @Autowired
    private CloudAccountQuartzTaskRelationMapper quartzTaskRelationMapper;
    @Autowired
    private RuleMapper ruleMapper;
    @Autowired
    private CloudAccountQuartzTaskRelaLogMapper quartzTaskRelaLogMapper;
    @Autowired
    private ProwlerService prowlerService;
    @Autowired
    private TokenService tokenService;

    public CloudTask saveManualTask(QuartzTaskDTO quartzTaskDTO, String messageOrderId) {
        try {
            if (StringUtils.equalsIgnoreCase(quartzTaskDTO.getScanType(), ScanTypeConstants.custodian.name())) {
                this.validateYaml(quartzTaskDTO);
                return orderService.createTask(quartzTaskDTO, CloudTaskConstants.TASK_STATUS.APPROVED.name(), messageOrderId);
            } else if (StringUtils.equalsIgnoreCase(quartzTaskDTO.getScanType(), ScanTypeConstants.prowler.name())) {
                return prowlerService.createTask(quartzTaskDTO, CloudTaskConstants.TASK_STATUS.APPROVED.name(), messageOrderId);
            } else {
                return orderService.createTask(quartzTaskDTO, CloudTaskConstants.TASK_STATUS.APPROVED.name(), messageOrderId);
            }
        } catch (Exception e) {
            throw new HRException(e.getMessage());
        }
    }

    public boolean morelTask(String taskId) {
        try {
            CloudTask cloudTask = cloudTaskMapper.selectByPrimaryKey(taskId);
            if (cloudTask != null) {
                cloudTask.setStatus(CloudTaskConstants.TASK_STATUS.APPROVED.name());
                cloudTaskMapper.updateByPrimaryKeySelective(cloudTask);
            } else {
                HRException.throwException("CloudTask not found");
            }
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    public boolean validateYaml(QuartzTaskDTO quartzTaskDTO) {
        try {
            String script = quartzTaskDTO.getScript();
            JSONArray jsonArray = JSON.parseArray(quartzTaskDTO.getParameter());
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String key = "${{" + jsonObject.getString("key") + "}}";
                if (script.contains(key)) {
                    script = script.replace(key, jsonObject.getString("defaultValue"));
                }
            }
            Yaml yaml = new Yaml();
            Map map = yaml.load(script);
            if (map != null) {
                List<Map> list = (List) map.get("policies");
                for (Map m : list) m.get("resource").toString();
            }
        } catch (Exception e) {
            HRException.throwException(Translator.get("i18n_compliance_rule_code_error"));
        }
        return true;
    }

    public void deleteManualTask(String taskId) {
        CloudTask cloudTask = cloudTaskMapper.selectByPrimaryKey(taskId);
        CloudTaskItemExample cloudTaskItemExample = new CloudTaskItemExample();
        cloudTaskItemExample.createCriteria().andTaskIdEqualTo(cloudTask.getId());
        List<CloudTaskItem> cloudTaskItemList = cloudTaskItemMapper.selectByExample(cloudTaskItemExample);
        try {
            cloudTaskItemList.forEach(taskItem -> {
                if (taskItem == null) return;
                cloudTaskItemMapper.deleteByPrimaryKey(taskItem.getId());

                CloudTaskItemLogExample cloudTaskItemLogExample = new CloudTaskItemLogExample();
                cloudTaskItemLogExample.createCriteria().andTaskItemIdEqualTo(taskItem.getId());
                cloudTaskItemLogMapper.deleteByExample(cloudTaskItemLogExample);

                CloudTaskItemResourceExample cloudTaskItemResourceExample = new CloudTaskItemResourceExample();
                cloudTaskItemResourceExample.createCriteria().andTaskItemIdEqualTo(taskItem.getId());
                List<CloudTaskItemResource> cloudTaskItemResources = cloudTaskItemResourceMapper.selectByExample(cloudTaskItemResourceExample);
                cloudTaskItemResourceMapper.deleteByExample(cloudTaskItemResourceExample);
                cloudTaskItemResources.forEach(taskItemResource -> {
                    if (taskItemResource == null) return;
                    resourceMapper.deleteByPrimaryKey(taskItemResource.getResourceId());

                    if (taskItemResource.getResourceId() != null) {
                        ResourceItemExample resourceItemExample = new ResourceItemExample();
                        resourceItemExample.createCriteria().andResourceIdEqualTo(taskItemResource.getResourceId());
                        List<ResourceItem> resourceItems = resourceItemMapper.selectByExample(resourceItemExample);
                        resourceItems.forEach(resourceItem -> {
                            ResourceRuleExample resourceRuleExample = new ResourceRuleExample();
                            if (resourceItem.getResourceId() != null) {
                                resourceRuleExample.createCriteria().andResourceIdEqualTo(resourceItem.getResourceId());
                                resourceRuleMapper.deleteByExample(resourceRuleExample);
                            }
                        });
                    }
                });

            });
            cloudTaskMapper.deleteByPrimaryKey(cloudTask.getId());
            OperationLogService.log(tokenService.getLoginUser().getUser(), taskId, cloudTask.getDescription(), ResourceTypeConstants.TASK.name(), ResourceOperation.DELETE, "i18n_delete_cloud_task");
        } catch (Exception e) {
            LogUtil.error("Delete manual cloudTask error{} " + e.getMessage());
            HRException.throwException(e.getMessage());
        }
    }

    public boolean dryRun(QuartzTaskDTO quartzTaskDTO) {
        //validate && dryrun
        String uuid = UUIDUtil.newUUID();
        try {
            String script = quartzTaskDTO.getScript();
            JSONArray jsonArray = JSON.parseArray(quartzTaskDTO.getParameter());
            String groupName = "xss";
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String key = "${{" + jsonObject.getString("key") + "}}";
                if (script.contains(key)) {
                    script = script.replace(key, jsonObject.getString("defaultValue"));
                }
                groupName = jsonObject.getString("defaultValue");
            }
            final String finalScript = script;
            String dirPath;
            AccountExample example = new AccountExample();
            example.createCriteria().andPluginIdEqualTo(quartzTaskDTO.getPluginId()).andStatusEqualTo("VALID");
            AccountWithBLOBs account = accountMapper.selectByExampleWithBLOBs(example).get(0);
            Proxy proxy = new Proxy();
            if (account.getProxyId() != null) proxy = proxyMapper.selectByPrimaryKey(account.getProxyId());
            // 校验云账号是否有效
            Optional.ofNullable(accountService.validate(account.getId()).isFlag()).filter(Boolean::booleanValue).orElseGet(() -> {
                HRException.throwException(Translator.get("i18n_ex_plugin_validate"));
                return null;
            });
            JSONObject regionObj = Optional.ofNullable(PlatformUtils._getRegions(account, proxy, true)).filter(s -> {
                return !s.isEmpty();
            }).map(jsonArr -> {
                return (JSONObject) jsonArr.get(0);
            }).orElseGet(() -> {
                HRException.throwException(Translator.get("i18n_ex_plugin_validate"));
                return null;
            });

            Map<String, String> map = PlatformUtils.getAccount(account, regionObj.get("regionId").toString(), proxyMapper.selectByPrimaryKey(account.getProxyId()));

            String fileName = "", commandEnum = "";
            if (StringUtils.equalsIgnoreCase(quartzTaskDTO.getScanType(), ScanTypeConstants.custodian.name())) {
                fileName = "policy.yml";
                commandEnum = CommandEnum.custodian.getCommand();
            } else if (StringUtils.equalsIgnoreCase(quartzTaskDTO.getScanType(), ScanTypeConstants.prowler.name())) {
                JSONArray objects = JSONObject.parseArray(quartzTaskDTO.getParameter());
                if (objects.isEmpty()) HRException.throwException(Translator.get("error_lang_invalid"));
                fileName = objects.getJSONObject(0).getString("defaultValue");
                commandEnum = CommandEnum.prowler.getCommand();
            }

            dirPath = commandEnum.equals(CommandEnum.prowler.getCommand()) ? CloudTaskConstants.PROWLER_RESULT_FILE_PATH : CommandUtils.saveAsFile(finalScript, CloudTaskConstants.RESULT_FILE_PATH_PREFIX + uuid, fileName, false);

            String command = PlatformUtils.fixedCommand(commandEnum, CommandEnum.validate.getCommand(), dirPath, fileName, map);

            String resultStr = CommandUtils.commonExecCmdWithResult(command, dirPath);
            // 检查结果
            checkResultStr(resultStr, quartzTaskDTO.getScanType());
            String command2 = PlatformUtils.fixedCommand(commandEnum, CommandEnum.dryrun.getCommand(), dirPath, fileName, map);
            String resultStr2 = CommandUtils.commonExecCmdWithResult(command2, dirPath);
            // 结果
            checkResultStr(resultStr2, quartzTaskDTO.getScanType());
        } catch (Exception e) {
            LogUtil.error("[{}] validate && dryrun generate policy.yml file failed:{}", uuid, e.getMessage());
            HRException.throwException(e instanceof HRException ? e.getMessage() : Translator.get("i18n_compliance_rule_error"));
        }
        return true;
    }

    /**
     * 检查返回值是否正常返回
     *
     * @param resultStr 需要检查的结果
     * @param type      检测类型
     */
    public void checkResultStr(String resultStr, String type) {
        if (type.equals(ScanTypeConstants.custodian.name())) {
            if (!resultStr.isEmpty() && !resultStr.contains("INFO")) {
                LogUtil.error(Translator.get("i18n_has_resource_failed") + " {validate}:" + resultStr);
                HRException.throwException(Translator.get("i18n_has_resource_failed"));
            }
        } else if (type.equals(ScanTypeConstants.prowler.name())) {
            if (!resultStr.isEmpty() && !resultStr.contains("INFO")) {
                LogUtil.error(Translator.get("i18n_has_resource_failed") + " {validate}:" + resultStr);
                HRException.throwException(Translator.get("i18n_has_resource_failed"));
            }
        }
    }

    public List<CloudTask> selectManualTasks(ManualRequest request) throws Exception {
        try {
            return extCloudTaskMapper.selectManualTasks(request);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public boolean checkRuleTaskStatus(String accountId, String ruleId, String[] status) {
        CloudTaskExample cloudTaskExample = new CloudTaskExample();
        cloudTaskExample.createCriteria().andAccountIdEqualTo(accountId).andRuleIdEqualTo(ruleId).andStatusIn(Arrays.stream(status).collect(Collectors.toList()));
        long count = cloudTaskMapper.countByExample(cloudTaskExample);
        return count > 0;
    }

    public List<CloudAccountQuartzTask> selectQuartzTasks(CloudQuartzRequest request) {
        return extQuartzTaskMapper.selectQuartzTasks(request);
    }

    public boolean saveQuartzTask(CloudAccountQuartzTaskDTO dto) throws Exception {
        try {
            dto.setId(UUIDUtil.newUUID());
            dto.setApplyUser(tokenService.getLoginUser().getUserId());
            dto.setCreateTime(System.currentTimeMillis());
            dto.setStatus(CloudTaskConstants.TASK_STATUS.RUNNING.name());
            dto.setCronDesc(DescCornUtils.descCorn(dto.getCron()));

            Trigger trigger = addQuartzTask(dto);
            dto.setTriggerId("quartz-cloudTask" + dto.getId());
            dto.setLastFireTime(trigger.getNextFireTime().getTime());
            if (trigger.getPreviousFireTime() != null) dto.setPrevFireTime(trigger.getPreviousFireTime().getTime());
            quartzTaskMapper.insertSelective(dto);

            if (StringUtils.equalsIgnoreCase(dto.getQzType(), "ACCOUNT")) {
                for (String accountId : dto.getAccountIds()) {
                    CloudAccountQuartzTaskRelation quartzTaskRelation = new CloudAccountQuartzTaskRelation();
                    quartzTaskRelation.setId(UUIDUtil.newUUID());
                    quartzTaskRelation.setQuartzTaskId(dto.getId());
                    quartzTaskRelation.setCreateTime(System.currentTimeMillis());
                    quartzTaskRelation.setSourceId(accountId);
                    quartzTaskRelation.setQzType(dto.getQzType());
                    quartzTaskRelationMapper.insertSelective(quartzTaskRelation);

                    AccountWithBLOBs account = accountMapper.selectByPrimaryKey(accountId);
                    RuleExample example = new RuleExample();
                    JSONArray jsonArray = new JSONArray();
                    example.createCriteria().andPluginIdEqualTo(account.getPluginId());
                    List<Rule> rules = ruleMapper.selectByExample(example);
                    for (Rule rule : rules) {
                        QuartzTaskDTO quartzTaskDTO = new QuartzTaskDTO();
                        BeanUtils.copyBean(quartzTaskDTO, rule);
                        quartzTaskDTO.setType("quartz");
                        quartzTaskDTO.setAccountId(accountId);
                        quartzTaskDTO.setCron(dto.getCron());
                        quartzTaskDTO.setTaskName(rule.getName());

                        List<SelectTag> selectTags = new LinkedList<>();
                        SelectTag s = new SelectTag();
                        s.setAccountId(account.getId());
                        JSONArray j = parseArray(account.getRegions());
                        JSONObject object;
                        List<String> regions = new ArrayList<>();
                        for (int i = 0; i < j.size(); i++) {
                            object = j.getJSONObject(i);
                            String value = object.getString("regionId");
                            regions.add(value);
                        }
                        s.setRegions(regions);
                        selectTags.add(s);
                        quartzTaskDTO.setSelectTags(selectTags);
                        quartzTaskDTO.setRegions(regions.toString());
                        CloudTask cloudTask = orderService.createTask(quartzTaskDTO, CloudTaskConstants.TASK_STATUS.APPROVED.name(), null);
                        cloudTask.setLastFireTime(dto.getLastFireTime());
                        cloudTask.setPrevFireTime(dto.getPrevFireTime());
                        cloudTask.setTriggerId(dto.getTriggerId());
                        cloudTaskMapper.updateByPrimaryKeySelective(cloudTask);

                        jsonArray.add(cloudTask.getId());
                    }

                    quartzTaskRelation.setTaskIds(jsonArray.toJSONString());
                    quartzTaskRelationMapper.updateByPrimaryKeySelective(quartzTaskRelation);

                    CloudAccountQuartzTaskRelaLogWithBLOBs quartzTaskRelaLog = new CloudAccountQuartzTaskRelaLogWithBLOBs();
                    quartzTaskRelaLog.setCreateTime(System.currentTimeMillis());
                    quartzTaskRelaLog.setQuartzTaskId(dto.getId());
                    quartzTaskRelaLog.setQuartzTaskRelaId(quartzTaskRelation.getId());
                    quartzTaskRelaLog.setTaskIds(quartzTaskRelation.getTaskIds());
                    quartzTaskRelaLog.setSourceId(quartzTaskRelation.getSourceId());
                    quartzTaskRelaLog.setQzType(quartzTaskRelation.getQzType());
                    quartzTaskRelaLog.setOperator(tokenService.getLoginUser().getUser().getName());
                    quartzTaskRelaLog.setOperation("i18n_create_qrtz_cloud_task");
                    quartzTaskRelaLogMapper.insertSelective(quartzTaskRelaLog);
                }
            } else {
                JSONArray jsonArray = new JSONArray();
                for (String ruleId : dto.getRuleIds()) {
                    CloudAccountQuartzTaskRelation quartzTaskRelation = new CloudAccountQuartzTaskRelation();
                    quartzTaskRelation.setId(UUIDUtil.newUUID());
                    quartzTaskRelation.setQuartzTaskId(dto.getId());
                    quartzTaskRelation.setCreateTime(System.currentTimeMillis());
                    quartzTaskRelation.setSourceId(ruleId);
                    quartzTaskRelation.setQzType(dto.getQzType());
                    quartzTaskRelationMapper.insertSelective(quartzTaskRelation);

                    QuartzTaskDTO quartzTaskDTO = new QuartzTaskDTO();
                    BeanUtils.copyBean(quartzTaskDTO, ruleMapper.selectByPrimaryKey(ruleId));
                    quartzTaskDTO.setType("quartz");
                    quartzTaskDTO.setAccountId(dto.getAccountId());
                    quartzTaskDTO.setCron(dto.getCron());
                    quartzTaskDTO.setTaskName(ruleMapper.selectByPrimaryKey(ruleId).getName());

                    List<SelectTag> selectTags = new LinkedList<>();
                    SelectTag s = new SelectTag();
                    s.setAccountId(dto.getAccountId());
                    JSONArray j = parseArray(accountMapper.selectByPrimaryKey(dto.getAccountId()).getRegions());
                    JSONObject object;
                    List<String> regions = new ArrayList<>();
                    for (int i = 0; i < j.size(); i++) {
                        object = j.getJSONObject(i);
                        String value = object.getString("regionId");
                        regions.add(value);
                    }
                    s.setRegions(regions);
                    selectTags.add(s);
                    quartzTaskDTO.setSelectTags(selectTags);
                    quartzTaskDTO.setRegions(regions.toString());
                    CloudTask cloudTask = orderService.createTask(quartzTaskDTO, CloudTaskConstants.TASK_STATUS.APPROVED.name(), null);
                    cloudTask.setLastFireTime(dto.getLastFireTime());
                    cloudTask.setPrevFireTime(dto.getPrevFireTime());
                    cloudTask.setTriggerId(dto.getTriggerId());
                    cloudTaskMapper.updateByPrimaryKeySelective(cloudTask);

                    jsonArray.add(cloudTask.getId());
                    quartzTaskRelation.setTaskIds(jsonArray.toJSONString());
                    quartzTaskRelationMapper.updateByPrimaryKeySelective(quartzTaskRelation);

                    CloudAccountQuartzTaskRelaLogWithBLOBs quartzTaskRelaLog = new CloudAccountQuartzTaskRelaLogWithBLOBs();
                    quartzTaskRelaLog.setCreateTime(System.currentTimeMillis());
                    quartzTaskRelaLog.setQuartzTaskId(dto.getId());
                    quartzTaskRelaLog.setQuartzTaskRelaId(quartzTaskRelation.getId());
                    quartzTaskRelaLog.setTaskIds(quartzTaskRelation.getTaskIds());
                    quartzTaskRelaLog.setSourceId(quartzTaskRelation.getSourceId());
                    quartzTaskRelaLog.setQzType(quartzTaskRelation.getQzType());
                    quartzTaskRelaLog.setOperator(tokenService.getLoginUser().getUser().getName());
                    quartzTaskRelaLog.setOperation("i18n_create_qrtz_cloud_task");
                    quartzTaskRelaLogMapper.insertSelective(quartzTaskRelaLog);
                }
            }
            OperationLogService.log(tokenService.getLoginUser().getUser(), dto.getId(), dto.getName(), ResourceTypeConstants.QUOTA.name(), ResourceOperation.CREATE, "i18n_create_qrtz_cloud_task");
        } catch (Exception e) {
            throw e;
        }
        return true;
    }


    private Trigger addQuartzTask(CloudAccountQuartzTask quartzTask) throws Exception {
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("quartz-cloudTask" + quartzTask.getId())
                .withSchedule(CronScheduleBuilder.cronSchedule(quartzTask.getCron()).withMisfireHandlingInstructionDoNothing())
                .build();

        quartzManageService.addJob("orderService", "createQuartzTask", trigger, quartzTask.getId());
        if (quartzTask.getStatus().equalsIgnoreCase(QuartzTaskStatus.PAUSE)) {
            quartzManageService.pauseJob(trigger.getJobKey());
        }
        return quartzManageService.getTrigger(trigger.getKey());

    }


    public void reAddQuartzOnStart() {
        List<CloudAccountQuartzTask> quartzTasks = quartzTaskMapper.selectByExample(null);
        quartzTasks.forEach(quartzTask -> {
            try {
                if (quartzManageService.getTrigger(new TriggerKey(quartzTask.getTriggerId())) == null) {
                    Trigger trigger = addQuartzTask(getResources(quartzTask.getId()));
                    quartzTask.setLastFireTime(trigger.getNextFireTime().getTime());
                    if (trigger.getPreviousFireTime() != null)
                        quartzTask.setPrevFireTime(trigger.getPreviousFireTime().getTime());
                    quartzTask.setTriggerId("quartz-cloudTask" + quartzTask.getId());
                    quartzTaskMapper.updateByPrimaryKeySelective(quartzTask);
                }
            } catch (Exception e) {
                HRException.throwException(e);
            }
        });
    }

    public CloudAccountQuartzTask getResources(String quartzTaskId) {
        return quartzTaskMapper.selectByPrimaryKey(quartzTaskId);
    }

    public void syncTriggerTime() {
        List<CloudAccountQuartzTask> quartzTasks = quartzTaskMapper.selectByExample(null);
        quartzTasks.forEach(quartzTask -> {
            Trigger trigger = null;
            try {
                trigger = quartzManageService.getTrigger(new TriggerKey(quartzTask.getTriggerId()));
            } catch (Exception e) {
                HRException.throwException(e);
            }
            quartzTask.setLastFireTime(trigger.getNextFireTime().getTime());
            if (trigger.getPreviousFireTime() != null)
                quartzTask.setPrevFireTime(trigger.getPreviousFireTime().getTime());
            quartzTaskMapper.updateByPrimaryKeySelective(quartzTask);

        });

    }

    public void syncTaskSum() {
        CloudTaskExample example = new CloudTaskExample();
        example.createCriteria().andStatusIn(Arrays.asList(CloudTaskConstants.TASK_STATUS.FINISHED.name(), CloudTaskConstants.TASK_STATUS.RUNNING.name()));
        List<CloudTask> cloudTasks = cloudTaskMapper.selectByExample(example);
        cloudTasks.forEach(task -> {
            if (task.getResourcesSum() != null && task.getReturnSum() != null) {
                int resourceSum = extCloudTaskMapper.getResourceSum(task.getId());
                int returnSum = extCloudTaskMapper.getReturnSum(task.getId());
                task.setResourcesSum((long) resourceSum);
                task.setReturnSum((long) returnSum);
                cloudTaskMapper.updateByPrimaryKeySelective(task);
            }
        });
    }

    public boolean changeQuartzStatus(String quartzId, final String action) throws Exception {
        try {
            CloudAccountQuartzTask quartzTask = quartzTaskMapper.selectByPrimaryKey(quartzId);
            String triggerId = quartzTask.getTriggerId();
            Trigger trigger = quartzManageService.getTrigger(new TriggerKey(triggerId));
            String status = null;
            switch (action) {
                case QuartzTaskAction.PAUSE:
                    quartzManageService.pauseJob(trigger.getJobKey());
                    status = QuartzTaskStatus.PAUSE;
                    break;

                case QuartzTaskAction.RESUME:
                    quartzManageService.resumeJob(trigger.getJobKey());
                    status = QuartzTaskStatus.RUNNING;
                    break;
                default:
                    HRException.throwException("action is not invalid");
            }
            quartzTask.setStatus(status);
            quartzTaskMapper.updateByPrimaryKeySelective(quartzTask);

            CloudAccountQuartzTaskRelationExample example = new CloudAccountQuartzTaskRelationExample();
            example.createCriteria().andQuartzTaskIdEqualTo(quartzId);
            List<CloudAccountQuartzTaskRelation> list = quartzTaskRelationMapper.selectByExampleWithBLOBs(example);

            for (CloudAccountQuartzTaskRelation quartzTaskRelation : list) {
                CloudAccountQuartzTaskRelaLogWithBLOBs quartzTaskRelaLog = new CloudAccountQuartzTaskRelaLogWithBLOBs();
                quartzTaskRelaLog.setCreateTime(System.currentTimeMillis());
                quartzTaskRelaLog.setQuartzTaskId(quartzId);
                quartzTaskRelaLog.setQuartzTaskRelaId(quartzTaskRelation.getId());
                quartzTaskRelaLog.setTaskIds(quartzTaskRelation.getTaskIds());
                quartzTaskRelaLog.setSourceId(quartzTaskRelation.getSourceId());
                quartzTaskRelaLog.setQzType(quartzTaskRelation.getQzType());
                quartzTaskRelaLog.setOperator("System");
                quartzTaskRelaLog.setOperation(action.equals(QuartzTaskAction.PAUSE) ? "i18n_pause_qrtz" : "i18n_start_qrtz");
                quartzTaskRelaLogMapper.insertSelective(quartzTaskRelaLog);
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public void deleteQuartzTask(String quartzTaskId) {
        CloudAccountQuartzTask quartzTask = quartzTaskMapper.selectByPrimaryKey(quartzTaskId);
        String triggerId = quartzTask.getTriggerId();
        Trigger trigger;
        try {
            trigger = quartzManageService.getTrigger(new TriggerKey(triggerId));
            quartzManageService.deleteJob(trigger.getJobKey());
        } catch (Exception e) {
            LogUtil.warn("Scheduled cloudTask not found！");
        } finally {
            //删除整个任务
            CloudAccountQuartzTaskRelationExample example = new CloudAccountQuartzTaskRelationExample();
            example.createCriteria().andQuartzTaskIdEqualTo(quartzTaskId);
            List<CloudAccountQuartzTaskRelation> quartzTaskRelations = quartzTaskRelationMapper.selectByExample(example);
            for (CloudAccountQuartzTaskRelation quartzTaskRelation : quartzTaskRelations) {
                CloudAccountQuartzTaskRelaLogExample quartzTaskRelaLogExample = new CloudAccountQuartzTaskRelaLogExample();
                quartzTaskRelaLogExample.createCriteria().andQuartzTaskRelaIdEqualTo(quartzTaskRelation.getId());
                quartzTaskRelaLogMapper.deleteByExample(quartzTaskRelaLogExample);
                quartzTaskRelationMapper.deleteByPrimaryKey(quartzTaskRelation.getId());
            }
            quartzTaskMapper.deleteByPrimaryKey(quartzTaskId);
            OperationLogService.log(tokenService.getLoginUser().getUser(), quartzTaskId, quartzTask.getName(), ResourceTypeConstants.QUOTA.name(), ResourceOperation.DELETE, "i18n_delete_qrtz_cloud_task");
        }
    }

    public ShowAccountQuartzTaskDTO showAccount(String taskId) {
        try {
            ShowAccountQuartzTaskDTO showAccountQuartzTaskDTO = new ShowAccountQuartzTaskDTO();

            CloudAccountQuartzTask quartzTask = quartzTaskMapper.selectByPrimaryKey(taskId);

            CloudAccountQuartzTaskRelationExample example = new CloudAccountQuartzTaskRelationExample();
            example.createCriteria().andQuartzTaskIdEqualTo(taskId);
            List<CloudAccountQuartzTaskRelation> quartzTaskRelationList = quartzTaskRelationMapper.selectByExampleWithBLOBs(example);

            List<ShowAccountQuartzTaskRelationDto> quartzTaskRelationDtos = new ArrayList<>();
            for (CloudAccountQuartzTaskRelation rela : quartzTaskRelationList) {
                ShowAccountQuartzTaskRelationDto dto = new ShowAccountQuartzTaskRelationDto();
                BeanUtils.copyBean(dto, rela);

                JSONArray jsonArray = JSONArray.parseArray(rela.getTaskIds());
                List<CloudTaskDTO> taskList = new ArrayList<>();
                for (Object obj : jsonArray) {
                    CloudTask cloudTask = cloudTaskMapper.selectByPrimaryKey(obj.toString());
                    CloudTaskDTO t = BeanUtils.copyBean(new CloudTaskDTO(), cloudTask);
                    t.setAccountName(accountMapper.selectByPrimaryKey(cloudTask.getAccountId()).getName());
                    taskList.add(t);
                }
                dto.setCloudTaskDTOList(taskList);

                quartzTaskRelationDtos.add(dto);
            }
            showAccountQuartzTaskDTO.setQuartzTaskRelationDtos(quartzTaskRelationDtos);
            BeanUtils.copyBean(showAccountQuartzTaskDTO, quartzTask);
            return showAccountQuartzTaskDTO;
        } catch (Exception e) {
            throw new HRException(e.getMessage());
        }
    }

}

class QuartzTaskStatus {
    protected final static String ERROR = "ERROR";
    protected final static String PAUSE = "PAUSE";
    protected final static String RUNNING = "RUNNING";
}

class QuartzTaskAction {
    protected final static String PAUSE = "pause";
    protected final static String RESUME = "resume";
}

