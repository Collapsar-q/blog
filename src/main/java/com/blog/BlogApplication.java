package com.blog;

import com.blog.utils.JwtUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.blog.mapper")
public class BlogApplication {
    @Value("${jwt.secret}")
    private String secretKey;
    @Bean
    public JwtUtil jwtUtil(){
        JwtUtil jwtUtil =new JwtUtil(secretKey);
        return jwtUtil;
    }
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

}
