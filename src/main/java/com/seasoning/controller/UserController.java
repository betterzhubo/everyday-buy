package com.seasoning.controller;


import com.alibaba.fastjson.JSON;
import com.seasoning.entity.User;
import com.seasoning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String getUserList(){
        List<User> list = userService.list();
        System.out.println(JSON.toJSONString(list));
        return "success";
    }


}

