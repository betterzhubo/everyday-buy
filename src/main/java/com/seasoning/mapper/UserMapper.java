package com.seasoning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seasoning.entity.User;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author better
 * @since 2019-07-26
 */
public interface UserMapper extends BaseMapper<User> {

    List<Integer> selectIdByName(String name);

}
