package com.nu11api.client.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(
        proxyBeanMethods = false
)
@EnableConfigurationProperties(Nu11ApiClientProperties.class)
public class Nu11ApiClientAutoConfiguration {

    @Bean
    public Nu11ApiClient nu11ApiClient(Nu11ApiClientProperties nu11ApiClientProperties){
        return new Nu11ApiClient(nu11ApiClientProperties.getAccess_key(),nu11ApiClientProperties.getSecret_key());
    }
}
