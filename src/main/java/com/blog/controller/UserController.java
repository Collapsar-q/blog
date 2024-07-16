package com.blog.controller;
import com.blog.common.RespResult;
import com.blog.mapper.UserMapper;
import com.blog.model.User;
import com.blog.service.UserService;
import com.blog.utils.CommonUtil;
import com.blog.utils.JwtUtil;
import com.google.code.kaptcha.Producer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
@RestController
@Slf4j
@RequestMapping("/auth")
public class UserController {
    //验证码api
    @Resource
    private Producer captchaProducer;
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtUtil jwtUtil;
    //redis
    @Resource
    protected StringRedisTemplate stringRedisTemplate;
    @GetMapping("/captcha")
    public RespResult getCaptcha() throws IOException {
        UUID uuid = UUID.randomUUID();
        String key=uuid.toString();
        String captchaText = captchaProducer.createText();
        stringRedisTemplate.opsForValue().set("LOGIN:CODE:"+key,captchaText,1, TimeUnit.MINUTES);
        BufferedImage captchaImage = captchaProducer.createImage(captchaText);
        int targetWidth = 150; // 设置目标宽度，根据实际情况调整
        int targetHeight = 50; // 设置目标高度，根据实际情况调整
        BufferedImage resizedImage = resizeImage(captchaImage, targetWidth, targetHeight);
        // 将验证码图片转换成Base64编码的字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        // 返回Base64编码的验证码图片字符串给前端
        return RespResult.ok(key,base64Image);
    }
    @PostMapping("/register")
    public RespResult userRegister(@RequestParam("username") String username,
                                   @RequestParam("password")String password,
                                   @RequestParam("email")String email,
                                   @RequestParam("key")String key,
                                   @RequestParam("code")String code
    ){
        String redisCode = stringRedisTemplate.opsForValue().get("LOGIN:CODE:" + key);
       if (StringUtils.isEmpty(redisCode)||!redisCode.equals(code)){
           return RespResult.error(303,"验证码无效");
       }
        if (!CommonUtil.usernameVerify(username)){
            return RespResult.error(303,"请输入正确的账号格式");
        }
        if (password.length()!=32){
            return RespResult.error(303,"请输入正确的密码格式");
        }
        if (!CommonUtil.emailVerify(email)){
            return RespResult.error(303,"请输入正确的邮箱格式");
        }
        User user = userService.QueryByUserName(username);
        if(user!=null){
            return RespResult.error(303,"该用户名已经被注册过了");
        }
        boolean b = userService.userRegister(username, password,email);
        if (b){
            return RespResult.ok(203,"注册成功",username);
        }else {
            return RespResult.error(303,"注册失败");
        }
    }
    @PostMapping("/login")
    public RespResult userLoginPhone(@RequestParam("username") String username,
                                     @RequestParam("password")String password,
                                     @RequestParam("key") String key,
                                     @RequestParam("code")String code) throws Exception{
        String redisCode = stringRedisTemplate.opsForValue().get("LOGIN:CODE:" + key);
        if (StringUtils.isEmpty(redisCode)||!redisCode.equals(code)){
            return RespResult.error(303,"验证码无效");
        }
        User user = userService.userLogin(username, password);
        if (user==null){
            return RespResult.error(303,"请输入正确的账号或者密码");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("uid",user.getUserId());
        String jwt = jwtUtil.creatJwt(map);
        map.put("username",user.getUsername());
        stringRedisTemplate.opsForValue().set("TOKEN:CODE:"+user.getUserId(), jwt, 7200, TimeUnit.SECONDS);
        return RespResult.ok(205,"登录成功",map,jwt);
    }
    @PostMapping("/me")
    public RespResult userMe(@RequestHeader("uid") Long userId){
        User user = userMapper.selectById(userId);
        return RespResult.ok(user);
    }
    // 辅助方法：将图片压缩到指定的宽度和高度
    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
