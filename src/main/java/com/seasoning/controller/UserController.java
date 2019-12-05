package com.seasoning.controller;


import com.alibaba.fastjson.JSON;
import com.seasoning.entity.User;
import com.seasoning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author better
 * @since 2019-07-26
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUserList")
    public String getUserList(){
        List<User> list = userService.list();
        System.out.println(JSON.toJSONString(list));
        return "success";
    }

    public String login(){

        return "";
    }


}

