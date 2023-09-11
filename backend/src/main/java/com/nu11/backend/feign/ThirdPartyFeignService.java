package com.nu11.backend.feign;

import com.nu11.common.utils.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("third-party-10003")
public interface ThirdPartyFeignService {
    @GetMapping("/sms/getCaptcha")
    Response<Void> getCaptcha(@RequestParam("phone") String phone);
}
