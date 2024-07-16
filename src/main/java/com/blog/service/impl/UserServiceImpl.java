package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.model.User;
import com.blog.service.UserService;
import com.blog.mapper.UserMapper;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-07-16 16:22:49
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
@Resource
private UserMapper userMapper;
    @Value("${blog.config.password-salt")
    private String passwordSalt;
    @Override
    public User QueryByUserName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Boolean userRegister(String username, String password,String email) {
        String md5Hex = DigestUtils.md5Hex(password + passwordSalt);
        User user=new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(md5Hex);
        Date date = new Date();
        user.setCreated(date);
        user.setLastModified(date);
        int insert = userMapper.insert(user);
        return insert>0;
    }

    @Override
    public User userLogin(String username, String password) {
        String md5Hex = DigestUtils.md5Hex(password + passwordSalt);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("password",md5Hex);
        User user = userMapper.selectOne(queryWrapper);
        if (user!=null){
            user.setLastModified(new Date());
        }
        return user;
    }
}




