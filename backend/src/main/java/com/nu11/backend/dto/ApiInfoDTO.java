package com.nu11.backend.dto;

import lombok.Data;

@Data
public class ApiInfoDTO {
    private Long id;
    /**
     * 接口名字
     */
    private String name;
    /**
     * 接口调用地址
     */
    private String url;
    /**
     * 接口调用方法类型
     */
    private String method;
    /**
     * 接口描述
     */
    private String description;
    /**
     * 接口被调用次数
     */
    private Long count;

    private Integer status;
}
