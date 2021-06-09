package com.yangzhuo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yangzhuo.api.CouponService;
import com.yangzhuo.constant.ShopCode;
import com.yangzhuo.entity.Result;
import com.yangzhuo.exception.CastException;
import com.yangzhuo.mapper.CouponMapper;
import com.yangzhuo.pojo.Coupon;
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
@Service(interfaceClass = CouponService.class)
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {
    
    @Override
    public Result updateCouponStatus(Coupon coupon) {
        if (coupon == null || coupon.getCouponId() == null) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        //更新优惠券状态
        updateById(coupon);
        return new Result(ShopCode.SHOP_SUCCESS.getSuccess(), ShopCode.SHOP_SUCCESS.getMessage());
    }
}
