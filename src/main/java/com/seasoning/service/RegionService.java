package com.seasoning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seasoning.entity.Region;
import com.seasoning.request.tem.CityTree;
import com.seasoning.request.tem.ProvinceTree;
import com.seasoning.request.tem.ProvinceTreeUnify;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author better
 * @since 2020-03-20
 */
public interface RegionService extends IService<Region> {

    List<ProvinceTree> getRegionTree();
    List<ProvinceTreeUnify> getRegionTreeLN();
    List<CityTree> getRegionTreeLNLevelTwo();
}
