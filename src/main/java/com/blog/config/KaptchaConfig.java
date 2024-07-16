package com.blog.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha captchaProducer() {
        Properties properties = new Properties();
        // 配置验证码图片的宽度和高度
        properties.setProperty("kaptcha.image.width", "200");
        properties.setProperty("kaptcha.image.height", "100");
        // 配置生成的验证码文本长度
        properties.setProperty("kaptcha.textproducer.char.length", "6");
        // 更多配置可以根据需求进行设置

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}