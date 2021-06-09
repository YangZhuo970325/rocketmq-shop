package com.yangzhuo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yangzhuo.entity.Result;
import com.yangzhuo.pojo.Coupon;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yangzhuo
 * @since 2021-05-28
 */
public interface CouponService extends IService<Coupon> {

    /**
     * 更新优惠券状态
     * @param coupon 优惠券
     * @return
     */
    Result updateCouponStatus(Coupon coupon);

}
