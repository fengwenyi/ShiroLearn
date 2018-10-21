package com.fengwenyi.shirotest;

import com.fengwenyi.shirotest.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author Wenyi Feng
 * @since 2018-10-15
 */
public class CustomRealmTest {

    // 自定义Realm认证测试
    @Test
    public void testAuthentication() {

        CustomRealm customRealm = new CustomRealm();

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        // 2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("admin", "admin");
        subject.login(token);

        System.out.println("是否认证：" + subject.isAuthenticated());

        subject.checkRole("admin");
//        subject.checkPermission("user:select");
        subject.checkPermissions("user:select", "user:add");

    }

    // 加密
    @Test
    public void testAuthentication2() {

        CustomRealm customRealm = new CustomRealm();
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        // 加密类型
        matcher.setHashAlgorithmName("md5");
        // 加密次数
        matcher.setHashIterations(1);
        customRealm.setCredentialsMatcher(matcher);

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        // 2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("admin", "123456");
        subject.login(token);

        System.out.println("是否认证：" + subject.isAuthenticated());

        subject.checkRole("admin");
//        subject.checkPermission("user:select");
        subject.checkPermissions("user:select", "user:add");

    }

}
