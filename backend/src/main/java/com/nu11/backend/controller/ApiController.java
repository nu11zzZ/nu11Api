package com.nu11.backend.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nu11.backend.dto.ApiInfoDTO;
import com.nu11.backend.entity.ApiEntity;

import com.nu11.backend.service.ApiService;
import com.nu11.backend.vo.ApiAnalyseVO;
import com.nu11.backend.vo.ApiInfoVO;

import com.nu11.common.utils.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    ApiService apiService;
    @GetMapping("/list")
    public Response<Page<ApiEntity>> getAllApi(@RequestParam Map<String,Object> params){
        Page<ApiEntity> list = apiService.getListApi(params);
        return Response.success(list);
    }
    @GetMapping("/all")
    public Response<List<ApiInfoVO>> getApiInfo(){
        List<ApiInfoVO> list = apiService.getApiInfo();
        return Response.success(list);
    }

    @GetMapping("/info/{id}")
    public Response<ApiEntity> getInfoById(@PathVariable Long id){

        ApiEntity info = apiService.getById(id);
        if(info != null){
            return Response.success(info);
        }
        return Response.error();
    }
    @PostMapping("/save")
    public Response<Void> save(@RequestBody ApiInfoDTO apiInfo){
        ApiEntity apiEntity = new ApiEntity();
        BeanUtils.copyProperties(apiInfo,apiEntity);
        apiEntity.setCreateTime(DateUtil.date());
        apiEntity.setUpdateTime(DateUtil.date());
        boolean save = apiService.save(apiEntity);
        if (save){
            return Response.success();
        }
        return Response.error();
    }
    @PostMapping("/update")
    public Response<Void> updateByEntity(@RequestBody ApiInfoDTO apiInfo){
        ApiEntity apiEntity = new ApiEntity();
        BeanUtils.copyProperties(apiInfo,apiEntity);
        apiEntity.setUpdateTime(DateUtil.date());
        boolean update = apiService.updateById(apiEntity);
        if (update){
            return Response.success();
        }
        return Response.error();
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public Response<Void> deleteById(@PathVariable Long id){

        ApiEntity apiEntity = new ApiEntity();
        apiEntity.setId(id);
        apiEntity.setUpdateTime(DateUtil.date());
        boolean update = apiService.updateById(apiEntity);
        boolean delete = apiService.removeById(id);
        if(update && delete){
            return Response.success();
        }
        return Response.error();
    }

    @GetMapping("/analyse")
    public Response<List<ApiAnalyseVO>> analyse(){
        List<ApiAnalyseVO> infos = apiService.analyse();
        return Response.success(infos);
    }
}
