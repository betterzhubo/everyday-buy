package com.seasoning.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.ContextConstant;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.GoodsImages;
import com.seasoning.service.GoodsImagesService;
import com.seasoning.util.CommonUtil;
import com.seasoning.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author better
 * @since 2019-08-15
 */
@Slf4j
@RestController
@Api(tags = "EcgBindingController", description = "APP端绑定手表相关操作")
@RequestMapping("/goods-images")
public class GoodsImagesController {

    @Autowired
    private ContextConstant contextConstant;
    @Autowired
    private GoodsImagesService goodsImagesService;

    @ApiOperation(value = "根据图片类型和商品id获取对应的图片列表")
    @GetMapping("/list/{goodsId}/{imagesType}")
    @ResponseBody
    public BaseMsg<List<GoodsImages>> getImagesList(@PathVariable Integer goodsId, @PathVariable @ApiParam(value = "图片类型1:轮播图片,2:详情图片'") Integer imagesType){
        QueryWrapper<GoodsImages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id", goodsId);
        if(imagesType != null && imagesType != 0){
            queryWrapper.eq("images_type", imagesType);
        }

        return CommonUtil.returnMsg(MsgConstant.OK, goodsImagesService.list(queryWrapper));
    }

    @PostMapping("/save")
    @ResponseBody
    public BaseMsg save(@RequestBody JSONObject param) throws IOException{
        Integer goodsId = Integer.parseInt(param.get("goodsId").toString());
        Integer imagesType = Integer.parseInt(param.get("imagesType").toString());
        Map<String, Object> map = new HashMap<>();
        map.put("goods_id", goodsId);
        map.put("images_type", imagesType);
        goodsImagesService.removeByMap(map);

        JSONArray urls = param.getJSONArray("urls");
        List<GoodsImages> list = new ArrayList<>();
        for(int i=0;i<urls.size();i++){
            String url = urls.get(i).toString();

            GoodsImages goodsImages = new GoodsImages();
            goodsImages.setGoodsId(goodsId);
            goodsImages.setImagesType(imagesType);
            goodsImages.setImagesUrl(url);
            list.add(goodsImages);
        }
        goodsImagesService.saveBatch(list);
        return CommonUtil.returnMsg(MsgConstant.OK);
    }

    @PostMapping("/upload")
    @ResponseBody
    public BaseMsg upload(@RequestParam MultipartFile uploadFile ) throws IOException{

        String filePath = contextConstant.getImagePath() + uploadFile.getOriginalFilename();
        return CommonUtil.returnMsg(FileUtil.copyFile(uploadFile, filePath)? MsgConstant.OK : MsgConstant.FILE_UPLODE_ERROR);
    }
}

