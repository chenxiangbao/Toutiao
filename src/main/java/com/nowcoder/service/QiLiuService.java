package com.nowcoder.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.nowcoder.util.TouTiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class QiLiuService {
    //private static final Logger logger = (Logger) LoggerFactory.getLogger(QiLiuService.class);
    //...生成上传凭证，然后准备上传

//    String accessKey = "3uuxBHGDl3q1chIOjSQ7Rm8TbZfu_k8WBYAKJTOf";
//    String secretKey = "c6vatctiYPBWi_iflGYh-EkzCyNJJwpeM_QT7Bng";
//    String bucket = "chenxiangbao";
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String accessKey = "abNXnXBIlI6viRaOeRY6Hk-zc3V-NpjLcGfYz5kD";
    String secretKey = "QP7Xja3FmP1Zyl-oxwQDCb7T6wCoEFKoO-0vht_5";
    //要上传的空间
    String bucket = "nowcoder";
    //如果是Windows情况下，格式是 D:\\qiniu\\test.png chenxiangbao
    //String localFilePath = "D:/qiliu/";
    //默认不指定key的情况下，以文件内容的hash值作为文件名
    String key = null;

    Auth auth = Auth.create(accessKey, secretKey);
    UploadManager uploadManager = new UploadManager();
    String upToken = auth.uploadToken(bucket);

    public String savaImage(MultipartFile file)throws IOException {
        try {

            int doPos =  file.getOriginalFilename().lastIndexOf(".");
            if(doPos<0){
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(doPos+1).toLowerCase();
            //判断是否是图片
            if(!TouTiaoUtil.isFileAllowed(fileExt)){
                return null;
            }
            //获取字符串前缀进行md5后再进行加后缀
            String fileName = TouTiaoUtil.getMD5(file.getOriginalFilename().substring(0,file.getOriginalFilename().indexOf(".")))+"."+fileExt;


            Response response = uploadManager.put(file.getBytes(),key,upToken);
//            //解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
            System.out.println(response.bodyString());
            if(response.isJson() && response.isOK()){
                  return TouTiaoUtil.QINIU_URL+ JSONObject.parseObject(response.bodyString()).get("key");
            }
            return null;
        } catch (QiniuException ex) {
                return null;
//            Response r = ex.response;
//            System.err.println(r.toString());
//            try {
//                System.err.println(r.bodyString());
//            } catch (QiniuException ex2) {
//                //ignore
//                rerurn
//            }
        }
    }
}




