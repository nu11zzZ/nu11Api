package com.nu11.backend.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserApiInfoDTO implements Serializable {

    private String url;

    private Long id;
}
