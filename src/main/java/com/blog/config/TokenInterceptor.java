package com.blog.config;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.blog.common.RespResult;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class TokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;
    private String secret;

    public TokenInterceptor(String secret,StringRedisTemplate stringRedisTemplate) {
        this.secret = secret;
        this.stringRedisTemplate=stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())){
            return true;
        }
        boolean isSend=false;
        try {
            String headerUid = request.getHeader("uid");
            String token = request.getHeader("Token");
            if (StringUtils.isNotBlank(token)) {
                String s = stringRedisTemplate.opsForValue().get("TOKEN:CODE:" + headerUid);
                if (StringUtils.isNotBlank(s)&&s.equals(token)){
                    isSend=true;
                    //如果时间少于30分钟重置过期时间
                    Long expire = stringRedisTemplate.getExpire("TOKEN:CODE:" + headerUid, TimeUnit.SECONDS);
                    if (expire!=null&&stringRedisTemplate.getExpire("TOKEN:CODE:" + headerUid, TimeUnit.SECONDS)<  60 * 30){
                        stringRedisTemplate.expire("TOKEN:CODE:" + headerUid,7200 , TimeUnit.SECONDS);
                    }
                }
            }

        }catch (Exception e){
            isSend=false;
            e.printStackTrace();
        }
        if (isSend==false){
            RespResult error = RespResult.error(333,"token失效");
            String jsonString = JSONObject.toJSONString(error);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print(jsonString);
            out.flush();
            out.close();
        }
        return isSend;
    }
}