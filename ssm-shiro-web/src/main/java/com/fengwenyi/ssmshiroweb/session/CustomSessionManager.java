package com.fengwenyi.ssmshiroweb.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * @author Wenyi Feng
 * @since 2018-10-20
 */
public class CustomSessionManager extends DefaultWebSessionManager {

    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {

        Serializable sessionId = getSessionId(sessionKey);

        ServletRequest servletRequest = null;

        if (sessionKey instanceof WebSessionKey) {
            servletRequest = ((WebSessionKey) sessionKey).getServletRequest();
        }

        if (servletRequest != null && sessionId != null) {
            Session session = (Session) servletRequest.getAttribute(sessionId.toString());
            if (session != null) {
                return session;
            }
        }

        Session session = super.retrieveSession(sessionKey);
        if (servletRequest != null) {
            servletRequest.setAttribute(sessionId.toString(), session);
        }
        return session;
    }
}
