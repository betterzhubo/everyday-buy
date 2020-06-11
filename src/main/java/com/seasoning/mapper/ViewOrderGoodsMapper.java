package com.seasoning.mapper;

import com.seasoning.entity.ViewOrderGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author better
 * @since 2020-05-21
 */
public interface ViewOrderGoodsMapper extends BaseMapper<ViewOrderGoods> {

    @Select("select order_status, count(1) count from `view_order_goods` where user_id=#{userId} and order_status in (1,2) group by order_status")
    List<Map<String, Object>> getStatusCount(int userId);
}
