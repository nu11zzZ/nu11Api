package com.nu11.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nu11.backend.entity.ApiEntity;
import com.nu11.backend.vo.ApiAnalyseVO;
import com.nu11.backend.vo.ApiInfoVO;

import java.util.List;
import java.util.Map;

public interface ApiService extends IService<ApiEntity> {
    Page<ApiEntity> getListApi(Map<String, Object> params);

    List<ApiInfoVO> getApiInfo();


    List<ApiAnalyseVO> analyse();

}
