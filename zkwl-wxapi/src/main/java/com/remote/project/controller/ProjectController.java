package com.remote.project.controller;

import com.github.pagehelper.PageInfo;
import com.remote.common.utils.ProjectCodeUitls;
import com.remote.common.utils.R;
import com.remote.device.entity.DeviceEntity;
import com.remote.device.entity.DeviceQuery;
import com.remote.device.service.DeviceService;
import com.remote.project.entity.ProjectEntity;
import com.remote.project.entity.ProjectQuery;
import com.remote.project.service.ProjectService;
import com.remote.sys.controller.AbstractController;
import com.remote.sys.entity.SysUserEntity;
import com.remote.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author zhangwenping
 * @Date 2019/5/31 14:12
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fun/project")
public class ProjectController extends AbstractController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private SysUserService sysUserService;



    @RequestMapping(value = "/add", method= RequestMethod.POST)
    public R add(@RequestBody ProjectEntity project){
        if(project.getExclusiveUser() == null){
            return R.error(201,"使用人不能为空");
        }
        if(StringUtils.isEmpty(project.getProjectName())){
            return R.error(201,"项目名称不能为空");
        }
        //前端传入登陆人id和姓名
        project.setProjectId(UUID.randomUUID().toString());
        project.setCreateTime(new Date());
        project.setProjectCode(ProjectCodeUitls.getProjectCode());
        boolean flag = projectService.addProject(project);
        if(!flag){
            return R.error(400,"添加项目失败");
        }else{
            sysUserService.updateProCount(project.getExclusiveUser(),1);
        }
        return R.ok();
    }

    @RequestMapping(value = "/queryProject", method= RequestMethod.GET)
    public R queryProject(@RequestParam(required = false,defaultValue="10") Integer pageSize,
                          @RequestParam(required = false,defaultValue="1") Integer pageNum,
                          @RequestParam(required = false,defaultValue="") String projectName,
                          @RequestParam(required = false,defaultValue="") long userId,
                          @RequestParam(required = false,defaultValue="") String parentId){
        ProjectQuery projectQuery = new  ProjectQuery(pageSize,pageNum,projectName);
        projectQuery.setUserId(userId);
        projectQuery.setParentId(parentId);
        PageInfo<ProjectEntity> proPage = projectService.queryProjectByUserIds(projectQuery);
        if(proPage != null){
            return R.ok(proPage);
        }
        return R.error(400,"查询项目失败");
    }

    @RequestMapping(value = "/delete", method= RequestMethod.GET)
    public R delete(String projectIds,Long userId){
        DeviceQuery deviceQuery = new DeviceQuery();
        if(StringUtils.isNotEmpty(projectIds)){
            List<String> projectList = Arrays.asList(projectIds.split(","));
            deviceQuery.setProjectId(projectList.get(0));
            List<DeviceEntity> deviceList = deviceService.queryDeviceNoPage(deviceQuery);
            List<Long> exclUserIds = projectService.queryExclusiveIds(projectList);
            if(CollectionUtils.isNotEmpty(deviceList)){
                return R.error(201,"当前项目下有未删除设备，删除失败");
            }
            boolean flag = projectService.delProject(projectList,userId);
            if(!flag){
                return R.error(400,"删除项目失败");
            }else{
                if(CollectionUtils.isNotEmpty(exclUserIds)||exclUserIds.size()>0){
                    for(Long exclUserId:exclUserIds){
                        sysUserService.updateProCount(exclUserId,-1);
                    }
                }
            }
        }
        return R.ok();
    }


    @RequestMapping(value = "/update", method= RequestMethod.POST)
    public R update(@RequestBody ProjectEntity projectEntity){
        projectEntity.setUpdateTime(new Date());
        //前端传入修改人id
        boolean flag = projectService.updateProject(projectEntity);
        Long excelUserId = projectService.queryExclusiveId(projectEntity.getProjectId());
        int count = 0;
        if(projectEntity.getExclusiveUser() != excelUserId){
            count = 1;
            sysUserService.updateProCount(excelUserId,-1);
        }
        if(!flag){
            return R.error(400,"修改项目失败");
        }else{
            sysUserService.updateProCount(projectEntity.getExclusiveUser(),count);
        }
        return R.ok();
    }
}
