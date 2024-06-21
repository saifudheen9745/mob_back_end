package com.shop.mob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class AppConfig implements  WebMvcConfigurer{

    @Autowired
    ApiInterceptor apiInterceptor;

    @Override
    public void addInterceptors (InterceptorRegistry registry){
        registry.addInterceptor(apiInterceptor).addPathPatterns("/api/v1/product");
    }
}
