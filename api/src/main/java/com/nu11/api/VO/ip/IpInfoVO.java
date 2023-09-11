package com.nu11.api.VO.ip;

import lombok.Data;

@Data
public class IpInfoVO {
//            "ip": "218.18.228.178",
    private String ip;
//            "long_ip": "3658671282",
//            "isp": "电信",
    private String isp;
//            "area": "华南",
    private String area;
//            "region_id": "440000",
//            "region": "广东",
    private String region;
//            "city_id": "440300",
//            "city": "深圳",
    private String city;
//            "country_id": "CN",
//            "country": "中国"
    private String country;
}
