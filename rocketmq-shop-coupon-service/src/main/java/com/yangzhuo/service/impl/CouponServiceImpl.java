package com.yangzhuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yangzhuo.api.CouponService;
import com.yangzhuo.pojo.Coupon;
import com.yangzhuo.mapper.CouponMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangzhuo
 * @since 2021-05-28
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

}
