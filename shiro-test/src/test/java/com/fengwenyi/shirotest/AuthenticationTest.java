package com.fengwenyi.shirotest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Wenyi Feng
 * @since 2018-10-15
 */
public class AuthenticationTest {

    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser() {
        //simpleAccountRealm.addAccount("admin", "admin");
        simpleAccountRealm.addAccount("admin", "admin", "admin");
    }

    // 认证测试
    @Test
    public void testAuthentication() {

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        // 2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        // 3.认证
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "admin");
        subject.login(token);
        System.out.println("是否认证：" + subject.isAuthenticated());

        // 4.退出
        subject.logout();
        System.out.println("是否认证：" + subject.isAuthenticated());
    }

    /**
     * 登录操作，返回登录认证信息
     * @return 登录结果
     */
    public String login() {
        try {
            DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
            defaultSecurityManager.setRealm(simpleAccountRealm);

            SecurityUtils.setSecurityManager(defaultSecurityManager);
            Subject subject = SecurityUtils.getSubject();

            UsernamePasswordToken token = new UsernamePasswordToken("admin", "admin");

            subject.login(token);
            if (subject.isAuthenticated())
                return  "登录成功";
        } catch (IncorrectCredentialsException e1) {
            e1.printStackTrace();
            return "密码错误";
        } catch (LockedAccountException e2) {
            e2.printStackTrace();
            return "登录失败，该用户已被冻结";
        } catch (AuthenticationException e3) {
            e3.printStackTrace();
            return "该用户不存在";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "登录失败";
    }

    // 授权测试
    @Test
    public void testAuthentication2() {

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        // 2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("Mark", "123456");
        subject.login(token);

        System.out.println("是否认证：" + subject.isAuthenticated());

        // 检查角色
        subject.checkRole("admin");

        // 检查多个权限（是否具有多个权限）
        subject.checkRoles("admin", "user");
    }

}
