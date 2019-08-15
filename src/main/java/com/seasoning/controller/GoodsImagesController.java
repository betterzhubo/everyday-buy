package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.CommonConstant;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.Goods;
import com.seasoning.entity.GoodsImages;
import com.seasoning.service.GoodsImagesService;
import com.seasoning.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
@RequestMapping("/goods-images")
public class GoodsImagesController {

    @Autowired
    private GoodsImagesService goodsImagesService;

    @GetMapping("/list")
    @ResponseBody
    public BaseMsg getImagesList(@PathVariable Integer goodsId, @PathVariable Integer imagesType){
        QueryWrapper<GoodsImages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id", goodsId);
        if(imagesType != null && imagesType != 0){
            queryWrapper.eq("images_type", imagesType);
        }

        return CommonUtil.returnMsg(MsgConstant.OK, goodsImagesService.list(queryWrapper));
    }

    @PostMapping("/upload")
    public BaseMsg uploadImages(@RequestParam List<MultipartFile> files, @RequestParam Integer goodsId, @RequestParam Integer imagesType) throws IOException{
        Map<String, Object> map = new HashMap<>();
        map.put("goods_id", goodsId);
        map.put("images_type", imagesType);
        goodsImagesService.removeByMap(map);

        String basePath = CommonConstant.IMAGES_PATH + "/" + goodsId + "/" + imagesType + "/";
        File imgDir = new File(basePath);
        if(!imgDir.exists()){
            imgDir.mkdir();
        }else{
            File[] fileArr = imgDir.listFiles();
            for(File f : fileArr){
                f.delete();
            }
        }

        List<GoodsImages> list = new ArrayList<>();
        for(int i=0;i<files.size();i++){
            MultipartFile uploadFile = files.get(i);
            String fileName = uploadFile.getOriginalFilename();
            String extName =fileName.substring(fileName.lastIndexOf("."));
            String filePath = basePath + i + extName;

            File file = new File(filePath);
            try(InputStream inputStream = uploadFile.getInputStream();
                OutputStream outputStream = new FileOutputStream(file)){
                byte[] bs = new byte[1024];
                int res = 0;
                while((res = inputStream.read(bs)) != -1){
                    outputStream.write(bs, 0, res);
                }
            }catch (IOException e){
                log.error("写入文件失败!", e);
                CommonUtil.returnMsg(MsgConstant.OK);
            }
            GoodsImages goodsImages = new GoodsImages();
            goodsImages.setGoodsId(goodsId);
            goodsImages.setImagesType(imagesType);
            goodsImages.setImagesUrl(filePath);
            list.add(goodsImages);
        }
        goodsImagesService.saveBatch(list);
        return CommonUtil.returnMsg(MsgConstant.FILE_GROUPIMG_ERROR);
    }




}

