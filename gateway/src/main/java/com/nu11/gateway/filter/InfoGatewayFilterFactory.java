package com.nu11.gateway.filter;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;


import java.util.Collections;
import java.util.List;

@Component
public class InfoGatewayFilterFactory extends AbstractGatewayFilterFactory<InfoGatewayFilterFactory.Config> {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    public InfoGatewayFilterFactory() {
        super(Config.class);
    }
    //使用yaml配置路由时，对参数进行排序。
    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("access_token");
    }

    @Override
    public GatewayFilter apply(Config config) {
        // grab configuration from Config object
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String accessToken = headers.getFirst(config.getAccess_token());

            if(StrUtil.isNotEmpty(accessToken) && redisTemplate.opsForValue().get(accessToken) != null){
                ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
                return chain.filter(exchange.mutate().request(builder.build()).build());
            }
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        };
    }
    @Data
    public static class Config {
        private String access_token;
    }

}