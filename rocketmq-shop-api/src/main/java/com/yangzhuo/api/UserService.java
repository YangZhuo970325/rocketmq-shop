package com.yangzhuo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yangzhuo.entity.Result;
import com.yangzhuo.pojo.User;
import com.yangzhuo.pojo.UserMoneyLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yangzhuo
 * @since 2021-05-28
 */
public interface UserService extends IService<User> {

    Result reduceMoneyPaid(UserMoneyLog userMoneyLog);

}
