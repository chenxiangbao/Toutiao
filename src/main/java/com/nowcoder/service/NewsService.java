package com.nowcoder.service;

import com.nowcoder.dao.NewsDAO;
import com.nowcoder.model.News;
import com.nowcoder.util.TouTiaoUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class NewsService {

    @Autowired
    private NewsDAO newsDAO;

    public News getByNewsId(int id){
        News news = newsDAO.selectById(id);
        return news;
    }

    public void updateNews(int id,int count){
        newsDAO.updateCommentCount(id,count);
    }

    public void updateLikeCount(int id,int likeCount){
        newsDAO.updateLikeCount(id,likeCount);
    }

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public int addNews(News news){
        newsDAO.addNews(news);
        return news.getId();
    }

    public String  saveImage(MultipartFile file)throws IOException{
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

        //file.transferTo(fileName);
        Files.copy(file.getInputStream(),new File(TouTiaoUtil.IMAGE_DIR+fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        //XXX.jpg
        return "image?name=" +fileName;
    }
}
