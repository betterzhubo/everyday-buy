package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.ShippingAddress;
import com.seasoning.request.AddShippingAddressRequest;
import com.seasoning.request.UpdateShippingAddressRequest;
import com.seasoning.service.ShippingAddressService;
import com.seasoning.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 配送地址
 * </p>
 *
 * @author better
 * @since 2019-10-24
 */
@Slf4j
@RestController
@Api(tags = "ShippingAddressController", description = "配送地址相关接口")
@RequestMapping("/shipping-address")
public class ShippingAddressController {

    @Autowired
    private ShippingAddressService shippingAddressService;

    @ApiOperation(value = "根据用户id获取配送地址信息")
    @GetMapping("/list")
    public BaseMsg<List<ShippingAddress>> list(@PathVariable Integer userId)  {
        if(userId == null || userId <= 0){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        QueryWrapper<ShippingAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        List<ShippingAddress> list = shippingAddressService.list(queryWrapper);
        return CommonUtil.returnMsg(MsgConstant.OK, list);
    }

    @ApiOperation(value = "根据用户id修改配送地址信息")
    @PutMapping("/update")
    public BaseMsg update(@RequestBody UpdateShippingAddressRequest request){
        if(request.getUserId() <= 0 || request.getId() <= 0){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        UpdateWrapper<ShippingAddress> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", request.getUserId());
        if(StringUtils.isNotEmpty(request.getConsigneeAddress())){
            updateWrapper.set("consignee_address", request.getConsigneeAddress());
        }
        if(StringUtils.isNotEmpty(request.getConsigneeName())){
            updateWrapper.set("consignee_name", request.getConsigneeName());
        }
        if(StringUtils.isNotEmpty(request.getConsigneeMobile())){
            updateWrapper.set("consignee_mobile", request.getConsigneeMobile());
        }
        if(StringUtils.isNotEmpty(request.getConsigneeArea())){
            updateWrapper.set("consignee_area", request.getConsigneeArea());
        }
        return CommonUtil.returnMsg(MsgConstant.OK);
    }

    @ApiOperation(value = "添加配送地址信息")
    @PostMapping("/add")
    public BaseMsg add(@RequestBody @Valid AddShippingAddressRequest request, BindingResult result){

        if(result.hasErrors()){
            log.error("参数错误！", result.getAllErrors().get(0).getDefaultMessage());
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        ShippingAddress shippingAddress = new ShippingAddress();
        BeanUtils.copyProperties(request, shippingAddress);
        if(shippingAddressService.save(shippingAddress)){
            return CommonUtil.returnMsg(MsgConstant.OK);
        }
        return CommonUtil.returnMsg(MsgConstant.SQL_ERROR);
    }

    @ApiOperation(value = "删除配送地址信息")
    @DeleteMapping("/delete/{id}")
    public BaseMsg delete(@PathVariable int id){
        if(id <= 0){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        return CommonUtil.returnMsg(MsgConstant.OK, shippingAddressService.removeById(id));
    }

}