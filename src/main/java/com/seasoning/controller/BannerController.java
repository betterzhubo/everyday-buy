package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seasoning.base.BaseMsg;
import com.seasoning.base.CommonConstant;
import com.seasoning.base.MsgConstant;
import com.seasoning.entity.Banner;
import com.seasoning.request.AddBannerRequest;
import com.seasoning.request.UpdateBannerRequest;
import com.seasoning.service.BannerService;
import com.seasoning.util.CommonUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author better
 * @since 2020-05-30
 */
@Slf4j
@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @ApiOperation(value = "获取首页轮播图")
    @GetMapping("/list")
    public BaseMsg<List<Banner>> list(HttpSession session){

        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
        String mark = (String) session.getAttribute(CommonConstant.USER_MANAGEMENT);
        if (StringUtils.equals("1", mark)) {
            queryWrapper.orderByDesc("sort", "create_time");
        }else{
            queryWrapper.eq("is_show", 1);
            queryWrapper.orderByDesc("sort");
        }
        List<Banner> list = bannerService.list(queryWrapper);

        return CommonUtil.returnMsg(MsgConstant.OK, list);
    }

    @ApiOperation(value = "根据id删除轮播图")
    @DeleteMapping("/delete/{id}")
    public BaseMsg delete(@PathVariable Integer id){

        if(id == null){
            return CommonUtil.returnMsg(MsgConstant.PARAM_IS_NULL);
        }
        Banner banner = bannerService.getById(id);
        if(banner == null){
            return CommonUtil.returnMsg(MsgConstant.DATA_ERROR);
        }
        bannerService.removeById(id);

        return CommonUtil.returnMsg(MsgConstant.OK);
    }

    @ApiOperation(value = "添加轮播图")
    @PostMapping("/add")
    public BaseMsg add(@RequestBody @Valid AddBannerRequest request, BindingResult result){

        if(result.hasErrors()){
            log.error("参数错误！"+ result.getAllErrors().get(0).getDefaultMessage());
            return CommonUtil.returnMsg(MsgConstant.PARAM_ERROR);
        }
        Banner banner = new Banner();
        BeanUtils.copyProperties(request, banner);
        if(!bannerService.save(banner)){
           return CommonUtil.returnMsg(MsgConstant.SQL_ERROR);
        }
        return CommonUtil.returnMsg(MsgConstant.OK);
    }

    @ApiOperation(value = "修改轮播图")
    @PutMapping("/update")
    public BaseMsg update(@RequestBody UpdateBannerRequest request){

        if(request.getId() == null){
            return CommonUtil.returnMsg(MsgConstant.PARAM_IS_NULL);
        }
        Banner banner = bannerService.getById(request.getId());
        if(banner == null){
            return CommonUtil.returnMsg(MsgConstant.DATA_ERROR);
        }

        Banner updateBanner = new Banner();
        BeanUtils.copyProperties(request, updateBanner);
        if(!bannerService.updateById(updateBanner)){
            return CommonUtil.returnMsg(MsgConstant.SQL_ERROR);
        }
        return CommonUtil.returnMsg(MsgConstant.OK);
    }
}

