package com.fengwenyi.ssmshiroweb.filter;

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
