package com.nu11.api.controller;

import cn.hutool.json.JSONUtil;
import com.nu11.api.DTO.bz.BzInfoDTO;
import com.nu11.api.DTO.ip.IpInfoDTO;
import com.nu11.api.VO.bz.BzInfoVO;
import com.nu11.api.VO.ip.IpInfoVO;
import com.nu11.common.utils.HttpUtils;
import com.nu11.common.utils.Response;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BzController {
    @GetMapping("/bz")
    public Response<BzInfoVO> bz(){
        String host = "https://api.btstu.cn";
        String path = "/sjbz/api.php";
        String method = "GET";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        Map<String, String> querys = new HashMap<>();
        querys.put("format", "json");
        querys.put("lx","fengjing");
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.getStatusLine().getStatusCode());
            String responseJson = EntityUtils.toString(response.getEntity());
            System.out.println(responseJson);

            if (response.getStatusLine().getStatusCode() == 200) {
                BzInfoDTO bzInfo = JSONUtil.toBean(responseJson, BzInfoDTO.class);
                BzInfoVO bzInfoVO = new BzInfoVO();
                BeanUtils.copyProperties(bzInfo, bzInfoVO);
                return Response.success(bzInfoVO);
            }
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.error();
    }
}
