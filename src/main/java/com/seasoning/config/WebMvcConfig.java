package com.seasoning.config;

import com.seasoning.base.ContextConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 相当于web.xml配置文件
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ContextConstant contextConstant;

    @Autowired
    private LoginInterceptor loginInterceptor;
    /**
     * 添加统一处理token的拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludeList = new ArrayList<>();
        excludeList.add("/");
        excludeList.add("/user/login");
        excludeList.add("/login");
        excludeList.add("/swagger-ui.html");
        excludeList.add("/webjars/**");
        excludeList.add("/region/**");
        excludeList.add("/swagger-resources/**");
        excludeList.add("/static/**");
        excludeList.add("/v2/api-docs/**");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(excludeList);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("file:" + contextConstant.getResourcesPath());
    }

}
