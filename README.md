
# Shiro学习实现权限验证

## 写在前面的话

提及权限，就会想到安全，是一个十分棘手的话题。这里只是作为学校Shiro的一个记录，而不是，权限就应该这样设计之类的。

## Shiro框架

1、Shiro是基于Apache开源的强大灵活的开源安全框架。

2、Shiro提供了 **`认证`**，**`授权`**，**`企业会话管理`**、**`安全加密`**、**`缓存管理`**。

3、Shiro与Security对比

![Shiro与Security对比](https://upload-images.jianshu.io/upload_images/5805596-ad699e465b3cdff3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

4、Shiro整体架构

![Shiro整体架构图](https://upload-images.jianshu.io/upload_images/5805596-72207305c55bbb20.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

5、特性

![Shiro特性](https://upload-images.jianshu.io/upload_images/5805596-d2c7aef10be1595a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

6、认证流程

![Shiro认证流程](https://upload-images.jianshu.io/upload_images/5805596-4fcfa853abfb992b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 认证

当我们理解Shiro之后，我们就能比较容易梳理出认证流程，大概就是下面这样子。

![Shiro认证流程](https://upload-images.jianshu.io/upload_images/5805596-53353cdb4737bc8c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们来看一段测试代码：

```java
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
```

我们发现Shiro真正帮我们做的就是认证这一步，那他到底是如何去认证的呢？

![Shiro登录过程](https://upload-images.jianshu.io/upload_images/5805596-1e55b02fd89ff18a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们把我们登录的代码完善一下：

```java
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
```

## Realm

1、IniReam

配置文件 `user.ini`

```ini
[users]
Mark=admin,admin
[roles]
admin=user:add,user:delete,user:update,user:select
```

测试代码

```java
// IniRealm 测试
@Test
public void testAuthenticationIniRealm () {

    IniRealm iniRealm = new IniRealm("classpath:user.ini");

    DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
    defaultSecurityManager.setRealm(iniRealm);

    SecurityUtils.setSecurityManager(defaultSecurityManager);
    Subject subject = SecurityUtils.getSubject();

    UsernamePasswordToken token = new UsernamePasswordToken("admin", "123456");
    subject.login(token);

    System.out.println("是否认证：" + subject.isAuthenticated());

    subject.checkRole("admin");
    subject.checkPermission("user:delete");

}
```

2、JdbcRealm

这里有两种方案，使用Shiro为我们提供了SQL语句，或者我们自己写SQL语句。

第一种：

```java
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
```

第二种：

```java
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
```

3、自定义Realm

在上面我们已经看过了 `JdbcRealm`，所以我们也可以依葫芦画瓢自定义Ramlm。

第一步：继承 `AuthorizingRealm`

第二步：实现认证方法

第三步：实现授权方法

通过AuthorizingRealm，我们完全按照自己的需求实现自己的业务逻辑。

```java
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
```

可能Shiro提供的Realm并不能满足我们的实际开发需求，所以真正弄明白自定义Realm还是有很大帮助的，你觉得呢？

4、安全加密

明文密码？

好吧，我们看看Shiro为我们提供的加密方法。

```java
//  Md5Hash md5Hash = new Md5Hash("123456");

Md5Hash md5Hash = new Md5Hash("123456", "admin");
System.out.println(md5Hash.toString());
```

那我们该怎么使用了？

在Realm认证中设置盐值

```java
// 使用admin对密码进行加密
authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("admin"));
```

我们只需要告诉我们的Realm，需要对密码进行加密就可以了。

```java
CustomRealm customRealm = new CustomRealm();
HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
// 加密类型
matcher.setHashAlgorithmName("md5"); 
// 加密次数
matcher.setHashIterations(1); 
customRealm.setCredentialsMatcher(matcher);
```

## 权限

1、Shiro为我们提供的权限

| 名称 | 说明 |
| --- | --- |
| anon | 不校验 |
| authc | 作校验 |
| roles | 需要具有指定角色（一个或多个）才能访问 |
| perms | 需要具有指定角色（一个或多个）才能访问 |

2、配置

```xml
<!-- 从上往下开始匹配 -->
/login.html = anon
/subLogin = anon
<!--/testRole = roles["admin"]-->
<!--/testRole1 = roles["admin", "admin1"]-->
<!--/testPerms = perms["user:delete"]-->
<!--/testPerms1 = perms["user:delete", "user:update"]-->
```

3、自定义权限认证

```
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author Wenyi Feng
 * @since 2018-10-22
 */
public class CustomAuthorizationFilter extends AuthorizationFilter {
    
    protected boolean isAccessAllowed(ServletRequest request, 
                                      ServletResponse response, 
                                      Object mappedValue) throws Exception {
        return false;
    }
}
```

另外，看一下 Shiro Filter

![Shiro Filter](https://upload-images.jianshu.io/upload_images/5805596-8088098c0af79cc1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 会话管理（Session）

会话管理，就是拿到Session之后，我们怎么处理。什么意思？分布式系统，需要共享Session。

```java
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import java.io.Serializable;
import java.util.Collection;

/**
 * 自定义Session操作接口
 * @author Wenyi Feng
 * @since 2018-10-22
 */
public class CustomSessionDAO extends AbstractSessionDAO {
    
    // 创建Session
    protected Serializable doCreate(Session session) {
        return null;
    }

    // 读session
    protected Session doReadSession(Serializable sessionId) {
        return null;
    }

    // 修改session
    public void update(Session session) throws UnknownSessionException {

    }

    // 删除session
    public void delete(Session session) {

    }

    // 获取当前活动的session
    public Collection<Session> getActiveSessions() {
        return null;
    }
}
```

## 缓存管理（Cache）

缓存管理同Session管理，可以这样说，session是一套系统的基础，缓存决定系统的优化级别，很重要。

![缓存设计](https://upload-images.jianshu.io/upload_images/5805596-a80d0b57c59cc99f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

另外，我们看一下，Shiro为我们设计的缓存接口。

```java
package org.apache.shiro.cache;

import java.util.Collection;
import java.util.Set;

public interface Cache<K, V> {
    V get(K var1) throws CacheException;

    V put(K var1, V var2) throws CacheException;

    V remove(K var1) throws CacheException;

    void clear() throws CacheException;

    int size();

    Set<K> keys();

    Collection<V> values();
}
```

## 自动登录

我们只需要为token设置RememberMe就可以了。

```java
token.setRememberMe(user.getRememberMe());
```

## 链接

[1] [Shiro安全框架入门](https://www.imooc.com/learn/977)
