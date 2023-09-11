package com.nu11.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nu11.backend.entity.UserEntity;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseMapper<UserEntity> {
    boolean reduceCountById(@Param("id") Long id);
}
