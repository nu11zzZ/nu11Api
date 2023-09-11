package com.nu11.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nu11.backend.dao.ApiParamsDao;
import com.nu11.backend.entity.ApiParamsEntity;
import com.nu11.backend.service.ApiParamsService;
import com.nu11.backend.vo.ApiParamsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiParamsServiceImpl extends ServiceImpl<ApiParamsDao, ApiParamsEntity> implements ApiParamsService {
    @Override
    public List<ApiParamsVO> getInfoByApiId(Long apiId) {
        List<ApiParamsEntity> list = baseMapper.selectList(new LambdaUpdateWrapper<ApiParamsEntity>().eq(ApiParamsEntity::getApiId, apiId));
        List<ApiParamsVO> infos = list.stream().map(item -> {
            ApiParamsVO apiParamsVO = new ApiParamsVO();
            BeanUtils.copyProperties(item, apiParamsVO);
            return apiParamsVO;
        }).collect(Collectors.toList());
        return infos;
    }
}
