package com.hummerrisk.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hummerrisk.base.domain.CloudResourceSync;
import com.hummerrisk.base.domain.CloudResourceSyncItem;
import com.hummerrisk.commons.utils.PageUtils;
import com.hummerrisk.commons.utils.Pager;
import com.hummerrisk.controller.handler.annotation.I18n;
import com.hummerrisk.controller.request.cloudEvent.CloudEventRequest;
import com.hummerrisk.controller.request.cloudResource.CloudResourceSyncRequest;
import com.hummerrisk.dto.CloudResourceSyncItemDto;
import com.hummerrisk.service.CloudSyncService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

@ApiIgnore
@RestController
@RequestMapping(value = "cloud/sync")
public class CloudSyncController {
    @Resource
    private CloudSyncService cloudSyncService;

    @I18n
    @GetMapping(value = "sync/{accountId}")
    public void sync(@PathVariable String accountId) throws Exception {
        cloudSyncService.sync(accountId);
    }

    @I18n
    @GetMapping(value = "delete/{id}")
    public void delete(@PathVariable String id) throws Exception {
        cloudSyncService.deleteSync(id);
    }

    @I18n
    @PostMapping(value = "log/list/{goPage}/{pageSize}")
    public Pager<List<CloudResourceSync>> listResourceSync(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody CloudResourceSyncRequest cloudResourceSyncRequest) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, cloudSyncService.getCloudResourceSyncLogs(cloudResourceSyncRequest));
    }



    @I18n
    @GetMapping(value = "log/item/list/{syncId}")
    public List<CloudResourceSyncItemDto> listResourceSyncItem(@PathVariable String syncId) {
        return cloudSyncService.getCloudResourceSyncItem(syncId);
    }

}