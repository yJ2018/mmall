package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: YJ
 * @Date: 2019/3/24 12:15
 * @Ver: 1.0
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String userid, String psw) {

        int result = userMapper.checkUser(userid);

        if (result == 0){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String md5Psw = MD5Util.MD5EncodeUtf8(psw);
        User user = userMapper.userLogin(userid,md5Psw);

        if (user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登陆成功",user);
    }

    public ServerResponse<String> register(User user){
        ServerResponse serverResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        serverResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        int resultCount = userMapper.checkUser(user.getUsername());
        if (resultCount > 0){
            return ServerResponse.createByErrorMessage("用户名已存在");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        resultCount = userMapper.insert(user);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("注册出错");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> checkValid(String str,String type){
        if(!StringUtils.isBlank(type)){
            if(Const.USERNAME.equals(type)){
                int result = userMapper.checkUser(str);
                if (result > 0){
                    return ServerResponse.createByErrorMessage("用户不存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int result = userMapper.checkEmail(str);
                if (result > 0){
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        }else {
            return ServerResponse.createByErrorMessage("参数错误！");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse<List<User>> checkAllUser(){
        List<User> userList = userMapper.getAllUser();
        return ServerResponse.createBySuccess(userList);
    }
}
