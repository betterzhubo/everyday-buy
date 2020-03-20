package com.seasoning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seasoning.entity.Region;
import com.seasoning.request.tem.CityTree;
import com.seasoning.request.tem.ProvinceTree;
import com.seasoning.request.tem.ProvinceTreeUnify;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author better
 * @since 2020-03-20
 */
public interface RegionMapper extends BaseMapper<Region> {

    List<ProvinceTree> getRegionTree();
    List<ProvinceTreeUnify> getRegionTreeLN();
    List<CityTree> getRegionTreeLNLevelTwo();
}
