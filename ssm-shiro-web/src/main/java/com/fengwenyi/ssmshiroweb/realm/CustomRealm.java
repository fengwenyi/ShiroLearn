package com.fengwenyi.ssmshiroweb.realm;

import com.fengwenyi.ssmshiroweb.dao.UserDao;
import com.fengwenyi.ssmshiroweb.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Wenyi Feng
 * @since 2018-10-15
 */
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDao;

    // 授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        // 获取角色
        Set<String> roles = getRolesByUsername(username);
        Set<String> permissions = getPermissionsByUsername(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

    private Set<String> getPermissionsByUsername(String username) {
        Set<String> permissionMap = new HashSet<String>();
//        permissionMap.add("user:select");
//        permissionMap.add("user:add");
//        permissionMap.add("user:update");
        permissionMap.add("user:delete");
        return permissionMap;
    }

    private Set<String> getRolesByUsername(String username) {
        List<String> list = userDao.findRolesByUsername(username);
        Set<String> roleMap = new HashSet<String>(list);
        return roleMap;
    }

    // 认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        // 从主体传过来的认证信息中，获取用户名
        String username = (String) token.getPrincipal();

        // 通过用户名到数据库中获取凭证
        String password = getPasswordByUsername(username);

//        System.out.println(password);

        if (password == null)
            return null;

        SimpleAuthenticationInfo authenticationInfo =
                new SimpleAuthenticationInfo(username, password, getName());

        // 使用admin对密码进行加密
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("admin"));

        return authenticationInfo;
    }

    private String getPasswordByUsername(String username) {
        User user = userDao.findUserByUserName(username);
        if (user != null)
            return user.getPassword();
        return null;
    }
}
