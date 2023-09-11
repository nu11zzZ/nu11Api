package com.nu11.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nu11.backend.dto.LoginInfoDTO;
import com.nu11.backend.dto.Oauth2InfoDTO;
import com.nu11.backend.dto.RegistryInfoDTO;
import com.nu11.backend.entity.UserEntity;

import com.nu11.backend.vo.UserVO;

import java.util.List;
import java.util.Map;


public interface UserService extends IService<UserEntity> {

    List<UserVO> getAllUser();

    String verifyInfo(LoginInfoDTO loginInfo);

    void addUser(RegistryInfoDTO registryInfo);

    void addUser(Oauth2InfoDTO oauth2Info);

    Page<UserVO> getListUser(Map<String, Object> params);

    boolean reduceCountById(Long id);
}
