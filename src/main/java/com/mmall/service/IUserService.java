package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

import java.util.List;

/**
 * @Author: YJ
 * @Date: 2019/3/24 1:21
 * @Ver: 1.0
 */
public interface IUserService {
    ServerResponse<User> login(String userid, String psw);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str,String type);

    ServerResponse<List<User>> checkAllUser();
}
