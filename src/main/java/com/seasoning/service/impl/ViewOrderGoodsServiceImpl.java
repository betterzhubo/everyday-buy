package com.seasoning.service.impl;

import com.seasoning.entity.ViewOrderGoods;
import com.seasoning.mapper.ViewOrderGoodsMapper;
import com.seasoning.service.ViewOrderGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author better
 * @since 2020-05-21
 */
@Service
public class ViewOrderGoodsServiceImpl extends ServiceImpl<ViewOrderGoodsMapper, ViewOrderGoods> implements ViewOrderGoodsService {
    @Override
    public List<Map<String, Object>> getStatusCount(Integer userId) {
        return this.baseMapper.getStatusCount(userId);
    }
}
