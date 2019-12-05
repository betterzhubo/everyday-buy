package com.seasoning.mapper;

import com.seasoning.entity.ShopingCartGoods;
import com.seasoning.entity.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    @Select("SELECT s.id AS id,s.user_id AS user_id,s.goods_id AS goods_id,s.goods_count AS goods_count,g.goods_name AS goods_name,g.purpose AS purpose,g.selling AS selling,g.show_image AS show_image,g.price AS price FROM shopping_cart AS s, goods AS g WHERE s.goods_id = g.id and s.user_id=#{userId}")
    List<ShopingCartGoods> getListByUserId(int userId);

}
