package com.nu11.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.nu11.gateway.dto.UserApiInfoDTO;
import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class DebugGatewayFilterFactory extends AbstractGatewayFilterFactory<DebugGatewayFilterFactory.Config> {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;

    public DebugGatewayFilterFactory() {
        super(DebugGatewayFilterFactory.Config.class);
    }
    //使用yaml配置路由时，对参数进行排序。
    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("access_key");
    }

    @Override
    public GatewayFilter apply(DebugGatewayFilterFactory.Config config) {
        // grab configuration from Config object
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String accessKey = headers.getFirst(config.getAccess_key());

            if(StrUtil.isNotEmpty(accessKey) && (redisTemplate.opsForValue().get(accessKey) == null || (Integer)redisTemplate.opsForValue().get(accessKey) <= 10)){
                redisTemplate.opsForValue().setIfAbsent(accessKey,1,30, TimeUnit.SECONDS);
                redisTemplate.opsForValue().setIfPresent(accessKey,(Integer)redisTemplate.opsForValue().get(accessKey)+1,redisTemplate.getExpire(accessKey),TimeUnit.SECONDS);
                UserApiInfoDTO userApiInfo = new UserApiInfoDTO();
                userApiInfo.setUrl(exchange.getRequest().getURI().getPath());
                rabbitTemplate.convertAndSend("amq.fanout","",userApiInfo);
                ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
                return chain.filter(exchange.mutate().request(builder.build()).build());
            }
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return response.setComplete();
        };
    }
    @Data
    public static class Config {
        private String access_key;
    }
}
