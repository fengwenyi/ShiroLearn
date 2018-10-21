package com.fengwenyi.shirotest;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

/**
 * @author Wenyi Feng
 * @since 2018-10-15
 */
public class MyTest {

    @Test
    public void md5() {

//        Md5Hash md5Hash = new Md5Hash("123456");
        Md5Hash md5Hash = new Md5Hash("123456", "admin");
        System.out.println(md5Hash.toString());
    }

}
