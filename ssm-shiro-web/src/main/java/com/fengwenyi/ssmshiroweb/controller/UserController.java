package com.fengwenyi.ssmshiroweb.controller;

import com.fengwenyi.ssmshiroweb.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Wenyi Feng
 * @since 2018-10-16
 */
@Controller
public class UserController {

    @RequestMapping(value = "/subLogin", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String subLogin(User user) {

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),
                user.getPassword());

        try {
            if (user.getRememberMe() != null)
                token.setRememberMe(user.getRememberMe());
            subject.login(token);
        } catch (AuthenticationException e) {
            return e.getMessage();
        }

        if (subject.hasRole("admin")) {
            return "有admin权限";
        }

//        return "登录成功";
        return "无admin权限";
    }

    /*@RequiresRoles("admin")
    @RequestMapping("/testRole")
    @ResponseBody
    public String testRole() {
        return "testRole success";
    }*/

    /*@RequiresRoles("admin1")
//    @RequiresPermissions("admin1") // 也有
    @RequestMapping("/testRole1")
    @ResponseBody
    public String testRole1() {
        return "testRole1 success";
    }*/

    @RequestMapping("/testRole")
    @ResponseBody
    public String testRole() {
        return "testRole success";
    }

    @RequestMapping("/testRole1")
    @ResponseBody
    public String testRole1() {
        return "testRole1 success";
    }

    @RequestMapping("/testPerms")
    @ResponseBody
    public String testPerms() {
        return "testPerms success";
    }

    @RequestMapping("/testPerms1")
    @ResponseBody
    public String testPerms1() {
        return "testPerms1 success";
    }

}
