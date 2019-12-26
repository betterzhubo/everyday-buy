package com.seasoning.controller;

import com.seasoning.base.CommonConstant;
import com.seasoning.service.GoodsTypeService;
import com.seasoning.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Authot:better
 * @Date:2019/8/5 11:56
 */
@Controller
public class RouterController {

    @Autowired
    private GoodsTypeService goodsTypeService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(){
        return "login";
    }


    @PostMapping("/login")
    public String login(HttpServletRequest request, String loginPassword, Model model){
        if(!StringUtils.equals("123123", loginPassword)){
            model.addAttribute("tips","密码错误!");
            return "login";
        }
        HttpSession session = request.getSession();
        session.setAttribute(CommonConstant.USER_MANAGEMENT, "1");
        return "main";
    }

    @GetMapping("/main")
    public String main(){
        return "main";
    }

    @GetMapping("/goods-table")
    public String goodsTable(Model model){

        model.addAttribute("goodsTypeList", goodsTypeService.list());
        return "goods";
    }

    @GetMapping("/goods-type")
    public String goodsType(){

        return "goods-type";
    }

    @GetMapping("/order")
    public String orderTable(Model model){
        model.addAttribute("userList", userService.list());
        return "order";
    }
}
