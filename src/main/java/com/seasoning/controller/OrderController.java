package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.*;
import com.seasoning.request.*;
import com.seasoning.request.tem.PaymentParam;
import com.seasoning.response.OrderListResponse;
import com.seasoning.service.*;
import com.seasoning.util.CommonUtil;
import com.seasoning.util.MD5Util;
import com.seasoning.util.WechatApiUtil;
import com.sun.org.apache.xpath.internal.operations.Or;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private ViewOrderGoodsService viewOrderGoodsService;
    @Autowired
    private WechatApiUtil wechatApiUtil;

    @ApiOperation(value = "获取订单详情接口")
    @GetMapping("/detail/{id}")
    public BaseMsg<OrderListResponse> detail(@PathVariable Integer id, HttpSession session) {

        OrderListResponse response = null;
        QueryWrapper<ViewOrderGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        List<ViewOrderGoods> list = viewOrderGoodsService.list(queryWrapper);
        for(ViewOrderGoods orderGoods : list) {
            if (response == null) {
                response = new OrderListResponse();
                response.setOrderGoodsList(new ArrayList<>());
                response.setCreateTime(orderGoods.getCreateTime());
                response.setOrderId(orderGoods.getId());
                response.setOrderStatus(orderGoods.getOrderStatus());
                response.setConsigneeAddress(orderGoods.getConsigneeAddress());
            }
            OrderListResponse.OrderGoodsResponse orderGoodsResponse = new OrderListResponse().new OrderGoodsResponse();
            response.getOrderGoodsList().add(orderGoodsResponse);
            orderGoodsResponse.setGoodsCount(orderGoods.getGoodsCount());
            orderGoodsResponse.setGoodsPrice(orderGoods.getGoodsPrice());
            orderGoodsResponse.setGoodsName(orderGoods.getGoodsName());
            orderGoodsResponse.setShowImage(orderGoods.getShowImage());
            orderGoodsResponse.setGoodsId(orderGoods.getGoodsId());
            Integer goodsCount = response.getTotalCount() + orderGoods.getGoodsCount();
            response.setTotalCount(goodsCount);
            BigDecimal totalPrice = response.getTotalPrice().add(orderGoods.getGoodsPrice());
            response.setTotalPrice(totalPrice);
        }

        if(response != null && 1 == response.getOrderStatus()){
            Duration duration = Duration.between(response.getCreateTime(), LocalDateTime.now());
            if(duration.getSeconds() > 600){
                UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id",id).set("order_status", 5);
                orderService.update(updateWrapper);
                response.setOrderStatus(5);
            }
        }
        return CommonUtil.returnMsg(MsgConstant.OK, response);
    }

    @ApiOperation(value = "获取订单列表接口")
    @GetMapping("/list/{status}")
    public BaseMsg<List<OrderListResponse>> list(@PathVariable Integer status, HttpSession session) {

        QueryWrapper<ViewOrderGoods> queryWrapper = new QueryWrapper<>();

        User user = CommonUtil.getUserBySession(session);
        if(user != null){
            queryWrapper.eq("user_id", user.getId());
        }
        if(null != status && -1 != status){
            queryWrapper.eq("order_status", status);
        }
        queryWrapper.orderByDesc("create_time");

        List<ViewOrderGoods> list = viewOrderGoodsService.list(queryWrapper);

        Map<Integer, OrderListResponse> map = new HashMap<>();
        List<OrderListResponse> responseList = new ArrayList();
        List<Integer> cancelIds = new ArrayList<>();
        for(ViewOrderGoods orderGoods : list){

            Duration duration = Duration.between(orderGoods.getCreateTime(), LocalDateTime.now());
            if(duration.getSeconds() > 600){
                cancelIds.add(orderGoods.getId());
                if(status == 1){
                    continue;
                }
                orderGoods.setOrderStatus(5);
            }
            OrderListResponse response = map.get(orderGoods.getId());
            if(response == null){
                response = new OrderListResponse();
                response.setOrderGoodsList(new ArrayList<>());
                response.setCreateTime(orderGoods.getCreateTime());
                response.setOrderId(orderGoods.getId());
                response.setOrderStatus(orderGoods.getOrderStatus());
                response.setConsigneeAddress(orderGoods.getConsigneeAddress());
                map.put(orderGoods.getId(), response);
            }
            OrderListResponse.OrderGoodsResponse orderGoodsResponse = new OrderListResponse().new OrderGoodsResponse();
            response.getOrderGoodsList().add(orderGoodsResponse);
            orderGoodsResponse.setGoodsName(orderGoods.getGoodsName());
            orderGoodsResponse.setShowImage(orderGoods.getShowImage());
            orderGoodsResponse.setGoodsId(orderGoods.getGoodsId());
            Integer goodsCount = response.getTotalCount() + orderGoods.getGoodsCount();
            response.setTotalCount(goodsCount);
            BigDecimal totalPrice = response.getTotalPrice().add(orderGoods.getGoodsPrice());
            response.setTotalPrice(totalPrice);

            responseList.add(response);
        }

        if(cancelIds.size() > 0){
            UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",cancelIds).set("order_status", 5);
            orderService.update(updateWrapper);
        }
        return CommonUtil.returnMsg(MsgConstant.OK, responseList);
    }

    @ApiOperation(value = "获取订单列表（后台用）")
    @PostMapping("/page")
    public BaseMsg<IPage<Order>> page(@RequestBody OrderSearchRequest request, HttpSession session) {

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
        if(null != request.getOrderStatus() && -1 != request.getOrderStatus()){
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
            order.setOrderStatus(1);
            int id = orderService.generateOrderId();
            order.setId(id);
            if(!orderService.save(order)){
                return CommonUtil.returnMsg(MsgConstant.SQL_ERROR, "订单保存失败！");
            }

            List<Integer> goodsIds = new ArrayList<>();
            List<OrderGoods> list = new ArrayList<>();
            for(AddOrderRequest.OrderGoodsRequest orderGoodsRequest : request.getOrderGoods()){
                goodsIds.add(orderGoodsRequest.getGoodsId());
                OrderGoods orderGoods = new OrderGoods();
                BeanUtils.copyProperties(orderGoodsRequest, orderGoods);
                orderGoods.setOrderId(id);
                list.add(orderGoods);
            }
            Collection<Goods> goodsList = goodsService.listByIds(goodsIds);
            for(OrderGoods orderGoods : list){
                for(Goods goods : goodsList){
                    if(!orderGoods.getGoodsId().equals(goods.getId())){
                        continue;
                    }
                    BeanUtils.copyProperties(goods, orderGoods);
                    orderGoods.setGoodsPrice(goods.getPrice());
                }
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
            log.error("参数错误！"+ result.getAllErrors().get(0).getDefaultMessage());
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
            order.setOrderStatus(1);
            int id = orderService.generateOrderId();
            order.setId(id);
            if(!orderService.save(order)){
                return CommonUtil.returnMsg(MsgConstant.SQL_ERROR, "订单保存失败！");
            }

            QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", user.getId());
            queryWrapper.in("id", request.getShoppingCartIds());
            List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
            if(list.size() == 0){
                return CommonUtil.returnMsg(MsgConstant.SHOPPINGCART_NULL);
            }
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
                    orderGoods.setGoodsPrice(goods.getPrice());
                }
            }
            if(!orderGoodsService.saveBatch(orderGoodsList)){
                return CommonUtil.returnMsg(MsgConstant.SQL_ERROR, "订单商品保存失败！");
            }
            shoppingCartService.removeByIds(request.getShoppingCartIds());
            return CommonUtil.returnMsg(MsgConstant.OK, id);
        }
    }

    @ApiOperation(value = "根据订单id获取订单内容")
    @GetMapping("/get/{id}")
    public BaseMsg<Order> get(@PathVariable int id, HttpSession session){
        if(id <= 0){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        User user = CommonUtil.getUserBySession(session);
        if(user != null){
            wrapper.eq("user_id", user.getId());
        }
        Order order = orderService.getOne(wrapper);
        if(1 == order.getOrderStatus()){
            Duration duration = Duration.between(order.getCreateTime(), LocalDateTime.now());
            if(duration.getSeconds() > 600){
                UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id",id).set("order_status", 5);
                orderService.update(updateWrapper);
                order.setOrderStatus(5);
            }
        }
        return CommonUtil.returnMsg(MsgConstant.OK, order);
    }

    @ApiOperation(value = "订单支付")
    @PostMapping("/pay")
    public BaseMsg<PaymentParam> payOrder(@RequestBody PayOrderRequest request, HttpSession session){

        Integer orderId = request.getOrderId();
        if(orderId == null || orderId == 0){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        User user = CommonUtil.getUserBySession(session);
        if(user == null){
            return CommonUtil.returnMsg(MsgConstant.LOGIN_OVERTIME);
        }
        synchronized (orderId){

            Order order = orderService.getById(request.getOrderId());
            if(order == null){
                return CommonUtil.returnMsg(MsgConstant.DATA_ERROR);
            }
            if(1 != order.getOrderStatus()){
                return CommonUtil.returnMsg(MsgConstant.ORDER_STATUS);
            }
            if(StringUtils.isBlank(order.getPrepayId())){

                List<OrderGoods> list = orderGoodsService.list(new QueryWrapper<OrderGoods>().eq("order_id", orderId));
                BigDecimal totalFee = new BigDecimal(0);
                for(OrderGoods orderGoods : list){
                    totalFee = totalFee.add(orderGoods.getGoodsPrice().multiply(new BigDecimal(orderGoods.getGoodsCount())));
                }
                String totalFeeStr = totalFee.multiply(new BigDecimal(100)).intValue()+ "";
                String prepayId = wechatApiUtil.placeOrder(orderId, totalFeeStr, user.getOpenid());
                order.setPrepayId(prepayId);
                UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id", order.getId());
                updateWrapper.set("prepay_id", prepayId);
                orderService.update(updateWrapper);
            }

            long rand = RandomUtils.nextInt(100,999);
            String nonce_str = DateFormatUtils.format(new Date(), "yyyyMMdd")+rand;
            String timestamp = System.currentTimeMillis() / 1000 + "";
            PaymentParam paymentParam = new PaymentParam();
            paymentParam.setNonceStr(nonce_str);
            paymentParam.setTimeStamp(timestamp);
            paymentParam.setPackageParam("prepay_id="+order.getPrepayId());
            String sign = wechatApiUtil.getSign(nonce_str, timestamp, order.getPrepayId());
            paymentParam.setPaySign(sign);
            if(paymentParam == null){
                return CommonUtil.returnMsg(MsgConstant.PAY_FAIL);
            }
            return CommonUtil.returnMsg(MsgConstant.OK, paymentParam);
        }
    }

    @ApiOperation(value = "订单支付通知")
    @PostMapping("/notice")
    public String payOrder(HttpServletRequest request){

        String requestParam = null;
        try(InputStream is = request.getInputStream()){
            StringBuilder sb = new StringBuilder();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1;)
            {
                sb.append(new String(b, 0, n));
            }
            requestParam = sb.toString();
        }
        catch (IOException e) {
            log.error("", e);
        }
        Map<String, String> result = CommonUtil.getXmlBodyContext(requestParam);

        if(request == null){
            return "<xml><return_code>FAIL</return_code></xml>";
        }
        if(StringUtils.equals(result.get("return_code"), "FAIL")){
            return "<xml><return_code>FAIL</return_code></xml>";
        }
        if(StringUtils.equals(result.get("result_code"), "FAIL")){
            return "<xml><return_code>FAIL</return_code></xml>";
        }

        Integer orderId = Integer.valueOf(result.get("out_trade_no"));
        synchronized (orderId){
            Order order = orderService.getById(orderId);
            if(order == null || order.getOrderStatus() != 1){
                return "<xml><return_code>FAIL</return_code></xml>";
            }
            UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", orderId);
            updateWrapper.set("order_status", 2);
            if(!orderService.update(updateWrapper)){
                return "<xml><return_code>FAIL</return_code></xml>";
            }
        }
        String respons = "<xml>"
                        +"<return_code>SUCCESS</return_code>"
                        +"<return_msg>OK</return_msg>"
                        +"</xml>";
        return respons;
    }
}

