package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.Goods;
import com.seasoning.request.GoodsAddRequest;
import com.seasoning.request.GoodsSearchRequest;
import com.seasoning.service.GoodsService;
import com.seasoning.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author better
 * @since 2019-07-31
 */
@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/add")
    public BaseMsg add(@RequestBody @Valid GoodsAddRequest request, BindingResult result) {
        if(result.hasErrors()){
            log.error("参数错误！", result.getAllErrors().get(0));
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }

        Goods goods = new Goods();
        BeanUtils.copyProperties(request, goods);
        if(goodsService.save(goods)){

            return CommonUtil.returnMsg(MsgConstant.OK, goods.getId());
        }
        return CommonUtil.returnMsg(MsgConstant.PROGRAM_ERROR);
    }

    @PutMapping("/update")
    public BaseMsg update(@RequestBody Goods goods) {
        if(null == goods.getId()){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", goods.getId());
        if(goods.getGoodsName() != null){
            updateWrapper.set("goods_name", goods.getGoodsName());
        }
        if(goods.getSelling() != null){
            updateWrapper.set("selling", goods.getSelling());
        }
        if(goods.getDetailImage() != null){
            updateWrapper.set("detail_image", goods.getDetailImage());
        }
        if(goods.getPrice() != null){
            updateWrapper.set("price", goods.getPrice());
        }
        if(goods.getShowImage() != null){
            updateWrapper.set("show_image", goods.getShowImage());
        }
        if(goods.getPurpose() != null){
            updateWrapper.set("purpose", goods.getPurpose());
        }
        if(goods.getGoodsTypeId() != null){
            updateWrapper.set("goods_type_id", goods.getGoodsTypeId());
        }
        return CommonUtil.returnMsg(MsgConstant.OK,  goodsService.update(updateWrapper));
    }

    @DeleteMapping("/delete/{goodsId}")
    public BaseMsg delete(@PathVariable Integer goodsId)  {

        return CommonUtil.returnMsg(MsgConstant.OK,  goodsService.removeById(goodsId));
    }

    @PostMapping("/page")
    public BaseMsg page(@RequestBody GoodsSearchRequest request)  {

        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(request.getGoodsName())){
            queryWrapper.like("goods_name", request.getGoodsName());
        }
        if(request.getGoodsTypeId() != null && request.getGoodsTypeId() > 0){
            queryWrapper.eq("goods_type_id", request.getGoodsTypeId());
        }
        if(request.getMinPrice() != null ){
            queryWrapper.ge("price", request.getMinPrice());
        }
        if(request.getMaxPrice() != null){
            queryWrapper.le("price", request.getMaxPrice());
        }
        if(request.getPurpose() != null && request.getPurpose() > -1){
            queryWrapper.eq("purpose", request.getPurpose());
        }
        if(request.getSelling() != null){
            queryWrapper.eq("selling", request.getSelling());
        }

        Page<Goods> page = new Page<>(request.getCurrent()+1, request.getPageSize());

        IPage<Goods> resultPage = goodsService.page(page, queryWrapper);
        return CommonUtil.returnMsg(MsgConstant.OK, resultPage);
    }

    @GetMapping("/get/{goodsId}")
    public BaseMsg get(@PathVariable Integer goodsId){
        Goods goods = goodsService.getById(goodsId);
        return CommonUtil.returnMsg(MsgConstant.OK, goods);
    }
}

