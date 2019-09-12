package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.Order;
import com.seasoning.request.OrderSearchRequest;
import com.seasoning.service.OrderService;
import com.seasoning.service.UserService;
import com.seasoning.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @PostMapping("/page")
    public BaseMsg page(@RequestBody OrderSearchRequest request)  {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();

        if(StringUtils.isNotBlank(request.getOrderId())){
            queryWrapper.like("order_id", request.getOrderId());
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

}

