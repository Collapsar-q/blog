package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.mapper.UserMapper;
import com.blog.model.Article;
import com.blog.model.User;
import com.blog.service.ArticleService;
import com.blog.mapper.ArticleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author Administrator
* @description 针对表【article】的数据库操作Service实现
* @createDate 2024-07-16 16:43:04
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{
@Resource
private UserMapper userMapper;
@Resource
private ArticleMapper articleMapper;
    @Override
    public Page<Article> queryArticle(String username, Long size, Long no,Long flag) {
        QueryWrapper<User> queryWrapperU = new QueryWrapper<>();
        Page<Article> articles = new Page<>();
        queryWrapperU.eq("username",username);
        User user = userMapper.selectOne(queryWrapperU);
        if (user==null){
            return articles;
        }
        Long userId = user.getUserId();
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        //按创建时间排序
        if (flag==1){
            queryWrapper.orderByAsc("created");
        }else {
            queryWrapper.orderByDesc("created");
        }
        Page<Article> page = new Page<>(no, size);
        return articleMapper.selectPage(page, queryWrapper);
    }
}




