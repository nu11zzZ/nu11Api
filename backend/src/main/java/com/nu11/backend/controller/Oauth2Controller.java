package com.nu11.backend.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.nu11.backend.dto.Oauth2InfoDTO;
import com.nu11.backend.entity.UserEntity;

import com.nu11.backend.service.UserService;
import com.nu11.common.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/oauth2")
public class Oauth2Controller {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    @Autowired
    UserService userService;
    @GetMapping("/login")
    public String login(@RequestParam("code")String code) throws Exception {
        //https://gitee.com/oauth/token?grant_type=authorization_code&code={code}&client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}
        Map<String,String> head = new HashMap<>();
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        head.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        bodys.put("client_secret","54617d7e782f1cbb1d7452ce62eeade6bca4f9d44f8023f0b53a6ad9c9e9123a");
        querys.put("grant_type","authorization_code");
        querys.put("code",code);
        querys.put("client_id","cd1b876c66d9d07a3b237e980c87f69edbe3dd5091d988566d173ca7966d569c");
        querys.put("redirect_uri","http://localhost:10000/api/backend/oauth2/login");
        HttpResponse response = HttpUtils.doPost("https://gitee.com", "/oauth/token", "POST", head, querys, bodys);
        String gitToken = EntityUtils.toString(response.getEntity());
        JSONObject parseObj = JSONUtil.parseObj(gitToken);
        String token = (String) parseObj.get("access_token");
        //https://gitee.com/api/v5/user?access_token=f6edf472e164b78793363093aee3727a
        querys.clear();
        querys.put("access_token",token);
        HttpResponse response1 = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "GET", head, querys);
        String gitInfo = EntityUtils.toString(response1.getEntity());
        Oauth2InfoDTO bean = JSONUtil.toBean(gitInfo, Oauth2InfoDTO.class);
        UserEntity user = userService.getOne(new LambdaUpdateWrapper<UserEntity>().eq(UserEntity::getOauthId, bean.getId()));
        if(user == null){
            userService.addUser(bean);
        }
        UserEntity userAfterAdd = userService.getOne(new LambdaUpdateWrapper<UserEntity>().eq(UserEntity::getOauthId, bean.getId()));
        Map<String,Object> payload = new HashMap<>();
        payload.put("id",userAfterAdd.getId());
        payload.put("username",userAfterAdd.getUserName());
        payload.put("type",userAfterAdd.getType());
        String access_token = JWTUtil.createToken(payload, UUID.randomUUID().toString().replace("-","").getBytes());
        redisTemplate.opsForValue().set(access_token,1,30, TimeUnit.MINUTES);
        return "redirect:http://localhost:8080/?access_token="+access_token;
    }
}
