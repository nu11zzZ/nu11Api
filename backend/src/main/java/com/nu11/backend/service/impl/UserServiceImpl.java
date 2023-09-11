package com.nu11.backend.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nu11.backend.dao.UserDao;
import com.nu11.backend.dto.LoginInfoDTO;
import com.nu11.backend.dto.Oauth2InfoDTO;
import com.nu11.backend.dto.RegistryInfoDTO;
import com.nu11.backend.entity.UserEntity;

import com.nu11.backend.service.UserService;
import com.nu11.backend.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public List<UserVO> getAllUser() {
        List<UserEntity> users = baseMapper.selectList(null);
        List<UserVO> userVOS = users.stream().map(item -> {
            UserVO userVo = new UserVO();
            BeanUtils.copyProperties(item, userVo);
            return userVo;
        }).collect(Collectors.toList());
        return userVOS;
    }

    @Override
    public String verifyInfo(LoginInfoDTO loginInfo) {
        UserEntity user = baseMapper.selectOne(new LambdaUpdateWrapper<UserEntity>().eq(UserEntity::getUserName, loginInfo.getUserName()));
        if(user != null){
            if(bCryptPasswordEncoder.matches(loginInfo.getPassword(), user.getPassword())){
                //生成JWT
                Map<String, Object> payload = new HashMap<>();
                payload.put("id",user.getId());
                payload.put("username",user.getUserName());
                payload.put("type",user.getType());
                payload.put("exp",3600);
                String access_token = JWTUtil.createToken(payload, UUID.randomUUID().toString().replace("-", "").getBytes());


                return access_token;
        }

        }
        return null;
    }

    @Override
    public void addUser(RegistryInfoDTO registryInfo) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(registryInfo,user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setAccessKey(RandomUtil.randomNumbers(9));
        //u7aEVVz5BQVLm6yHb0Lyk4DozkewIEin
        user.setSecretKey(RandomUtil.randomString(32));
        user.setCreateTime(DateUtil.date());
        user.setUpdateTime(DateUtil.date());
        baseMapper.insert(user);
    }

    @Override
    public void addUser(Oauth2InfoDTO oauth2Info) {
        UserEntity user = new UserEntity();
        user.setOauthId(oauth2Info.getId());
        user.setUserName(oauth2Info.getName());
        user.setPassword(bCryptPasswordEncoder.encode(user.getUserName()));
        user.setAccessKey(RandomUtil.randomNumbers(9));
        //u7aEVVz5BQVLm6yHb0Lyk4DozkewIEin
        user.setSecretKey(RandomUtil.randomString(32));
        user.setCreateTime(DateUtil.date());
        user.setUpdateTime(DateUtil.date());
        baseMapper.insert(user);
    }

    @Override
    public Page<UserVO> getListUser(Map<String, Object> params) {
        Page<UserEntity> userPage = new Page<>( Long.parseLong((String)params.get("page")), Long.parseLong((String) params.get("limit")));
        LambdaUpdateWrapper<UserEntity> wrapper = new LambdaUpdateWrapper<>();
        if(StrUtil.isNotEmpty((String) params.get("key"))){
            wrapper.like(UserEntity::getId,params.get("key"));
        }
        IPage<UserEntity> page = baseMapper.selectPage(userPage, wrapper);
        Page<UserVO> result = new Page<>();
        List<UserVO> list = page.getRecords().stream().map(item -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(item, userVO);
            return userVO;
        }).collect(Collectors.toList());
        result.setRecords(list);
        result.setTotal(page.getTotal());
        return result;
    }

    @Override
    public boolean reduceCountById(Long id) {
        boolean success = baseMapper.reduceCountById(id);
        return false;
    }
}
