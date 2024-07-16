package com.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.RespResult;
import com.blog.mapper.ArticleMapper;

import com.blog.model.Article;
import com.blog.model.User;
import com.blog.service.ArticleService;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/posts")
public class PostController {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleService articleService;
    @PostMapping("/add")
    public RespResult addArticle(@RequestHeader("uid") Long uid, @RequestBody Article article){
        article.setUserId(uid);
        Date date = new Date();
        article.setCreated(date);
        article.setLastModified(date);
        int insert = articleMapper.insert(article);
        if (insert<1){
            return RespResult.error(300,"添加失败");
        }
        return RespResult.ok(article);
    }
    @GetMapping("/getArticlePage")
    public RespResult getArticlePage(@RequestParam("username") String username, @RequestParam("pageSize")Long pageSize,@RequestParam("pageNo")Long pageNo,@RequestParam("flag") Long flag){
        Page<Article> articlePage = articleService.queryArticle(username, pageSize, pageNo,flag);
        return RespResult.ok(articlePage);
    }
    @GetMapping("/{id}")
    public RespResult getArticleById(@PathVariable("id") Long id){
        Article article = articleMapper.selectById(id);
        if (article==null){
            return RespResult.error();
        }
        return RespResult.ok(article);
    }
    @PutMapping("/update")
    public RespResult updateArticle(@RequestHeader("uid")Long uid ,@RequestBody Article article){
        if (article==null||article.getPostId()==null){
            return RespResult.error("文章不存在");
        }
        Article article1 = articleMapper.selectById(article.getPostId());
        if (!article1.getUserId().equals(uid)){
            return RespResult.error("没有权限修改");
        }
        article.setLastModified(new Date());
        int i = articleMapper.updateById(article);
        if (i<1){
            return RespResult.error();
        }
        return RespResult.ok("修改成功");
    }
    @DeleteMapping("/delete/{id}")
    public RespResult deleteArticle(@RequestHeader("uid")Long uid,@PathVariable("id") Long id){
        Article article = articleMapper.selectById(id);
        if (article==null||article.getUserId()==null||!article.getUserId().equals(uid)){
            return RespResult.error("没有权限删除");
        }
        int i = articleMapper.deleteById(id);
        if (i<1){
            return RespResult.error();
        }
        return RespResult.ok("删除成功");
    }
}
