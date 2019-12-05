package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.ShopingCartGoods;
import com.seasoning.entity.ShoppingCart;
import com.seasoning.request.AddShoppingCartRequest;
import com.seasoning.service.ShoppingCartService;
import com.seasoning.util.CommonUtil;
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
@RequestMapping("/shopping-cart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list/{userId}")
    public BaseMsg list(@PathVariable int userId){
        if(userId <= 0){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        List<ShopingCartGoods> list = shoppingCartService.getListByUserId(userId);
        return CommonUtil.returnMsg(MsgConstant.OK, list);
    }

    @DeleteMapping("/delete/{id}")
    public BaseMsg delete(@PathVariable int id){
        if(id <= 0){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        shoppingCartService.removeById(id);
        return CommonUtil.returnMsg(MsgConstant.OK);
    }

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

