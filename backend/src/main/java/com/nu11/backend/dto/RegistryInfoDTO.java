package com.nu11.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegistryInfoDTO {
    @NotEmpty(message = "用户名不能为空")
    private String userName;
    @NotEmpty(message = "密码不能为空")
    private String password;
    @NotEmpty(message = "手机号不能为空")
    private String phone;
    @NotEmpty(message = "验证码不能为空")
    private String captcha;
}
