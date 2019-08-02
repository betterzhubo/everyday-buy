package com.seasoning.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.Goods;
import com.seasoning.entity.GoodsType;
import com.seasoning.request.GoodsAddRequest;
import com.seasoning.service.GoodsTypeService;
import com.seasoning.util.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
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

    @DeleteMapping("/delete")
    public BaseMsg delete(@PathVariable Integer goodsTypeId)  {

        return CommonUtil.returnMsg(MsgConstant.OK,  goodsTypeService.removeById(goodsTypeId));
    }

    @PostMapping("/add")
    public BaseMsg add(@RequestBody JSONObject request) {
//        if(result.hasErrors()){
//            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
//        }
        GoodsType goodsType = JSONObject.toJavaObject(request, GoodsType.class);
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
        if(goodsTypeService.save(goodsType)){
            return CommonUtil.returnMsg(MsgConstant.OK, goodsType.getId());
        }
        return CommonUtil.returnMsg(MsgConstant.PROGRAM_ERROR);
    }
}

