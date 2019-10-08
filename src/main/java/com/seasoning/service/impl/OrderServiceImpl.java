package com.seasoning.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seasoning.entity.Order;
import com.seasoning.mapper.OrderMapper;
import com.seasoning.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

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

    private static AtomicInteger DATA_PREFIX = new AtomicInteger(0);

    private static int NOW_DAY = 0;
    private static int NOW_YEAR = 0;

    @Override
    public int generateOrderId(){

        int orderId = (NOW_YEAR << 21) | (DATA_PREFIX.incrementAndGet() << 9) | NOW_DAY;

        return orderId;
    }

    @PostConstruct
    public void init(){
        int lastOrderId = baseMapper.findLastId();
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        NOW_YEAR = cal.get(Calendar.YEAR)%2000;
        NOW_DAY = (month << 5) | day;
        if(NOW_DAY == (lastOrderId & NOW_DAY)){
            DATA_PREFIX.set((lastOrderId >> 9) & 0x3fff);
        }
    }
}