package com.hummer.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hummer.common.core.constant.RoleConstants;
import com.hummer.common.core.domain.User;
import com.hummer.common.core.domain.request.member.AddMemberRequest;
import com.hummer.common.core.domain.request.member.EditPassWordRequest;
import com.hummer.common.core.domain.request.member.QueryMemberRequest;
import com.hummer.common.core.domain.request.member.UserRequest;
import com.hummer.common.core.domain.request.organization.AddOrgMemberRequest;
import com.hummer.common.core.domain.request.organization.QueryOrgMemberRequest;
import com.hummer.common.core.dto.UserDTO;
import com.hummer.common.core.dto.UserRoleDTO;
import com.hummer.common.core.handler.annotation.I18n;
import com.hummer.common.core.user.SessionUser;
import com.hummer.common.core.utils.PageUtils;
import com.hummer.common.core.utils.Pager;
import com.hummer.common.security.service.TokenService;
import com.hummer.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户")
@RequestMapping("user")
@RestController
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private TokenService tokenService;

    @I18n
    @ApiOperation(value = "添加用户")
    @PostMapping("/special/add")
    public UserDTO insertUser(@RequestBody UserRequest user) throws Exception {
        return userService.insert(user);
    }

    @I18n
    @ApiOperation(value = "用户列表")
    @PostMapping("/special/list/{goPage}/{pageSize}")
    public Pager<List<User>> getUserList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody UserRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, userService.getUserListWithRequest(request));
    }

    @I18n
    @ApiOperation(value = "用户角色")
    @GetMapping("/special/user/role/{userId}")
    public UserRoleDTO getUserRole(@PathVariable("userId") String userId) {
        return userService.getUserRole(userId);
    }

    @ApiOperation(value = "删除用户")
    @GetMapping("/special/delete/{userId}")
    public void deleteUser(@PathVariable(value = "userId") String userId) {
        userService.deleteUser(userId);
    }

    @ApiOperation(value = "更新用户")
    @PostMapping("/special/update")
    public void updateUser(@RequestBody UserRequest user) {
        userService.updateUserRole(user);
    }

    @ApiOperation(value = "更新用户状态")
    @PostMapping("/special/update_status")
    public void updateStatus(@RequestBody User user) {
        userService.updateUser(user);
    }

    @ApiIgnore
    @PostMapping("/special/ws/member/add")
    public void addMemberByAdmin(@RequestBody AddMemberRequest request) {
        userService.addMember(request);
    }

    @ApiIgnore
    @GetMapping("/special/ws/member/delete/{workspaceId}/{userId}")
    public void deleteMemberByAdmin(@PathVariable String workspaceId, @PathVariable String userId) {
        userService.deleteMember(workspaceId, userId);
    }

    @ApiIgnore
    @PostMapping("/special/org/member/add")
    public void addOrganizationMemberByAdmin(@RequestBody AddOrgMemberRequest request) {
        userService.addOrganizationMember(request);
    }

    @ApiIgnore
    @GetMapping("/special/org/member/delete/{organizationId}/{userId}")
    public void delOrganizationMemberByAdmin(@PathVariable String organizationId, @PathVariable String userId) {
        userService.delOrganizationMember(organizationId, userId);
    }

    @I18n
    @ApiOperation(value = "所有用户")
    @GetMapping("/list/all")
    public List<User> getUserList() {
        return userService.getUserList();
    }

    @ApiIgnore
    @PostMapping("/update/current")
    public UserDTO updateCurrentUser(@RequestBody User user) throws Exception {
        userService.updateUser(user);
        return userService.getUserDTO(user.getId());
    }

    @I18n
    @ApiOperation(value = "用户信息")
    @GetMapping("/info/{userId}")
    public UserDTO getUserInfo(@PathVariable(value = "userId") String userId) throws Exception {
        return userService.getUserInfo(userId);
    }

    /**
     * 修改当前用户密码
     */
    @ApiOperation(value = "修改用户密码")
    @PostMapping("/update/password")
    public int updateCurrentUserPassword(@RequestBody EditPassWordRequest request) throws Exception {
        return userService.updateCurrentUserPassword(request);
    }

    /**
     * 管理员修改用户密码
     */
    @ApiIgnore
    @PostMapping("/special/password")
    public int updateUserPassword(@RequestBody EditPassWordRequest request) throws Exception {
        return userService.updateUserPassword(request);
    }

    @I18n
    @ApiIgnore
    @GetMapping("/search/{condition}")
    public List<User> searchUser(@PathVariable String condition) {
        return userService.searchUser(condition);
    }

    /*Get default language*/
    @GetMapping(value = "/language")
    public String getDefaultLanguage() {
        return userService.getDefaultLanguage();
    }

}