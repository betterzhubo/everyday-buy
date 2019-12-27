package com.seasoning.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seasoning.entity.ShopingCartGoods;
import com.seasoning.entity.ShoppingCart;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    Page<ShopingCartGoods> getListByUserId(Page<ShopingCartGoods> page, int userId);
}
