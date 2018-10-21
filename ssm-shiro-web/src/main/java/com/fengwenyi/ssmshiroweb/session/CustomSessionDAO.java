package com.fengwenyi.ssmshiroweb.session;

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
