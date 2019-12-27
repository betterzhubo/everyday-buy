package com.seasoning.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seasoning.entity.Goods;
import com.seasoning.entity.ShopingCartGoods;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author better
 * @since 2019-07-31
 */
public interface GoodsService extends IService<Goods> {

    Page<ShopingCartGoods> getGoodsAndShopingCartPage(Page<ShopingCartGoods> page, int userId);
}
