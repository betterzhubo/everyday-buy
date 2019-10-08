package com.seasoning.mapper;

import com.seasoning.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
public interface OrderMapper extends BaseMapper<Order> {

    int findLastId();
}
