package com.nu11.backend.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    private Long id;

    private String userName;

    private String phone;

    private Integer type;

    private String accessKey;

    private String secretKey;

    private Long apiCount;
}
