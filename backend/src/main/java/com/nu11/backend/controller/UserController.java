package com.nu11.backend.controller;



import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import com.nu11.backend.dto.UserInfoDTO;
import com.nu11.backend.entity.UserEntity;
import com.nu11.backend.service.UserService;
import com.nu11.backend.vo.UserVO;

import com.nu11.common.utils.Response;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @GetMapping("/list")
    public Response<Page<UserVO>> getAllUser(@RequestParam Map<String,Object> params){
        Page<UserVO> list = userService.getListUser(params);
        return Response.success(list);
    }

    @GetMapping("/info/{id}")
    public Response<UserVO> getInfoById(@PathVariable Long id){
        UserVO userVO = new UserVO();
        UserEntity userInfo = userService.getById(id);
        BeanUtils.copyProperties(userInfo,userVO);
        return Response.success(userVO);
    }

    @GetMapping("/invoke/{access_key}")
    public Response<UserVO> getInfoByAccessKey(@PathVariable String access_key){
        UserVO userVO = new UserVO();
        UserEntity info = userService.getOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getAccessKey, access_key));
        if(info !=null){
            BeanUtils.copyProperties(info,userVO);
            return Response.success(userVO);
        }
        return Response.error();
    }

    @PutMapping("/count/reduce/{id}")
    public Response<Void> reduceCountById(@PathVariable Long id){
        if(userService.reduceCountById(id)){
            return Response.success();
        }
        return Response.error();
    }

    @PostMapping("/update")
    public Response<Void> updateByEntity(@RequestBody UserInfoDTO userInfo){
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userInfo,userEntity);
        userEntity.setUpdateTime(DateUtil.date());
        boolean update = userService.updateById(userEntity);
        if(update){
            return Response.success();
        }
        return Response.error();
    }
    @Transactional
    @DeleteMapping("/delete/{id}")
    public Response<Void> deleteById(@PathVariable Long id){

        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setUpdateTime(DateUtil.date());
        boolean update = userService.updateById(userEntity);
        boolean delete = userService.removeById(id);
        if(update && delete){
            return Response.success();
        }
        return Response.error();
    }
}
