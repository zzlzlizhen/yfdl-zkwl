package com.remote.project.controller;

import com.github.pagehelper.PageInfo;
import com.remote.common.utils.ProjectCodeUitls;
import com.remote.common.utils.R;
import com.remote.project.entity.ProjectEntity;
import com.remote.project.entity.ProjectQuery;
import com.remote.project.service.ProjectService;
import com.remote.sys.controller.AbstractController;
import com.remote.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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




    @RequestMapping(value = "/add", method= RequestMethod.POST)
    public R add(@RequestBody ProjectEntity project){
        SysUserEntity user = getUser();
        project.setProjectId(UUID.randomUUID().toString());
        project.setCreateUser(user.getUserId());
        project.setCreateName(user.getUsername());
        project.setCreateTime(new Date());
        project.setProjectCode(ProjectCodeUitls.getProjectCode());
        boolean flag = projectService.addProject(project);
        if(!flag){
            return R.error(400,"添加项目失败");
        }
        return R.ok();
    }

    @RequestMapping(value = "/queryProject", method= RequestMethod.GET)
    public R queryProject(@RequestParam(required = false,defaultValue="10") Integer pageSize,
                          @RequestParam(required = false,defaultValue="1") Integer pageNum,
                          @RequestParam(required = false,defaultValue="") String projectName){
        ProjectQuery projectQuery = new  ProjectQuery(pageSize,pageNum,projectName);
        SysUserEntity user = getUser();
        projectQuery.setUserId(user.getUserId());
        PageInfo<ProjectEntity> proPage = projectService.queryProjectByUserIds(projectQuery);
        if(proPage != null){
            return R.ok(proPage);
        }
        return R.error(400,"查询项目失败");
    }


}
