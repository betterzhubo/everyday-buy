package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.*;
import com.seasoning.request.AddOrderByShoppingCartRequest;
import com.seasoning.request.AddOrderRequest;
import com.seasoning.request.OrderSearchRequest;
import com.seasoning.request.UpdateOrderRequest;
import com.seasoning.service.*;
import com.seasoning.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
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
@Api(tags = "OrderController", description = "订单操作相关接口")
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private GoodsService goodsService;

    @ApiOperation(value = "根据订单id获取订单内容")
    @PostMapping("/page")
    public BaseMsg<IPage<Order>> page(@RequestBody OrderSearchRequest request, HttpSession session)  {

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
        User user = CommonUtil.getUserBySession(session);
        if(user != null){
            queryWrapper.eq("user_id", user.getId());
        }
        if(null != request.getOrderStatus()){
            queryWrapper.eq("order_status", request.getOrderStatus());
        }
        queryWrapper.orderByDesc("create_time");

        Page<Order> page = new Page<>(request.getCurrent()+1, request.getPageSize());
        IPage<Order> resultPage = orderService.page(page, queryWrapper);
        return CommonUtil.returnMsg(MsgConstant.OK, resultPage);
    }

    @ApiOperation(value = "根据订单id修改订单内容, 可修改内容")
    @PutMapping("/update")
    public BaseMsg update(@RequestBody UpdateOrderRequest request, HttpSession session)  {
        if(null == request.getId()){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", request.getId());
        User user = CommonUtil.getUserBySession(session);
        if(user != null){
            updateWrapper.eq("user_id", user.getId());
        }

        synchronized (user){
            if(request.getConsigneeName() != null){
                updateWrapper.set("consignee_name", request.getConsigneeName());
            }
            if(request.getConsigneeMobile() != null){
                updateWrapper.set("consignee_mobile", request.getConsigneeMobile());
            }
            if(request.getConsigneeAddress() != null){
                updateWrapper.set("consignee_address", request.getConsigneeAddress());
            }
            if(request.getOrderStatus() != null){
                updateWrapper.set("order_status", request.getOrderStatus());
            }
            return CommonUtil.returnMsg(MsgConstant.OK, orderService.update(updateWrapper));
        }
    }


    @ApiOperation(value = "添加订单")
    @PostMapping("/add")
    public BaseMsg add(@RequestBody @Valid AddOrderRequest request, BindingResult result, HttpSession session){
        if(result.hasErrors()){
            log.error("参数错误！", result.getAllErrors().get(0).getDefaultMessage());
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        User user = CommonUtil.getUserBySession(session);
        if(user == null){
            return CommonUtil.returnMsg(MsgConstant.LOGIN_OVERTIME);
        }
        synchronized (user){

            Order order = new Order();
            BeanUtils.copyProperties(request, order);
            order.setUserId(user.getId());
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

    @ApiOperation(value = "通过购物车添加订单")
    @PostMapping("/addByShoppingCart")
    public BaseMsg addByShoppingCart(@RequestBody @Valid AddOrderByShoppingCartRequest request, BindingResult result, HttpSession session){
        if(result.hasErrors()){
            log.error("参数错误！", result.getAllErrors().get(0).getDefaultMessage());
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        User user = CommonUtil.getUserBySession(session);
        if(user == null){
            return CommonUtil.returnMsg(MsgConstant.LOGIN_OVERTIME);
        }
        synchronized (user){

            Order order = new Order();
            BeanUtils.copyProperties(request, order);
            order.setUserId(user.getId());
            int id = orderService.generateOrderId();
            order.setId(id);
            if(!orderService.save(order)){
                return CommonUtil.returnMsg(MsgConstant.SQL_ERROR, "订单保存失败！");
            }

            QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", session.getId());
            queryWrapper.in("id", request.getShoppingCartIds());
            List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
            List<Integer> goodsIds = new ArrayList<>();
            List<OrderGoods> orderGoodsList = new ArrayList<>();
            for(ShoppingCart shoppingCart : list){
                goodsIds.add(shoppingCart.getGoodsId());
                OrderGoods orderGoods = new OrderGoods();
                BeanUtils.copyProperties(shoppingCart, orderGoods);

                orderGoods.setOrderId(id);
                orderGoodsList.add(orderGoods);
            }
            Collection<Goods> goodsList = goodsService.listByIds(goodsIds);

            for(OrderGoods orderGoods : orderGoodsList){
                for(Goods goods : goodsList){
                    if(!orderGoods.getGoodsId().equals(goods.getId())){
                        continue;
                    }
                    BeanUtils.copyProperties(goods, orderGoods);
                }
            }

            if(!orderGoodsService.saveBatch(orderGoodsList)){
                return CommonUtil.returnMsg(MsgConstant.SQL_ERROR, "订单商品保存失败！");
            }
            return CommonUtil.returnMsg(MsgConstant.OK, id);
        }
    }

    @ApiOperation(value = "根据订单id获取订单内容")
    @GetMapping("/get/{orderId}")
    public BaseMsg<Order> get(@PathVariable int id, HttpSession session){
        if(id <= 0){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        User user = CommonUtil.getUserBySession(session);
        if(user == null){
            wrapper.eq("user_id", user.getId());
        }
        Order order = orderService.getOne(wrapper);
        if(1 == order.getOrderStatus()){
            Duration duration = Duration.between(order.getCreateTime(), LocalDateTime.now());
            if(duration.getSeconds() > 600){
                UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
                updateWrapper .eq("id",id).set("order_status", 5);
                orderService.update(updateWrapper);
                order.setOrderStatus(0);
            }
        }
        return CommonUtil.returnMsg(MsgConstant.OK, order);
    }

}

