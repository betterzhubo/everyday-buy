package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.Order;
import com.seasoning.request.OrderSearchRequest;
import com.seasoning.service.OrderService;
import com.seasoning.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/page")
    public BaseMsg page(@RequestBody OrderSearchRequest request)  {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        Page<Order> page = new Page<>();

        IPage<Order> resultPage = orderService.page(page, queryWrapper);
        return CommonUtil.returnMsg(MsgConstant.OK, resultPage);
    }

}

