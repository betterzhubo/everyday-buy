package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.ShopingCartGoods;
import com.seasoning.entity.ShoppingCart;
import com.seasoning.entity.User;
import com.seasoning.request.AddShoppingCartRequest;
import com.seasoning.request.PageRequest;
import com.seasoning.service.ShoppingCartService;
import com.seasoning.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
@RestController
@Api(tags = "ShoppingCartController", description = "购物车相关接口")
@RequestMapping("/shopping-cart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @ApiOperation(value = "根据用户id获取购物车内商品信息")
    @PostMapping("/list")
    public BaseMsg<Page<ShopingCartGoods>> list(@RequestBody PageRequest request, HttpSession session){
        User user = CommonUtil.getUserBySession(session);
        Page<ShopingCartGoods> list = shoppingCartService.getListByUserId(new Page<>(request.getCurrent(), request.getPageSize()), user.getId());
        return CommonUtil.returnMsg(MsgConstant.OK, list);
    }

    @ApiOperation(value = "根据唯一id删除购物车内商品")
    @DeleteMapping("/delete/{id}")
    public BaseMsg delete(@PathVariable int id, HttpSession session){
        if(id <= 0){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        User user = CommonUtil.getUserBySession(session);

        UpdateWrapper<ShoppingCart> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", user.getId());
        wrapper.eq("id", id);
        shoppingCartService.remove(wrapper);
        return CommonUtil.returnMsg(MsgConstant.OK);
    }

    @ApiOperation(value = "根据唯一id减去或增加购物车内商品(减少时当商品数为1时做删除操作)")
    @GetMapping("/decrease/{id}/{operate}")
    public BaseMsg decrease(@PathVariable int id, @PathVariable @ApiParam(value = "操作1:增加，-1:减少(只允许这两个值)") int operate, HttpSession session){
        if(id <= 0){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        if(operate != 1 && operate != -1){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        ShoppingCart shoppingCart = shoppingCartService.getById(id);
        if(shoppingCart == null){
            return CommonUtil.returnMsg(MsgConstant.DATA_ERROR);
        }
        if(operate == -1 && shoppingCart.getGoodsCount() == 1){
            shoppingCartService.removeById(id);
            return CommonUtil.returnMsg(MsgConstant.OK);
        }
        User user = CommonUtil.getUserBySession(session);
        UpdateWrapper<ShoppingCart> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        updateWrapper.set("goods_count", shoppingCart.getGoodsCount() + operate);
        shoppingCartService.update(updateWrapper);
        return CommonUtil.returnMsg(MsgConstant.OK);
    }

    @ApiOperation(value = "添加商品到购物车")
    @PostMapping("/add")
    public BaseMsg add(@RequestBody @Valid AddShoppingCartRequest request, BindingResult result, HttpSession session){
        if(result.hasErrors()){
            log.error("参数错误！", result.getAllErrors().get(0).getDefaultMessage());
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        User user = CommonUtil.getUserBySession(session);
        synchronized (user){
            QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", user.getId());
            queryWrapper.eq("goods_id", request.getGoodsId());
            ShoppingCart shoppingCartDb = shoppingCartService.getOne(queryWrapper, false);
            if(shoppingCartDb == null){
                ShoppingCart shoppingCart = new ShoppingCart();
                BeanUtils.copyProperties(request, shoppingCart);
                shoppingCart.setUserId(user.getId());
                shoppingCartService.save(shoppingCart);
            }else{
                UpdateWrapper<ShoppingCart> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("user_id", user.getId());
                updateWrapper.eq("goods_id", request.getGoodsId());
                updateWrapper.set("goods_count", shoppingCartDb.getGoodsCount()+request.getGoodsCount());
                shoppingCartService.update(updateWrapper);
            }
            return CommonUtil.returnMsg(MsgConstant.OK);
        }
    }

    @ApiOperation(value = "获取购物车内商品总数量")
    @GetMapping("/sum")
    public BaseMsg sum(HttpSession session){

        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        User user = CommonUtil.getUserBySession(session);
        wrapper.eq("user_id", user.getId());
//        wrapper.eq("user_id", 1);
        wrapper.select("sum(goods_count) total");
        wrapper.groupBy("user_id");
        Map<String, Object> map = shoppingCartService.getMap(wrapper);
        if(map == null || null == map.get("total")){
            return CommonUtil.returnMsg(MsgConstant.OK, 0);
        }
        BigDecimal bd = (BigDecimal) map.get("total");
        return CommonUtil.returnMsg(MsgConstant.OK, bd.intValue());
    }
}

