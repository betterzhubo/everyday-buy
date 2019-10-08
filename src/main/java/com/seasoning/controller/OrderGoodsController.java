package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.OrderGoods;
import com.seasoning.service.OrderGoodsService;
import com.seasoning.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
@RestController
@RequestMapping("/order-goods")
public class OrderGoodsController {

    @Autowired
    private OrderGoodsService orderGoodsService;

    @GetMapping("/get/{orderId}")
    @ResponseBody
    public BaseMsg getOrderGoodsList(@PathVariable String orderId){
        QueryWrapper<OrderGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);

        return CommonUtil.returnMsg(MsgConstant.OK, orderGoodsService.list(queryWrapper));
    }

}

