package com.seasoning.service;

import com.seasoning.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author better
 * @since 2019-07-26
 */
public interface UserService extends IService<User> {

    public List<Integer> getIdByName(String name);
}
