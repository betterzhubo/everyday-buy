package com.seasoning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seasoning.base.ResponseBaseModelT;
import com.seasoning.entity.Region;
import com.seasoning.request.tem.AreaTree;
import com.seasoning.request.tem.CityTree;
import com.seasoning.request.tem.ProvinceTree;
import com.seasoning.request.tem.ProvinceTreeUnify;
import com.seasoning.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author better
 * @since 2020-03-20
 */
@RestController
@RequestMapping("/region")
@Api(tags = "RegionController", description = "地区信息相关接口")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @ApiOperation(value = "获取地址树")
    @GetMapping("/api/getRegionTree")
    public ResponseBaseModelT<List<ProvinceTree>> getRegionTree() {
        List<ProvinceTree> regionTree = regionService.getRegionTree();
        ResponseBaseModelT<List<ProvinceTree>> responseBaseModelT = new ResponseBaseModelT<>();
        responseBaseModelT.setData(regionTree);
        return responseBaseModelT;
    }

    @ApiOperation(value = "获取地址树(辽宁省)")
    @GetMapping("/api/getRegionTreeLN")
    public ResponseBaseModelT<List<ProvinceTreeUnify>> getRegionTreeLN() {
        List<ProvinceTreeUnify> regionTree = regionService.getRegionTreeLN();
        ResponseBaseModelT<List<ProvinceTreeUnify>> responseBaseModelT = new ResponseBaseModelT<>();
        responseBaseModelT.setData(regionTree);
        return responseBaseModelT;
    }

    @ApiOperation(value = "获取地址树(辽宁省两级:市 区)")
    @GetMapping("/api/getRegionTreeLNLevelTwo")
    public ResponseBaseModelT<List<CityTree>> getRegionTreeLNLevelTwo() {
        List<CityTree> regionTree = regionService.getRegionTreeLNLevelTwo();
        ResponseBaseModelT<List<CityTree>> responseBaseModelT = new ResponseBaseModelT<>();
        responseBaseModelT.setData(regionTree);
        return responseBaseModelT;
    }
	
	@ApiOperation(value = "获取地址树(沈阳市一级： 区)")
    @GetMapping("/api/getRegionTreeSYLevel")
    public ResponseBaseModelT<CityTree> getRegionTreeSYLevel() {
        QueryWrapper<Region> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parentid", 467);
        List<Region> regionList = regionService.list(queryWrapper);
        CityTree cityTree = new CityTree();
        cityTree.setCityid(466);
        cityTree.setCityname("沈阳市");
        cityTree.setLevel(2);
        cityTree.setAreaTreeList(new ArrayList<>());
        for(Region region : regionList){
            AreaTree areaTree = new AreaTree();
            areaTree.setAreaid(region.getAreaid());
            areaTree.setAreaname(region.getAreaname());
            areaTree.setLevel(region.getLevel());
            cityTree.getAreaTreeList().add(areaTree);
        }
        ResponseBaseModelT<CityTree> responseBaseModelT = new ResponseBaseModelT<>();
        responseBaseModelT.setData(cityTree);
        return responseBaseModelT;
    }

}

