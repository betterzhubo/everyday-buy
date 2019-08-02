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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author better
 * @since 2019-07-31
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/add")
    public BaseMsg add(@RequestBody GoodsAddRequest request, BindingResult result) {
        if(result.hasErrors()){
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
    public BaseMsg update(Goods goods) {
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
            updateWrapper.set("detailImage", goods.getDetailImage());
        }
        if(goods.getPrice() != null){
            updateWrapper.set("price", goods.getPrice());
        }
        if(goods.getShowImage() != null){
            updateWrapper.set("price", goods.getShowImage());
        }
        if(goods.getPurpose() != null){
            updateWrapper.set("purpose", goods.getPurpose());
        }
        if(goods.getGoodsTypeId() != null){
            updateWrapper.set("goods_type_id", goods.getGoodsTypeId());
        }
        updateWrapper.set("updateTime", LocalDateTime.now());
        return CommonUtil.returnMsg(MsgConstant.OK,  goodsService.update(updateWrapper));
    }

    @DeleteMapping("/delete")
    public BaseMsg delete(@PathVariable Integer goodsId)  {

        return CommonUtil.returnMsg(MsgConstant.OK,  goodsService.removeById(goodsId));
    }

    @PostMapping("/page")
    public BaseMsg page(@RequestBody GoodsSearchRequest request)  {

        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        Page<Goods> page = new Page<>();

        IPage<Goods> resultPage = goodsService.page(page, queryWrapper);
        return CommonUtil.returnMsg(MsgConstant.OK, resultPage);
    }

    @GetMapping("/get")
    public BaseMsg get(@PathVariable Integer goodsId){
        Goods goods = goodsService.getById(goodsId);
        return CommonUtil.returnMsg(MsgConstant.OK, goods);
    }
}

