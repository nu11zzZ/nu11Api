package com.nu11.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nu11.backend.entity.ApiParamsEntity;
import com.nu11.backend.vo.ApiParamsVO;

import java.util.List;

public interface ApiParamsService extends IService<ApiParamsEntity> {
    List<ApiParamsVO> getInfoByApiId(Long apiId);
}
