package com.nowcoder.configration;


import com.nowcoder.Intercerpet.LoginRequireInterceptor;
import com.nowcoder.Intercerpet.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Component
public class TouTiaoWebConfigration extends WebMvcConfigurerAdapter {

    @Autowired
    MyInterceptor myInterceptor;
    @Autowired
    LoginRequireInterceptor loginRequireInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor);
        registry.addInterceptor(loginRequireInterceptor).addPathPatterns("/setting*");
        super.addInterceptors(registry);
    }
}
