package com.yangzhuo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yangzhuo.api.UserService;
import com.yangzhuo.mapper.UserMapper;
import com.yangzhuo.pojo.User;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangzhuo
 * @since 2021-05-28
 */
@Component
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
