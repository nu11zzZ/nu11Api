package com.nu11.backend.vo;


import lombok.Data;

import java.util.Date;
@Data
public class UserVO {
    private Long id;

    private String userName;

    private Long oauthId;

    private String phone;

    private Integer type;

    private String accessKey;

    private String secretKey;

    private Long apiCount;

    private Date createTime;

    private Date updateTime;

}
