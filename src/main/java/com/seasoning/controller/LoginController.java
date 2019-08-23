package com.seasoning.controller;

import com.seasoning.service.GoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Authot:better
 * @Date:2019/8/5 11:56
 */
@Controller
public class LoginController {

    @Autowired
    private GoodsTypeService goodsTypeService;

    @GetMapping("/")
    public String index(){
        return "login";
    }


    @PostMapping("/login")
    public String login(String loginPassword, Model model){
        System.out.println(loginPassword);
        model.addAttribute("test", "test123");
        return "main";
    }

    @GetMapping("/main")
    public String login(){
        return "main";
    }

    @GetMapping("/goods-table")
    public String goodsTable(Model model){

        model.addAttribute("goodsTypeList", goodsTypeService.list());
        return "goods";
    }

}
