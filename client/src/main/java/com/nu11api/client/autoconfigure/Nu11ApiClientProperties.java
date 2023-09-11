package com.nu11api.client.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "spring.nu11api.client"
)
@Data
public class Nu11ApiClientProperties {

    private String access_key;

    private String secret_key;


}
