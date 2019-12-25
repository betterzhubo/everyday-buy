package com.seasoning.base;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Authot:better
 * @Date:2019/8/15 16:34
 */
@Component
@Data
public class ContextConstant {

    @Value("${everyday-buy.resources-path}")
    private String resourcesPath;

    private String imagePath;

    @PostConstruct
    private void init(){
        this.imagePath = resourcesPath + "/image/";
    }

}
