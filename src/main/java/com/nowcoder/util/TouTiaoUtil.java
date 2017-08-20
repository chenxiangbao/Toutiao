package com.nowcoder.util;

import com.alibaba.fastjson.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Map;

public class TouTiaoUtil {
    //http://ou1qpmn7z.bkt.clouddn.com/cxb.jpg
    public static String QINIU_URL="http://ou1qpmn7z.bkt.clouddn.com/";
    public static String TOUTIAO_DOMAIN="http:localhost:8080/";
    public static String IMAGE_DIR = "C:/Users/11447/Desktop/upload/";
    public static String[] IMAGE_FILE_EXT = new String[]{"png","bmp","jpg","jpeg"};
    public static String getJsonString(int code){
        JSONObject json = new JSONObject();
        json.put("code",code);
        return json.toJSONString();
    }
    public static String getJsonString(int code,String msg){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        return json.toJSONString();
    }
    public static String getJsonString(int code, Map<String,Object> map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        for(Map.Entry<String,Object> entry:map.entrySet()){
            json.put(entry.getKey(),entry.getValue());
        }
        return json.toJSONString();
    }
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }
    public static boolean isFileAllowed(String fileext){
        for(String ext:IMAGE_FILE_EXT){
                if(ext.equals(fileext)){
                    return true;
                }
        }
        return false;
    }
}
