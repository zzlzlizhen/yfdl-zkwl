package com.remote.modules.project.controller;

import com.github.pagehelper.PageInfo;
import com.remote.common.utils.ProjectCodeUitls;
import com.remote.common.utils.R;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.entity.DeviceQuery;
import com.remote.modules.device.service.DeviceService;
import com.remote.modules.project.entity.ProjectEntity;
import com.remote.modules.project.entity.ProjectQuery;
import com.remote.modules.project.service.ProjectService;
import com.remote.modules.sys.controller.AbstractController;
import com.remote.modules.sys.entity.SysUserEntity;
import com.remote.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author zhangwenping
 * @Date 2019/5/31 14:12
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fun/project")
public class ProjectController  extends AbstractController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DeviceService deviceService;


    @RequestMapping(value = "/add", method= RequestMethod.POST)
    public R add(@RequestBody ProjectEntity project) {
        if(project.getExclusiveUser() == null){
            return R.error(201,"使用人不能为空");
        }
        if(StringUtils.isEmpty(project.getProjectName())){
            return R.error(201,"项目名称不能为空");
        }
        SysUserEntity user = getUser();
        project.setProjectId(UUID.randomUUID().toString());
        project.setCreateUser(user.getUserId());
        project.setCreateName(user.getRealName());
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

    @RequestMapping(value = "/queryProject", method= RequestMethod.POST)
    public R queryProject(@RequestParam(required = false,defaultValue="10") Integer pageSize,
                          @RequestParam(required = false,defaultValue="1") Integer pageNum,
                          @RequestParam(required = false,defaultValue="") String projectName,
                          @RequestParam(required = false,defaultValue="") String projectCode,
                          @RequestParam(required = false,defaultValue="") String userName){
        ProjectQuery projectQuery = new  ProjectQuery(pageSize,pageNum,projectName,projectCode,userName);
        SysUserEntity user = getUser();
        projectQuery.setUserId(user.getUserId());
        PageInfo<ProjectEntity> proPage = projectService.queryProjectByUserIds(projectQuery);
        if(proPage != null){
            return R.ok(proPage);
        }
        return R.error(400,"查询项目失败");
    }

    @RequestMapping(value = "/delete", method= RequestMethod.GET)
    public R delete(String projectIds){
        SysUserEntity user = getUser();
        if(StringUtils.isNotEmpty(projectIds)){
            List<String> projectList = Arrays.asList(projectIds.split(","));
            DeviceQuery deviceQuery = new DeviceQuery();
            deviceQuery.setProjectId(projectList.get(0));
            List<Long> exclUserIds = projectService.queryExclusiveIds(projectList);

            List<DeviceEntity> deviceList = deviceService.queryDeviceNoPage(deviceQuery);
            if(CollectionUtils.isNotEmpty(deviceList)){
                return R.error(201,"当前项目下有未删除设备，删除失败");
            }
            boolean flag = projectService.delProject(projectList,user.getUserId());
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
    public R update(@RequestBody ProjectEntity projectEntity) throws Exception {
        if(projectEntity.getExclusiveUser() == null){
            return R.error(201,"使用人不能为空");
        }
        if(StringUtils.isEmpty(projectEntity.getProjectName())){
            return R.error(201,"项目名称不能为空");
        }
        SysUserEntity user = getUser();
        projectEntity.setUpdateTime(new Date());
        projectEntity.setUpdateUser(user.getUserId());
        Long excelUserId = projectService.queryExclusiveId(projectEntity.getProjectId());
        projectEntity.setCreateUser(Long.valueOf(projectEntity.getExclusiveUser()));
        boolean flag = projectService.updateProject(projectEntity);
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

    @RequestMapping(value = "/queryProjectMap", method= RequestMethod.GET)
    public R queryProjectMap(String projectId){
        ProjectEntity projectEntity = projectService.queryProjectMap(projectId);
        if(projectEntity != null){
            return R.ok(projectEntity);
        }
        return R.error(400,"查询项目失败");
    }

    @RequestMapping(value = "/queryProjectNoPage", method= RequestMethod.GET)
    public R queryProjectNoPage(Integer deviceStatus){
        SysUserEntity user = getUser();
        List<ProjectEntity> list = projectService.queryProjectNoPage(user.getUserId(),deviceStatus);
        if(list != null){
            return R.ok(list);
        }
        return R.error(400,"查询项目失败");
    }

    @RequestMapping(value = "/queryProjectById", method= RequestMethod.GET)
    public R queryProjectNoPage(String projectId,String groupId){
        return R.ok(projectService.queryProjectById(projectId,groupId));
    }
}
