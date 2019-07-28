package com.seasoning.service.impl;

import com.seasoning.entity.Order;
import com.seasoning.mapper.OrderMapper;
import com.seasoning.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author better
 * @since 2019-07-28
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
