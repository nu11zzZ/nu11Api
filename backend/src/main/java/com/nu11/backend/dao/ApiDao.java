package com.nu11.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nu11.backend.entity.ApiEntity;
import com.nu11.backend.vo.ApiAnalyseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ApiDao extends BaseMapper<ApiEntity> {
    Boolean increaseCount(@Param("url") String url);

    List<ApiAnalyseVO> analyse();
}
