package com.nowcoder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController {
    @RequestMapping(value = {"/","/index"})
    @ResponseBody
    public String index(){
        return "index";
    }

    @RequestMapping(value = {"/vm"})
    public String news(){
        return "news";
    }

    @RequestMapping(value = {"/setting"})
    @ResponseBody
    public String setting(){
        return "Setting";
    }
    //图片上传测试
    @RequestMapping(value = {"/fileupload"})
    public String upload(){
        return "fileUpload";
    }
}
