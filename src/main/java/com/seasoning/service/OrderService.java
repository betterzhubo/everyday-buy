package com.seasoning.service;

import com.seasoning.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
public interface OrderService extends IService<Order> {

    int generateOrderId();

    void updateDay();

}
