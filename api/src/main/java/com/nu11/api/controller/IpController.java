package com.nu11.api.controller;

import cn.hutool.json.JSONUtil;
import com.nu11.api.DTO.ip.IpInfoDTO;
import com.nu11.api.VO.ip.IpInfoVO;
import com.nu11.common.utils.HttpUtils;
import com.nu11.common.utils.Response;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class IpController {
    @GetMapping("/ip")
    public Response<IpInfoVO> getIpInfo(@RequestParam("ip") String ip) {
        String host = "https://c2ba.api.huachen.cn";
        String path = "/ip";
        String method = "GET";
        String appcode = "569ae1411ee3419d87270a7b17924539";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("ip", ip);


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
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.getStatusLine().getStatusCode());
            String responseJson = EntityUtils.toString(response.getEntity());
            System.out.println(responseJson);
            IpInfoDTO ipInfo = JSONUtil.toBean(responseJson, IpInfoDTO.class);
            if (ipInfo.getRet() == 200) {
                IpInfoVO ipInfoVO = new IpInfoVO();
                BeanUtils.copyProperties(ipInfo.getData(), ipInfoVO);
                return Response.success(ipInfoVO);
            }
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.error();
    }
}
