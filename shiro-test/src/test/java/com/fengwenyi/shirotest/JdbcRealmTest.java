package com.fengwenyi.shirotest;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author Wenyi Feng
 * @since 2018-10-15
 */
public class JdbcRealmTest {

    DruidDataSource druidDataSource = new DruidDataSource();

    {
        druidDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/shiro-test");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("kU#m5eHY5iTiQj#q");
    }

    // JdbcRealm 测试 Shiro SQL
    @Test
    public void testAuthenticationShiroSQL() {

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(druidDataSource);
        jdbcRealm.setPermissionsLookupEnabled(true);

        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("xfsy", "xfsy2018");
        subject.login(token);

        System.out.println("是否认证：" + subject.isAuthenticated());

        subject.checkRole("user");
        subject.checkPermission("user:select");

    }

    // JdbcRealm 测试 Custom SQL
    @Test
    public void testAuthenticationCustomSQL() {

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(druidDataSource);
        jdbcRealm.setPermissionsLookupEnabled(true);

        /**
         * @see org.apache.shiro.realm.jdbc.JdbcRealm
         */
        String sql = "select pwd from t_user where user_name = ?";
        jdbcRealm.setAuthenticationQuery(sql);

        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("test", "123");
        subject.login(token);

        System.out.println("是否认证：" + subject.isAuthenticated());
    }

}
