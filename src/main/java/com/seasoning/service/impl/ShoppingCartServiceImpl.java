package com.seasoning.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seasoning.entity.ShopingCartGoods;
import com.seasoning.entity.ShoppingCart;
import com.seasoning.mapper.ShoppingCartMapper;
import com.seasoning.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Override
    public Page<ShopingCartGoods> getListByUserId(Page<ShopingCartGoods> page, int userId){
        return page.setRecords(this.baseMapper.getListByUserId(page, userId));
    }
}
