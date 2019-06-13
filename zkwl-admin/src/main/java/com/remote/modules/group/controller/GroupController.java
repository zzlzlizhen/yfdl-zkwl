package com.remote.modules.group.controller;

import com.github.pagehelper.PageInfo;
import com.remote.common.utils.R;
import com.remote.modules.group.entity.GroupEntity;
import com.remote.modules.group.entity.GroupQuery;
import com.remote.modules.group.service.GroupService;
import com.remote.modules.sys.controller.AbstractController;
import com.remote.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author zhangwenping
 * @Date 2019/6/4 9:24
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fun/group")
public class GroupController extends AbstractController {

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/add", method= RequestMethod.POST)
    public R add(@RequestBody GroupEntity group){
        SysUserEntity user = getUser();
        group.setGroupId(UUID.randomUUID().toString());
        group.setCreateUser(user.getUserId());
        group.setCreateName(user.getUsername());
        group.setCreateTime(new Date());
        boolean b = groupService.queryByName(group.getProjectId(),group.getGroupName());
        if(b){
            return R.error(400,"分组名称重复");
        }
        boolean flag = groupService.addGroup(group);
        if(!flag){
            return R.error(400,"添加分组失败");
        }
        return R.ok();
    }

    @RequestMapping(value = "/queryGroup", method= RequestMethod.POST)
    public R queryGroup(@RequestBody GroupQuery groupQuery){
        SysUserEntity user = getUser();
        PageInfo<GroupEntity> pageInfo = groupService.queryGroupByName(groupQuery);
        if(pageInfo != null){
            return R.ok(pageInfo);
        }
        return R.error(400,"查询分组失败");
    }

    @RequestMapping(value = "/delete", method= RequestMethod.GET)
    public R deleteGroup(String groupIds,String projectId){
        SysUserEntity user = getUser();
        List<String> groupList = Arrays.asList(groupIds.split(","));
        String msg = groupService.deleteGroup(groupList, projectId);
        if("".equals(msg)){
            return  R.ok();
        }
        return R.error(msg);
    }
    /*
     * @Author chengpunan
     * @Description 在进入项目添加项目前查询
     * @Date 15:07 2019/6/6
     * @Param
     * @return
     **/
    @RequestMapping(value = "/queryGroupNoPage", method= RequestMethod.GET)
    public R queryGroupNoPage(String projectId){
        SysUserEntity user = getUser();
        return R.ok(groupService.queryGroupNoPage(user.getUserId(), user.getUsername(), projectId));
    }

    @RequestMapping(value = "/queryGroupIdNoPage", method= RequestMethod.GET)
    public R queryGroupIdNoPage(String projectId){
        return R.ok(groupService.queryGroupIdNoPage(projectId));
    }


    @RequestMapping(value = "/updateGroup", method= RequestMethod.POST)
    public R updateGroup(@RequestBody GroupEntity groupEntity){
        SysUserEntity user = getUser();
        groupEntity.setUpdateTime(new Date());
        groupEntity.setUpdateUser(user.getUserId());
        boolean flag = groupService.updateGroup(groupEntity);
        if(!flag){
            R.error(400,"修改分组失败");
        }
        return R.ok();
    }
}
