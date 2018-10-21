package com.fengwenyi.shirotest.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author Wenyi Feng
 * @since 2018-10-22
 */
public class CustomRealmTest extends AuthorizingRealm {

    /**
     * Retrieves the AuthorizationInfo for the given principals from the underlying data store.  When returning
     * an instance from this method, you might want to consider using an instance of
     * {@link org.apache.shiro.authz.SimpleAuthorizationInfo SimpleAuthorizationInfo}, as it is suitable in most cases.
     *
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return the AuthorizationInfo associated with this principals.
     * @see org.apache.shiro.authz.SimpleAuthorizationInfo
     */
    // 授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * Returns {@code true} if authentication caching should be utilized based on the specified
     * {@link AuthenticationToken} and/or {@link AuthenticationInfo}, {@code false} otherwise.
     * <p/>
     * The default implementation simply delegates to {@link #isAuthenticationCachingEnabled()}, the general-case
     * authentication caching setting.  Subclasses can override this to turn on or off caching at runtime
     * based on the specific submitted runtime values.
     *
     * @param token the submitted authentication token
     * @param info  the {@code AuthenticationInfo} acquired from data source lookup via
     *              {@link #doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)}
     * @return {@code true} if authentication caching should be utilized based on the specified
     *         {@link AuthenticationToken} and/or {@link AuthenticationInfo}, {@code false} otherwise.
     * @since 1.2
     */
    // 认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        return null;
    }
}
