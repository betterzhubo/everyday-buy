/**
 * @Authot:better
 * @Date:2019/7/26 10:23
 */
package com.seasoning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@ServletComponentScan
public class EverydayBuyApplication {

    public static void main(String[] args) {
        SpringApplication.run(EverydayBuyApplication.class, args);
    }
}
