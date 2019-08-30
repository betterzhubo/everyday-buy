package com.seasoning.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.GoodsType;
import com.seasoning.service.GoodsTypeService;
import com.seasoning.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
@RestController
@RequestMapping("/goods-type")
public class GoodsTypeController {

    @Autowired
    private GoodsTypeService goodsTypeService;

    @GetMapping("/list")
    public BaseMsg list()  {
        return CommonUtil.returnMsg(MsgConstant.OK, goodsTypeService.list());
    }

    @DeleteMapping("/delete/{goodsTypeId}")
    public BaseMsg delete(@PathVariable Integer goodsTypeId)  {

        return CommonUtil.returnMsg(MsgConstant.OK,  goodsTypeService.removeById(goodsTypeId));
    }

    @PostMapping("/add")
    public BaseMsg add(@RequestBody GoodsType goodsType ) {

        if(goodsType == null){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        if(goodsTypeService.save(goodsType)){
            return CommonUtil.returnMsg(MsgConstant.OK, goodsType.getId());
        }
        return CommonUtil.returnMsg(MsgConstant.PROGRAM_ERROR);
    }

    @PutMapping("/update")
    public BaseMsg update(@RequestBody GoodsType goodsType) {
        if(null == goodsType.getId()){
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", goodsType.getId());
        if(goodsType.getGoodsType() != null){
            updateWrapper.set("goods_type", goodsType.getGoodsType());
        }
        if(goodsTypeService.update(updateWrapper)){
            return CommonUtil.returnMsg(MsgConstant.OK);
        }
        return CommonUtil.returnMsg(MsgConstant.PROGRAM_ERROR);
    }
}

