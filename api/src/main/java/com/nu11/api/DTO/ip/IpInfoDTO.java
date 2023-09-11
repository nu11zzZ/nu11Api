package com.nu11.api.DTO.ip;

import lombok.Data;

@Data
public class IpInfoDTO {
    private int ret;
    private String msg;
    private IpData data;
    private String log_id;
    @Data
    private static class IpData{
        private String ip;
        private String long_ip;
        private String isp;
        private String area;
        private String region_id;
        private String region;
        private String city_id;
        private String city;
        private String country_id;
        private String country;
    }
}
