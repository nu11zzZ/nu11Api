package com.nu11api.client.utils;

import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;

public class SignatureUtils {
    public static String sign(String access_key,String timestamp,String secret_key){
        HMac hMac = new HMac(HmacAlgorithm.HmacSHA256, secret_key.getBytes());
        return hMac.digestHex(access_key+timestamp);
    }
}
