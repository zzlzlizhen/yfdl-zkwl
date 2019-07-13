package com.remote.account;

import cn.hutool.http.HttpRequest;
import com.remote.sys.entity.SysUserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    //http://localhost:8020/remote-wxapi/test/addUser?username=1111&password=1234445
    @RequestMapping(value = "/addUser")
    public String addUser(@RequestParam("username")String username, @RequestParam("password")String password){
        System.out.println("username is:"+username);
        System.out.println("password is:"+password);
        return "aaa";
    }
    //http://localhost:8020/remote-wxapi/test/addUser2?username=1111&password=1234445
    @RequestMapping("/addUser2")
    public String addUser2(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("username:"+username);
        System.out.println("password:"+password);
        return "bbb";
    }
    //http://localhost:8020/remote-wxapi/test/addUser3?username=1111&password=1234445
    @RequestMapping(value = "/addUser3")
    public String addUser3(UserMode sysUserEntity){
        System.out.println("username is:"+sysUserEntity.getUsername());
        System.out.println("password is:"+sysUserEntity.getPassword());
        return "ccc";
    }
    //http://localhost:8020/remote-wxapi/test/addUser4/1111/1234445
    @RequestMapping(value = "/addUser4/{username}/{password}",method = RequestMethod.GET)
    public String addUser4(@PathVariable String username,@PathVariable String password){
        System.out.println("username is:"+username);
        System.out.println("password is:"+password);
        return "ddd";
    }
}
