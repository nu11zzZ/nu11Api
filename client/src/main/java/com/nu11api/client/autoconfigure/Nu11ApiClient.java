package com.nu11api.client.autoconfigure;

import com.nu11api.client.utils.HttpUtils;
import com.nu11api.client.utils.SignatureUtils;
import lombok.Data;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

@Data
public class Nu11ApiClient {

    private String access_key;

    private String secret_key;

    public Nu11ApiClient(String access_key,String secret_key){

        this.access_key = access_key;
        this.secret_key = secret_key;
    }

    public HttpResponse ip(String ip){
        String host = "http://localhost:10000/api/interface";
        String path = "/ip";
        String method = "GET";
        Map<String, String> headers = new HashMap<>();
        headers.put("access_key",access_key);
        headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
        headers.put("signature", SignatureUtils.sign(access_key,headers.get("timestamp"),secret_key));

        Map<String, String> querys = new HashMap<>();
        querys.put("ip", ip);
        HttpResponse response = null;
        try {
            response = HttpUtils.doGet(host, path, method, headers, querys);
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    public HttpResponse bz(){
        String host = "http://localhost:10000/api/interface";
        String path = "/bz";
        String method = "GET";
        Map<String, String> headers = new HashMap<>();
        headers.put("access_key",access_key);
        headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
        headers.put("signature", SignatureUtils.sign(access_key,headers.get("timestamp"),secret_key));

        Map<String, String> querys = new HashMap<>();
        HttpResponse response = null;
        try {
            response = HttpUtils.doGet(host, path, method, headers, querys);
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
