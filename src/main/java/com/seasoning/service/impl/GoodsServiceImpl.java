package com.seasoning.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seasoning.entity.Goods;
import com.seasoning.entity.ShopingCartGoods;
import com.seasoning.mapper.GoodsMapper;
import com.seasoning.service.GoodsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author better
 * @since 2019-07-31
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Override
    public Page<ShopingCartGoods> getGoodsAndShopingCartPage(Page<ShopingCartGoods> page, int userId) {
        return page.setRecords(this.baseMapper.getGoodsPage(page, userId));
    }
}
