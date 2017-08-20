package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.TouTiaoUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {

    @Autowired
    NewsService newsService;
    @Autowired
    QiLiuService qiLiuService;
    @Autowired
    UserService userService;
    @Autowired
    HostLocal hostLocal;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;


    /**
     * 添加评论
     * @return
     */
    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId")int newsId, Comment comment,Model model){
        //其中content已经加入
        if(hostLocal.getUsers()==null){
            comment.setUserId(3);
        }
        comment.setUserId(hostLocal.getUsers().getId());
        comment.setCreateDate(new Date());
        comment.setEntityId(newsId);
        comment.setEntityType(EntityType.ENTITY_NEWS);
        comment.setStatus(0);

        commentService.addComment(comment);
        //更新news里面的评论数量
        int count = commentService.getCommentCount(comment.getEntityId(),EntityType.ENTITY_NEWS);
        newsService.updateNews(comment.getEntityId(),count);
        return "redirect:/news/"+String.valueOf(newsId);
    }

    /**
     * 删除评论
     */
    @RequestMapping(path = {"/deleteComment/{id}"},method = {RequestMethod.GET})
    public String deleteComment(@PathVariable  String id){
        int idc = Integer.parseInt(id);
        Comment comment = commentService.selectById(idc);
        commentService.deleteComment(idc,comment.getEntityId(),comment.getEntityType());
        int newsId = comment.getEntityId();
        //更新news里面的评论数量
        int count = commentService.getCommentCount(comment.getEntityId(),EntityType.ENTITY_NEWS);
        newsService.updateNews(comment.getEntityId(),count);
        return "redirect:/news/"+String.valueOf(newsId);
    }
    /**
     * 通过newsId查询评论页
     * @param newsId
     * @param model
     * @return
     */
    @RequestMapping(path = {"/news/{newsId}"},method = {RequestMethod.GET})
    public String newsDetials(@PathVariable int newsId, Model model){
        News news = newsService.getByNewsId(newsId);
        int localHostId = hostLocal.getUsers() !=null ? hostLocal.getUsers().getId() :0;
        if(localHostId!=0){
            model.addAttribute("like",likeService.getLikeStatus(localHostId, EntityType.ENTITY_NEWS,newsId));
        }else{
            model.addAttribute("like",0);
        }
        if(news!=null) {
            //评论
            //获取所有评论
            List<Comment> commentList = commentService.selectByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentvos = new ArrayList<>();
            for(Comment comment:commentList){
                if(comment.getStatus()==0){
                    ViewObject vo = new ViewObject();
                    vo.set("comment",comment);
                    //通过评论里面的userID获取用户信息
                    vo.set("user",userService.getUser(comment.getUserId()));
                    commentvos.add(vo);
                }
            }
            model.addAttribute("comments",commentvos);
        }
        model.addAttribute("news", news);
        model.addAttribute("owner",userService.getUser(news.getUserId()));
        return "detail";
    }

    @RequestMapping(path = {"/addNews/"},method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image")String image,
                          @RequestParam("title")String title,
                          @RequestParam("link")String link ){
        if(StringUtils.isBlank(title) || StringUtils.isBlank(link) || StringUtils.isBlank(image)){
            return TouTiaoUtil.getJsonString(1,"标题或者链接以及图片不能为空");
        }
        if(!link.substring(0,link.indexOf(":")).equals("http")){
            return TouTiaoUtil.getJsonString(1,"链接要以http开头");
        }
        try {
            News news = new News();
            if(hostLocal.getUsers()!=null){
                news.setUserId(hostLocal.getUsers().getId());
            }else{
                //匿名用户可以自己定义一个id为匿名用户
                news.setUserId(3);
            }
            news.setTitle(title);
            news.setLink(link);
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setLikeCount(0);
            news.setCommentCount(0);

            newsService.addNews(news);
            return TouTiaoUtil.getJsonString(0);
        }catch (Exception e){
            e.getMessage();
            return  TouTiaoUtil.getJsonString(1,"发布失败");
        }
    }

    @RequestMapping(path = {"/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response){
        //String ext = imageName.substring(imageName.indexOf(".")+1);
        //String image = TouTiaoUtil.getMD5(imageName.substring(0,imageName.indexOf(".")));
       // imageName = image+"."+ext;
        response.setContentType("image/jpg");
        try {
            StreamUtils.copy(new FileInputStream(new File(TouTiaoUtil.IMAGE_DIR+imageName)),response.getOutputStream());
        }catch (Exception e){
            e.getMessage();
        }

    }
    @RequestMapping(path = {"/user/{userId}/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@PathVariable int userId,@RequestParam("name") String imageName, HttpServletResponse response){
        //String ext = imageName.substring(imageName.indexOf(".")+1);
        //String image = TouTiaoUtil.getMD5(imageName.substring(0,imageName.indexOf(".")));
        // imageName = image+"."+ext;
        response.setContentType("image/jpg");
        try {
            StreamUtils.copy(new FileInputStream(new File(TouTiaoUtil.IMAGE_DIR+imageName)),response.getOutputStream());
        }catch (Exception e){
            e.getMessage();
        }

    }
    @RequestMapping(path = {"/news/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImages(@RequestParam("name") String imageName, HttpServletResponse response){
        //String ext = imageName.substring(imageName.indexOf(".")+1);
        //String image = TouTiaoUtil.getMD5(imageName.substring(0,imageName.indexOf(".")));
        // imageName = image+"."+ext;
        response.setContentType("image/jpg");
        try {
            StreamUtils.copy(new FileInputStream(new File(TouTiaoUtil.IMAGE_DIR+imageName)),response.getOutputStream());
        }catch (Exception e){
            e.getMessage();
        }

    }
    @RequestMapping(path = {"/uploadImage/"},method = {RequestMethod.POST})
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile file){
        try {
            String fileUrl = newsService.saveImage(file);
           //String fileUrl = qiLiuService.savaImage(file);
            if(fileUrl==null){
                return TouTiaoUtil.getJsonString(1,"图片上传失败");
            }
            return TouTiaoUtil.getJsonString(0,fileUrl);
        }catch (Exception e){
            return TouTiaoUtil.getJsonString(1,"图片上传失败");
        }
    }

}
