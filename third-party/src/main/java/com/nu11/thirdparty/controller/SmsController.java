package com.nu11.thirdparty.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.nu11.common.utils.HttpUtils;
import com.nu11.common.utils.Response;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/sms")
public class SmsController {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    @GetMapping("/getCaptcha")
    public Response<Void> getCaptcha(@RequestParam("phone") String phone){
        if(StrUtil.isNotEmpty(phone)){
            String captcha = (String) redisTemplate.opsForValue().get(phone);
            if(captcha == null){
                String code = RandomUtil.randomNumbers(6);
                String host = "https://dfsns.market.alicloudapi.com";
                String path = "/data/send_sms";
                String method = "POST";
                String appcode = "";
                Map<String, String> headers = new HashMap<String, String>();
                //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
                headers.put("Authorization", "APPCODE " + appcode);
                //根据API的要求，定义相对应的Content-Type
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                Map<String, String> querys = new HashMap<String, String>();
                Map<String, String> bodys = new HashMap<String, String>();
                bodys.put("content", "code:"+code);
                bodys.put("template_id", "CST_ptdie100");
                bodys.put("phone_number", phone);


                try {
                    /**
                     * 重要提示如下:
                     * HttpUtils请从
                     * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
                     * 下载
                     *
                     * 相应的依赖请参照
                     * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
                     */
                    HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
                    if(response.getStatusLine().getStatusCode() == 200){
                        redisTemplate.opsForValue().set(phone,code,10, TimeUnit.MINUTES);
                        return Response.success();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return Response.error();

    }
}
