package com.nu11.backend.dto;

import lombok.Data;

@Data
public class ApiParamsDTO {
    private Long id;

    private Long apiId;
    /**
     * 接口参数的类型如Header Query Body
     */
    private String type;
    /**
     * 参数名字
     */
    private String name;
    /**
     * 参数的示例值
     */
    private String value;
}
