package com.seasoning.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seasoning.entity.Goods;
import com.seasoning.entity.ShopingCartGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author better
 * @since 2019-07-31
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("select g.id as goods_id, g.goods_name, g.purpose, g.selling , g.goods_type_id, g.show_image, g.price, c.id, c.goods_count from goods g left JOIN (select * from shopping_cart c  where c.user_id = #{userId}) c on g.id = c.goods_id  ${ew.customSqlSegment}")
    List<ShopingCartGoods> getGoodsPage(Page<ShopingCartGoods> page, int userId, @Param(Constants.WRAPPER) Wrapper wrapper);

}
