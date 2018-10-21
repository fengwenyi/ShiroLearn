package com.fengwenyi.ssmshiroweb.dao;

import com.fengwenyi.ssmshiroweb.vo.User;

import java.util.List;

/**
 * @author Wenyi Feng
 * @since 2018-10-16
 */
public interface UserDao {

    User findUserByUserName(String username);

    List<String> findRolesByUsername(String username);
}
