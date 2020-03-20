package com.seasoning.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seasoning.entity.Region;
import com.seasoning.mapper.RegionMapper;
import com.seasoning.request.tem.CityTree;
import com.seasoning.request.tem.ProvinceTree;
import com.seasoning.request.tem.ProvinceTreeUnify;
import com.seasoning.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author better
 * @since 2020-03-20
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    @Override
    public List<ProvinceTree> getRegionTree() {
        return this.getBaseMapper().getRegionTree();
    }

    @Override
    public List<ProvinceTreeUnify> getRegionTreeLN() {
        return this.getBaseMapper().getRegionTreeLN();
    }

    @Override
    public List<CityTree> getRegionTreeLNLevelTwo() {
        return this.getBaseMapper().getRegionTreeLNLevelTwo();
    }
}
