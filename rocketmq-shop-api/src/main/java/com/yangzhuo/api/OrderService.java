package com.yangzhuo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yangzhuo.entity.Result;
import com.yangzhuo.pojo.Order;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yangzhuo
 * @since 2021-05-28
 */
public interface OrderService extends IService<Order> {
    
    Result confirmOrder(Order order);

}
