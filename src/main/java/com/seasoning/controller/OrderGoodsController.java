package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seasoning.base.MsgConstant;
import com.seasoning.base.ResponseBaseModelT;
import com.seasoning.entity.OrderGoods;
import com.seasoning.service.OrderGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
@RestController
@Api(tags = "OrderGoodsController", description = "订单商品操作相关接口")
@RequestMapping("/order-goods")
public class OrderGoodsController {

    @Autowired
    private OrderGoodsService orderGoodsService;

    @ApiOperation(value = "根据订单id获取订单内商品信息")
    @GetMapping("/get/{orderId}")
    @ResponseBody
    public ResponseBaseModelT<List<OrderGoods>> getOrderGoodsList(@PathVariable String orderId){
        QueryWrapper<OrderGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);

        ResponseBaseModelT<List<OrderGoods>> responseBaseModelT = new ResponseBaseModelT();
        List<OrderGoods> list = orderGoodsService.list(queryWrapper);
        if(list.size() == 0){
            return new ResponseBaseModelT<>(MsgConstant.DATA_ERROR);
        }
        responseBaseModelT.setData(list);
        return responseBaseModelT;
    }

}

