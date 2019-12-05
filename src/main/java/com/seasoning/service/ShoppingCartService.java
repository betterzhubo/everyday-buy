package com.seasoning.service;

import com.seasoning.entity.ShopingCartGoods;
import com.seasoning.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    List<ShopingCartGoods> getListByUserId(int userId);
}
