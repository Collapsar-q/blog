package com.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.model.Article;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【article】的数据库操作Service
* @createDate 2024-07-16 16:43:04
*/
public interface ArticleService extends IService<Article> {
    Page<Article> queryArticle(String username, Long size, Long no,Long flag);
}
