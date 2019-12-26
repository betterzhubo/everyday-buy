package com.seasoning.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.CommonConstant;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.User;
import com.seasoning.request.UserLoginRequest;
import com.seasoning.service.UserService;
import com.seasoning.util.CommonUtil;
import com.seasoning.util.WechatApiUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author better
 * @since 2019-07-26
 */
@Slf4j
@Api(tags = "UserController", description = "用户操作相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WechatApiUtil wechatApiUtil;

    @GetMapping("/getUserList")
    public String getUserList(){
        List<User> list = userService.list();
        System.out.println(JSON.toJSONString(list));
        return "success";
    }

    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    public BaseMsg login(@RequestBody @Valid UserLoginRequest request, BindingResult result, HttpSession session){
        if(result.hasErrors()){
            log.error("参数错误！", result.getAllErrors().get(0).toString());
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }

        String resultStr = wechatApiUtil.code2Session(request.getCode());
        if(StringUtils.isEmpty(resultStr)){
            return CommonUtil.returnMsg(MsgConstant.HTTP_REQUEST_ERROR);
        }
        JSONObject jsonObject = JSONObject.parseObject(resultStr);
        String errorCode = jsonObject.getString("errcode");
        if(!StringUtils.isEmpty(errorCode)){
            return CommonUtil.returnMsg(MsgConstant.HTTP_REQUEST_ERROR);
        }
        String openid = jsonObject.getString("openid");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        User user = userService.getOne(queryWrapper, false);
        if(user == null){
            user = new User();
            user.setName(request.getName());
            user.setPhone("");
            user.setOpenid(openid);
            userService.save(user);
        }
        session.setAttribute(CommonConstant.USER_INFO, user);

        return CommonUtil.returnMsg(MsgConstant.OK);
    }
}