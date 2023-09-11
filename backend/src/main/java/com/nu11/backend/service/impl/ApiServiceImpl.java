package com.nu11.backend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nu11.backend.dao.ApiDao;
import com.nu11.backend.dto.UserApiInfoDTO;
import com.nu11.backend.entity.ApiEntity;
import com.nu11.backend.entity.UserEntity;
import com.nu11.backend.service.ApiService;
import com.nu11.backend.vo.ApiAnalyseVO;
import com.nu11.backend.vo.ApiInfoVO;
import com.nu11.backend.vo.UserVO;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApiServiceImpl extends ServiceImpl<ApiDao, ApiEntity> implements ApiService {
    @Override
    public Page<ApiEntity> getListApi(Map<String, Object> params) {

            Page<ApiEntity> apiPage = new Page<>( Long.parseLong((String)params.get("page")), Long.parseLong((String) params.get("limit")));
            LambdaUpdateWrapper<ApiEntity> wrapper = new LambdaUpdateWrapper<>();
            if(StrUtil.isNotEmpty((String) params.get("key"))){
                wrapper.like(ApiEntity::getId,params.get("key"));
            }
            IPage<ApiEntity> page = baseMapper.selectPage(apiPage, wrapper);

            return (Page<ApiEntity>) page;

    }

    @Override
    public List<ApiInfoVO> getApiInfo() {
        List<ApiEntity> apiEntities = baseMapper.selectList(new LambdaQueryWrapper<ApiEntity>().eq(ApiEntity::getStatus, 1));
        List<ApiInfoVO> list = apiEntities.stream().map(item -> {
            ApiInfoVO apiInfoVO = new ApiInfoVO();
            BeanUtils.copyProperties(item, apiInfoVO);
            return apiInfoVO;
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<ApiAnalyseVO> analyse() {
        List<ApiAnalyseVO> infos = baseMapper.analyse();
        return infos;
    }

    @RabbitListener(queues = {"apicount"})
    public void increaseCount(Message message,UserApiInfoDTO userApiInfo, Channel channel) throws IOException {
        Boolean success = baseMapper.increaseCount(userApiInfo.getUrl());
        long tag = message.getMessageProperties().getDeliveryTag();
        if(success){
            channel.basicAck(tag,false);
        }else {
            channel.basicNack(tag,false,false);
        }
    }
}
