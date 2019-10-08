package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.Order;
import com.seasoning.entity.OrderGoods;
import com.seasoning.request.AddOrderRequest;
import com.seasoning.request.OrderSearchRequest;
import com.seasoning.service.OrderGoodsService;
import com.seasoning.service.OrderService;
import com.seasoning.service.UserService;
import com.seasoning.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private UserService userService;

    @PostMapping("/page")
    public BaseMsg page(@RequestBody OrderSearchRequest request)  {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();

        if(null != request.getId()){
            queryWrapper.eq("id", request.getId());
        }
        if(StringUtils.isNotBlank(request.getUserName())){
            List<Integer> list = userService.getIdByName(request.getUserName());
            if(list.size() > 0){
                queryWrapper.in("user_id", list);
            }
        }
        if(null != request.getOrderStatus()){
            queryWrapper.eq("order_status", request.getOrderStatus());
        }

        Page<Order> page = new Page<>();
        IPage<Order> resultPage = orderService.page(page, queryWrapper);
        return CommonUtil.returnMsg(MsgConstant.OK, resultPage);
    }
    @PutMapping("/update")
    public BaseMsg update(@RequestBody Order order)  {
        if(null == order.getId()){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", order.getId());

        if(order.getConsigneeName() != null){
            updateWrapper.set("consignee_name", order.getConsigneeName());
        }
        if(order.getConsigneeMobile() != null){
            updateWrapper.set("consignee_mobile", order.getConsigneeMobile());
        }
        if(order.getConsigneeAddress() != null){
            updateWrapper.set("consignee_address", order.getConsigneeAddress());
        }
        if(order.getOrderStatus() != null){
            updateWrapper.set("order_status", order.getOrderStatus());
        }
        return CommonUtil.returnMsg(MsgConstant.OK, orderService.update(updateWrapper));
    }


    @PostMapping("/add")
    public BaseMsg add(@RequestBody @Valid AddOrderRequest request, BindingResult result){
        if(result.hasErrors()){
            log.error("参数错误！", result.getAllErrors().get(0));
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        Order order = new Order();
        BeanUtils.copyProperties(request, order);
        int id = orderService.generateOrderId();
        order.setId(id);
        if(!orderService.save(order)){
            return CommonUtil.returnMsg(MsgConstant.SQL_ERROR, "订单保存失败！");
        }

        List<OrderGoods> list = new ArrayList<>();
        for(AddOrderRequest.OrderGoodsRequest orderGoodsRequest : request.getOrderGoods()){
            OrderGoods orderGoods = new OrderGoods();
            BeanUtils.copyProperties(orderGoodsRequest, orderGoods);
            orderGoods.setOrderId(id);
            list.add(orderGoods);
        }
        if(!orderGoodsService.saveBatch(list)){
            return CommonUtil.returnMsg(MsgConstant.SQL_ERROR, "订单商品保存失败！");
        }
        return CommonUtil.returnMsg(MsgConstant.OK, id);
    }

}

