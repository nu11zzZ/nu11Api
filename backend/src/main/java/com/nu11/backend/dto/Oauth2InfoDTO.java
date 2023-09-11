package com.nu11.backend.dto;

import lombok.Data;

@Data
public class Oauth2InfoDTO {
    /**
     * 通过第三方的OpenAPI获取的用户在第三方平台的id
     */
    private Long id;
    /**
     * 通过第三方的OpenAPI获取的用户在第三方平台的name
     */
    private String name;
}
