package com.seasoning.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seasoning.entity.User;
import com.seasoning.mapper.UserMapper;
import com.seasoning.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author better
 * @since 2019-07-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public List<Integer> getIdByName(String name){
        return baseMapper.selectIdByName(name);
    }
}
