package com.nu11.common.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * 全局统一响应类
 * @author nu11zzZ
 * @param <T>
 */
@Data
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private T data;

    public static <T> Response<T> success(T data){
        Response<T> response = new Response<>();
        response.setCode(200);
        response.setMessage("成功");
        response.setData(data);
        return response;
    }
    public static <T> Response<T> success(){
        Response<T> response = new Response<>();
        response.setCode(200);
        response.setMessage("成功");
        return response;
    }

    public static <T> Response<T> error(){
        Response<T> response = new Response<>();
        response.setCode(400);
        response.setMessage("失败");
        return response;
    }
}
