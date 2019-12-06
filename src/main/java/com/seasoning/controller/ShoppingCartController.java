package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.ShopingCartGoods;
import com.seasoning.entity.ShoppingCart;
import com.seasoning.request.AddShoppingCartRequest;
import com.seasoning.service.ShoppingCartService;
import com.seasoning.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
@Api(tags = "ShoppingCartController", description = "购物车相关接口")
@RequestMapping("/shopping-cart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @ApiOperation(value = "根据用户id获取购物车内商品信息")
    @GetMapping("/list/{userId}")
    public BaseMsg<List<ShopingCartGoods>> list(@PathVariable int userId){
        if(userId <= 0){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        List<ShopingCartGoods> list = shoppingCartService.getListByUserId(userId);
        return CommonUtil.returnMsg(MsgConstant.OK, list);
    }

    @ApiOperation(value = "根据唯一id删除购物车内商品")
    @DeleteMapping("/delete/{id}")
    public BaseMsg delete(@PathVariable int id){
        if(id <= 0){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        shoppingCartService.removeById(id);
        return CommonUtil.returnMsg(MsgConstant.OK);
    }

    @ApiOperation(value = "根据唯一id减去购物车内商品(默认减一，商品数为1时做删除操作)")
    @GetMapping("/decrease/{id}")
    public BaseMsg decrease(@PathVariable int id){
        if(id <= 0){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        ShoppingCart shoppingCart = shoppingCartService.getById(id);
        if(shoppingCart == null){
            return CommonUtil.returnMsg(MsgConstant.DATA_ERROR);
        }
        if(shoppingCart.getGoodsCount() == 1){
            shoppingCartService.removeById(id);
            return CommonUtil.returnMsg(MsgConstant.OK);
        }
        UpdateWrapper<ShoppingCart> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("shopping_cart", shoppingCart.getGoodsCount() - 1);
        return CommonUtil.returnMsg(MsgConstant.OK);
    }

    @ApiOperation(value = "添加商品到购物车")
    @PostMapping("/add")
    public BaseMsg add(@RequestBody @Valid AddShoppingCartRequest request, BindingResult result){
        if(result.hasErrors()){
            log.error("参数错误！", result.getAllErrors().get(0).getDefaultMessage());
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(request, shoppingCart);
        shoppingCartService.save(shoppingCart);
        return CommonUtil.returnMsg(MsgConstant.OK);
    }
}

