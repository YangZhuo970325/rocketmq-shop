package com.yangzhuo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yangzhuo.api.OrderService;
import com.yangzhuo.api.UserService;
import com.yangzhuo.constant.ShopCode;
import com.yangzhuo.entity.Result;
import com.yangzhuo.exception.CastException;
import com.yangzhuo.mapper.UserMapper;
import com.yangzhuo.mapper.UserMoneyLogMapper;
import com.yangzhuo.pojo.User;
import com.yangzhuo.pojo.UserMoneyLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangzhuo
 * @since 2021-05-28
 */
@Component
@Service(interfaceClass = UserService.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Autowired
    private UserMoneyLogMapper userMoneyLogMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result reduceMoneyPaid(UserMoneyLog userMoneyLog) {
        // 1. 校验参数是否合法
        if (userMoneyLog == null || userMoneyLog.getOrderId() == null || userMoneyLog.getUseMoney() == null 
                || userMoneyLog.getUseMoney().compareTo(BigDecimal.ZERO) <= 0) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        
        // 2. 查询订单余额使用日志
        QueryWrapper<UserMoneyLog> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",userMoneyLog.getUserId());
        wrapper.eq("order_id", userMoneyLog.getOrderId());
        int r = userMoneyLogMapper.selectCount(wrapper);
        User user = userMapper.selectById(userMoneyLog.getUserId());
        
        // 3. 扣减余额
        if (userMoneyLog.getMoneyLogType().intValue() == ShopCode.SHOP_USER_MONEY_PAID.getCode().intValue()) {
            if (r > 0) {
                // 已经付款
                CastException.cast(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY);
            }
            user.setUserMoney(user.getUserMoney().subtract(userMoneyLog.getUseMoney()));
        }
        
        // 4. 回退余额
        if (userMoneyLog.getMoneyLogType().intValue() == ShopCode.SHOP_USER_MONEY_REFUND.getCode().intValue()) {
            if (r < 0) {
                CastException.cast(ShopCode.SHOP_ORDER_PAY_STATUS_NO_PAY);
            }
            //防止多次退款
            QueryWrapper<UserMoneyLog> wrapper1=new QueryWrapper<>();
            wrapper1.eq("user_id",userMoneyLog.getUserId());
            wrapper1.eq("order_id", userMoneyLog.getOrderId());
            wrapper1.eq("money_log_type", ShopCode.SHOP_USER_MONEY_REFUND.getCode());
            int r1 = userMoneyLogMapper.selectCount(wrapper1);
            if (r1 > 0) {
                CastException.cast(ShopCode.SHOP_USER_MONEY_REDUCE_ALREADY);
            }
            //退款
            user.setUserMoney(user.getUserMoney().add(userMoneyLog.getUseMoney()));
        }
        userMapper.updateById(user);
        // 5. 记录订单余额使用日志
        userMoneyLog.setCreateTime(new Date());
        userMoneyLogMapper.insert(userMoneyLog);
        
        return new Result(ShopCode.SHOP_SUCCESS.getSuccess(), ShopCode.SHOP_SUCCESS.getMessage());
    }
}
