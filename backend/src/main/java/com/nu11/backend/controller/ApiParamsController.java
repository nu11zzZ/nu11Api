package com.nu11.backend.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import com.nu11.backend.dto.ApiParamsDTO;
import com.nu11.backend.entity.ApiParamsEntity;
import com.nu11.backend.service.ApiParamsService;
import com.nu11.backend.vo.ApiParamsVO;
import com.nu11.common.utils.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/params")
public class ApiParamsController {

    @Autowired
    ApiParamsService apiParamsService;

    @GetMapping("/info")
    public Response<ApiParamsVO> getInfoById(@RequestParam Long id){
        ApiParamsEntity info = apiParamsService.getById(id);
        if(info != null){
            ApiParamsVO paramsVO = new ApiParamsVO();
            BeanUtils.copyProperties(info,paramsVO);
            return Response.success(paramsVO);
        }
        return Response.error();

    }

    @GetMapping("/info/{apiId}")
    public Response<List<ApiParamsVO>> getInfoByApiId(@PathVariable Long apiId){
        List<ApiParamsVO> info = apiParamsService.getInfoByApiId(apiId);
        return Response.success(info);
    }

    @Transactional
    @PostMapping("/delete/{id}")
    public Response<Void> deleteById(@PathVariable Long id){
        ApiParamsEntity entity = new ApiParamsEntity();

        entity.setUpdateTime(DateUtil.date());
        entity.setId(id);
        boolean update = apiParamsService.updateById(entity);

        boolean delete = apiParamsService.removeById(id);
        if(delete && update){
            return Response.success();
        }
        return Response.error();
    }

    @PostMapping("/save")
    public Response<Void> save(@RequestBody ApiParamsDTO apiParams){
        ApiParamsEntity entity = new ApiParamsEntity();
        BeanUtils.copyProperties(apiParams,entity);
        entity.setCreateTime(DateUtil.date());
        entity.setUpdateTime(DateUtil.date());

        boolean save = apiParamsService.save(entity);
        if(save){
            return Response.success();
        }
        return Response.error();
    }

    @PostMapping("/update")
    public Response<Void> update(@RequestBody ApiParamsDTO apiParams){
        ApiParamsEntity entity = new ApiParamsEntity();
        BeanUtils.copyProperties(apiParams,entity);
        entity.setUpdateTime(DateUtil.date());
        boolean update = apiParamsService.updateById(entity);
        if(update){
            return Response.success();
        }
        return Response.error();
    }
}
