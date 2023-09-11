package com.nu11.gateway.feign;

import com.nu11.gateway.common.Response;
import com.nu11.gateway.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("backend-10001")
public interface BackendFeignService {

    @GetMapping("/backend/user/invoke/{access_key}")
    Response<UserVO> getInfoByAccessKey(@PathVariable String access_key);

    @PutMapping("/backend/user/count/reduce/{id}")
    Response<Void> reduceCountById(@PathVariable Long id);
}
