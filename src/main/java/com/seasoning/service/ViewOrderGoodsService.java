package com.seasoning.service;

import com.seasoning.entity.ViewOrderGoods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author better
 * @since 2020-05-21
 */
public interface ViewOrderGoodsService extends IService<ViewOrderGoods> {

    List<Map<String, Object>> getStatusCount(Integer userId);
}
