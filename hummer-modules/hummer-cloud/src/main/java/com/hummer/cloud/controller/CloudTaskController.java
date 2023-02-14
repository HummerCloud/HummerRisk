package com.hummer.cloud.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hummer.common.mapper.handler.annotation.I18n;
import com.hummer.common.mapper.service.CloudTaskService;
import com.hummer.common.mapper.service.OrderService;
import com.hummer.common.core.domain.*;
import com.hummer.common.core.domain.request.cloudTask.CloudQuartzRequest;
import com.hummer.common.core.domain.request.cloudTask.ManualRequest;
import com.hummer.common.core.dto.*;
import com.hummer.common.core.utils.PageUtils;
import com.hummer.common.core.utils.Pager;
import org.apache.logging.log4j.core.util.CronExpression;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@ApiIgnore
@RestController
@RequestMapping(value = "cloud/task")
public class CloudTaskController {
    @Resource
    private CloudTaskService cloudTaskService;
    @Resource
    private OrderService orderService;

    @I18n
    @GetMapping(value = "detail/{taskId}")
    public CloudTaskDTO getTaskDetail(@PathVariable String taskId) {
        return orderService.getTaskDetail(taskId);
    }

    @I18n
    @GetMapping(value = "copy/{taskId}")
    public CloudTaskCopyDTO copy(@PathVariable String taskId) {
        return orderService.copy(taskId);
    }

    @I18n
    @GetMapping(value = "log/taskId/{taskId}")
    public List<CloudTaskItemLogDTO> getTaskItemLogByTask(@PathVariable String taskId) {
        return orderService.getTaskItemLogByTaskId(taskId);
    }

    @I18n
    @GetMapping(value = "quartz/log/taskId/{taskId}")
    public List<CloudTaskItemLogDTO> getQuartzLogByTask(@PathVariable String taskId) {
        return orderService.getQuartzLogByTask(taskId);
    }

    @I18n
    @PostMapping("quartz/log/{taskItemId}/{goPage}/{pageSize}")
    public Pager<List<CloudTaskItemLogWithBLOBs>> getquartzLogDetails(@PathVariable int goPage, @PathVariable int pageSize, @PathVariable String taskItemId) {
        CloudTaskItemWithBLOBs taskItem = orderService.taskItemWithBLOBs(taskItemId);
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, orderService.getQuartzLogByTaskItemId(taskItem));
    }

    @I18n
    @PostMapping("quartz/rela/log/{qzTaskId}/{goPage}/{pageSize}")
    public Pager<List<CloudAccountQuartzTaskRelaLogWithBLOBs>> getQuartzLogs(@PathVariable int goPage, @PathVariable int pageSize, @PathVariable String qzTaskId) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, orderService.getQuartzLogsById(qzTaskId));
    }

    @GetMapping(value = "extendinfo/{taskId}")
    public CloudTaskDTO getTaskExtendInfo(@PathVariable String taskId) {
        return orderService.getTaskExtendInfo(taskId);
    }

    @PostMapping(value = "retry/{taskId}")
    public void retryTask(@PathVariable String taskId) throws Exception {
        orderService.retry(taskId);
    }

    @PostMapping(value = "getCronDesc/{taskId}")
    public void getCronDesc(@PathVariable String taskId) throws Exception {
        orderService.getCronDesc(taskId);
    }

    @I18n
    @PostMapping("manual/list/{goPage}/{pageSize}")
    public Pager<List<CloudTask>> getManualTasks(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody ManualRequest request) throws Exception {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        request.setType("manual");
        return PageUtils.setPageInfo(page, cloudTaskService.selectManualTasks(request));
    }

    @GetMapping("manual/more/{taskId}")
    public boolean morelTask(@PathVariable String taskId) {
        return cloudTaskService.morelTask(taskId);
    }

    @I18n
    @PostMapping("manual/create")
    public CloudTask saveManualTask(@RequestBody QuartzTaskDTO quartzTaskDTO) {
        quartzTaskDTO.setType("manual");
        return cloudTaskService.saveManualTask(quartzTaskDTO, null);
    }

    @PostMapping("manual/delete")
    public void deleteManualTask(@RequestBody String quartzTaskId) {
        cloudTaskService.deleteManualTask(quartzTaskId);
    }

    @PostMapping(value = "manual/dryRun")
    public boolean dryRun(@RequestBody QuartzTaskDTO quartzTaskDTO) {
        quartzTaskDTO.setType("manual");
        return cloudTaskService.dryRun(quartzTaskDTO);
    }

    @I18n
    @PostMapping("quartz/list/{goPage}/{pageSize}")
    public Pager<List<CloudAccountQuartzTask>> getQuartzTasks(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody CloudQuartzRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, cloudTaskService.selectQuartzTasks(request));
    }

    @PostMapping("quartz/create")
    public boolean saveQuartzTask(@RequestBody CloudAccountQuartzTaskDTO cloudAccountQuartzTaskDTO) throws Exception {
        return cloudTaskService.saveQuartzTask(cloudAccountQuartzTaskDTO);
    }

    @GetMapping("quartz/pause/{quartzTaskId}")
    public boolean pauseQuartzTask(@PathVariable String quartzTaskId) throws Exception {
        return cloudTaskService.changeQuartzStatus(quartzTaskId, "pause");
    }

    @GetMapping("quartz/resume/{quartzTaskId}")
    public boolean resumeQuartzTask(@PathVariable String quartzTaskId) throws Exception {
        return cloudTaskService.changeQuartzStatus(quartzTaskId, "resume");
    }

    @GetMapping("quartz/delete/{quartzTaskId}")
    public void deleteQuartzTask(@PathVariable String quartzTaskId) {
        cloudTaskService.deleteQuartzTask(quartzTaskId);
    }

    @PostMapping(value = "quartz/dryRun")
    public boolean quartzDryRun(@RequestBody QuartzTaskDTO quartzTaskDTO) {
        quartzTaskDTO.setType("quartz");
        return cloudTaskService.dryRun(quartzTaskDTO);
    }

    @PostMapping(value = "quartz/validateCron")
    public boolean validateCron(@RequestBody Map<String, String> map) {
        return CronExpression.isValidExpression(map.get("cron"));
    }

    @I18n
    @GetMapping("show/account/{taskId}")
    public ShowAccountQuartzTaskDTO showAccount(@PathVariable String taskId) {
        return cloudTaskService.showAccount(taskId);
    }

    @I18n
    @PostMapping("vuln/list/{goPage}/{pageSize}")
    public Pager<List<CloudTask>> getVulnTasks(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody Map<String, Object> param) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        param.put("type", "manual");
        return PageUtils.setPageInfo(page, cloudTaskService.getVulnTasks(param));
    }
}
