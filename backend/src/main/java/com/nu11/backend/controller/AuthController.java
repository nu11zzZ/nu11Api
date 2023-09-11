package com.nu11.backend.controller;

import com.nu11.backend.dto.LoginInfoDTO;
import com.nu11.backend.dto.RegistryInfoDTO;
import com.nu11.backend.service.UserService;
import com.nu11.common.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Resource
    UserService userService;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @PostMapping("/login")
    public Response<String> login(@RequestBody @Valid LoginInfoDTO loginInfo){
        String access_token = userService.verifyInfo(loginInfo);
        if(access_token == null)
            return Response.error();
        redisTemplate.opsForValue().set(access_token,1,30, TimeUnit.MINUTES);
        return Response.success(access_token);
    }

    @PostMapping("/registry")
    public Response<Void> registry(@RequestBody @Valid RegistryInfoDTO registryInfo){
        String captcha = (String) redisTemplate.opsForValue().get(registryInfo.getPhone());
        if(captcha != null){
            if(captcha.equals(registryInfo.getCaptcha())){
                userService.addUser(registryInfo);
                redisTemplate.delete(registryInfo.getPhone());
                return Response.success();
            }
        }
        return Response.error();
    }

    @GetMapping("/logout")
    public Response<Void> logout(@RequestHeader String access_token){
        if(redisTemplate.opsForValue().get(access_token) != null){
            redisTemplate.delete(access_token);
        }
        return Response.success();
    }
}
