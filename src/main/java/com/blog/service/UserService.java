package com.blog.service;

import com.blog.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service
* @createDate 2024-07-16 16:22:49
*/
public interface UserService extends IService<User> {
    User QueryByUserName(String username);
    Boolean userRegister(String username,String password,String email);
    User userLogin(String username,String password);
}
