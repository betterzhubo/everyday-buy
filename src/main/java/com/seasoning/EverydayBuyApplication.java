package com.seasoning; /**
 * @Authot:better
 * @Date:2019/7/26 10:23
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class EverydayBuyApplication {

    public static void main(String[] args) {
        SpringApplication.run(EverydayBuyApplication.class, args);
    }
}
