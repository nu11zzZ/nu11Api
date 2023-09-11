package com.nu11.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.nu11.gateway.common.Response;
import com.nu11.gateway.dto.UserApiInfoDTO;
import com.nu11.gateway.feign.BackendFeignService;
import com.nu11.gateway.utils.SignatureUtils;
import com.nu11.gateway.vo.UserVO;
import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Component
public class InvokeApiGatewayFilterFactory extends AbstractGatewayFilterFactory<InvokeApiGatewayFilterFactory.Config> {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Lazy
    @Autowired
    BackendFeignService backendFeignService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    ExecutorService executor = Executors.newFixedThreadPool(10);

    public InvokeApiGatewayFilterFactory() {
        super(InvokeApiGatewayFilterFactory.Config.class);
    }
    //使用yaml配置路由时，对参数进行排序。
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("access_key","timestamp","signature");
    }

    @Override
    public GatewayFilter apply(InvokeApiGatewayFilterFactory.Config config) {
        // grab configuration from Config object
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String access_key = headers.getFirst(config.getAccess_key());
            String timestamp = headers.getFirst(config.getTimestamp());
            String signature = headers.getFirst(config.getSignature());
            //判断请求头是否携带access_key
            //防重放攻击，验证timestamp是否合法
            if(StrUtil.isNotEmpty(access_key) && StrUtil.isNotEmpty(timestamp) && StrUtil.isNotEmpty(signature) && (System.currentTimeMillis() - Long.parseLong(timestamp) < 30 * 1000)){
                //验证access_key的合法性,采用openfeign调用backend进行查询数据库
                ReentrantLock lock = new ReentrantLock();
                lock.lock();
                Future<Response<UserVO>> future = executor.submit(() -> {
                    return backendFeignService.getInfoByAccessKey(access_key);
                });

                Response<UserVO> info = null;
                try {
                    info = future.get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                UserVO data = info.getData();
                //检查用户剩余调用次数，以及签名是否有效

                if(data != null && data.getApiCount() > 0 && SignatureUtils.sign(access_key,timestamp,data.getSecretKey()).equals(signature)){
                    //远程调用backend，对用户的调用次数进行-1
                    executor.execute(() -> {
                        backendFeignService.reduceCountById(data.getId());
                    });
                    lock.unlock();
                    //发送消息给mq，通知对该接口调用次数+1 将用户信息和路径封装成DTO对象
                    UserApiInfoDTO userApiInfo = new UserApiInfoDTO();
                    userApiInfo.setId(data.getId());
                    userApiInfo.setUrl(exchange.getRequest().getURI().getPath());
                    rabbitTemplate.convertAndSend("amq.fanout","",userApiInfo);
//                    System.out.println(exchange.getRequest().getURI().getPath());
                    ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
                    return chain.filter(exchange.mutate().request(builder.build()).build());
                }
            }

            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        };
    }
    @Data
    public static class Config {
        private String access_key;
        private String timestamp;
        private String signature;
    }
}
