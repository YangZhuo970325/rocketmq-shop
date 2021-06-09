package com.yangzhuo.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yangzhuo.api.CouponService;
import com.yangzhuo.api.GoodsService;
import com.yangzhuo.api.OrderService;
import com.yangzhuo.api.UserService;
import com.yangzhuo.constant.ShopCode;
import com.yangzhuo.entity.Result;
import com.yangzhuo.exception.CastException;
import com.yangzhuo.mapper.OrderMapper;
import com.yangzhuo.pojo.*;
import com.yangzhuo.utils.SnowFlake;
import lombok.extern.slf4j.Slf4j;
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
@Service(interfaceClass = OrderService.class)
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    
    @Reference
    private GoodsService goodsService;
    
    @Reference
    private UserService userService;
    
    @Autowired
    private SnowFlake snowFlake;
    
    @Reference
    private CouponService couponService;
    
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Result confirmOrder(Order order) {
        //1. 校验订单
        getCheckOrder(order);
        
        //2. 生成预订单
        long orderId = savePerOrder(order);
        
        try {
            //3. 扣减库存
            getReduceGoodsNum(order);
            
            //4. 扣减优惠券
            updateCouponStatus(order);
            
            //5. 扣减余额
            reduceMoneyPaid(order);

            //6. 确认订单
            updateOrderStatus(order);
            log.info("订单:[" + orderId + "]确认成功");
            
            //7. 返回成功状态
            return new Result(ShopCode.SHOP_SUCCESS.getSuccess(), ShopCode.SHOP_SUCCESS.getMessage());
            
        } catch (Exception e) {
            //1. 确认订单失败
            
            //2. 返回失败状态
        }
        return null;
    }
    
    private void updateOrderStatus(Order order) {
        order.setOrderStatus(ShopCode.SHOP_ORDER_CONFIRM.getCode());
        order.setPayStatus(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY.getCode());
        order.setConfirmTime(new Date());
        int r = orderMapper.updateById(order);
        if (r <= 0) {
            CastException.cast(ShopCode.SHOP_ORDER_CONFIRM_FAIL);
        }
        log.info("订单：" + order.getOrderId() + "确认订单成功");
    }

    /**
     * 扣减余额
     * @param order
     */
    private void reduceMoneyPaid(Order order) {
        if (order.getMoneyPaid() != null && order.getMoneyPaid().compareTo(BigDecimal.ZERO) == 1) {
            UserMoneyLog userMoneyLog = new UserMoneyLog();
            userMoneyLog.setOrderId(order.getOrderId());
            userMoneyLog.setUserId(order.getUserId());
            userMoneyLog.setUseMoney(order.getMoneyPaid());
            userMoneyLog.setMoneyLogType(ShopCode.SHOP_USER_MONEY_PAID.getCode());
            Result result = userService.reduceMoneyPaid(userMoneyLog);
            if (result.getSuccess().equals(ShopCode.SHOP_FAIL.getSuccess())) {
                CastException.cast(ShopCode.SHOP_USER_MONEY_REDUCE_FAIL);
            }
            log.info("订单：" + order.getOrderId() + "，扣减余额成功");
        }
    }

    /**
     * 使用优惠券
     * @param order 订单
     */
    private void updateCouponStatus(Order order) {
        if (order.getCouponId() != null) {
            Coupon coupon = couponService.getById(order.getCouponId());
            coupon.setCouponId(order.getCouponId());
            coupon.setIsUsed(ShopCode.SHOP_COUPON_ISUSED.getCode());
            coupon.setUsedTime(new Date());
            
            //更新优惠券状态
            Result result = couponService.updateCouponStatus(coupon);
            if (result.getSuccess().equals(ShopCode.SHOP_FAIL.getSuccess())) {
                CastException.cast(ShopCode.SHOP_COUPON_USE_FAIL);
            }
            log.info("订单：" + order.getOrderId() + "，使用优惠券");
        }
    }

    /**
     * 扣减库存
     * @param order
     */
    private void getReduceGoodsNum(Order order) {
        // 需要传参 订单id,商品id,商品数量
        GoodsNumberLog goodsNumberLog = new GoodsNumberLog();
        goodsNumberLog.setOrderId(order.getOrderId());
        goodsNumberLog.setGoodsId(order.getGoodsId());
        goodsNumberLog.setGoodsNumber(order.getGoodsNumber());
        Result result = goodsService.reduceGoodsNum(goodsNumberLog);
        if (result.getSuccess().equals(ShopCode.SHOP_FAIL.getSuccess())) {
            CastException.cast(ShopCode.SHOP_REDUCE_GOODS_NUM_FAIL);
        }
        log.info("订单：" + order.getOrderId() + " 扣减库存成功");
    }

    private long savePerOrder(Order order) {
        // 1. 设置订单状态为不可见
        order.setOrderStatus(ShopCode.SHOP_ORDER_NO_CONFIRM.getCode());
        
        // 2. 设置订单id
        long orderId = snowFlake.nextId();
        order.setOrderId(orderId);
        
        // 3. 核算订单运费
        BigDecimal shippingFree = getShippingFree(order.getOrderAmount());
        if (order.getShippingFree().compareTo(shippingFree) != 0) {
            CastException.cast(ShopCode.SHOP_ORDER_SHIPPINGFEE_INVALID);
        }
        
        // 4. 核算订单总金额是否合法
        BigDecimal orderAmount = order.getGoodsPrice().multiply(new BigDecimal(order.getGoodsNumber()));
        orderAmount.add(shippingFree);
        if (order.getOrderAmount().compareTo(orderAmount) != 0) {
            CastException.cast(ShopCode.SHOP_ORDERMOUNT_INVALID);
        }
        
        // 5. 判断用户是否使用余额
        BigDecimal moneyPaid = order.getMoneyPaid();
        if (moneyPaid != null) {
            // 5.1 订单中余额是否合法
            int r = moneyPaid.compareTo(BigDecimal.ZERO);
            
            if (r == -1) {
                CastException.cast(ShopCode.SHOP_MONEY_PAID_LESS_ZERO);
            }
            
            if (r == 1) {
                User user = userService.getById(order.getUserId());
                
                if (moneyPaid.compareTo(user.getUserMoney()) == 1) {
                    CastException.cast(ShopCode.SHOP_MONEY_PAID_INVALIS);
                }
            }
        } else {
            order.setMoneyPaid(BigDecimal.ZERO);
        }
        
        // 6. 判断用户是否使用优惠券
        Long couponId = order.getCouponId();
        if (couponId != null) {
            // 6.1 判断优惠券是否存在
            Coupon coupon = couponService.getById(couponId);
            if (coupon == null) {
                CastException.cast(ShopCode.SHOP_COUPON_NO_EXIST);
            }
            // 6.2 判断优惠券是否已经被使用
            if (coupon.getIsUsed().intValue() == ShopCode.SHOP_COUPON_ISUSED.getCode().intValue()) {
                CastException.cast(ShopCode.SHOP_COUPON_ISUSED);
            }
            order.setCouponPaid(coupon.getCouponPrice());
        } else {
            order.setCouponPaid(BigDecimal.ZERO);
        }
        
        // 7. 核算订单支付金额
        BigDecimal payAmount = order.getOrderAmount().subtract(order.getMoneyPaid()).subtract(order.getCouponPaid());
        order.setPayAmount(payAmount);
        
        // 8. 设置下单时间
        order.setAddTime(new Date());
        
        // 9. 保存订单到数据库
        orderMapper.insert(order);
        
        // 10. 返回订单id
        return orderId;
    }

    /**
     * 核算运费
     * @return orderAmount
     */
    private BigDecimal getShippingFree(BigDecimal orderAmount) {
        if (orderAmount.compareTo(new BigDecimal(88)) > 0) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(10);
        }
        
    }

    /**
     * 校验订单
     * @param order
     */
    private void getCheckOrder(Order order) {
        // 1. 校验订单是否存在
        if (order == null) {
            CastException.cast(ShopCode.SHOP_ORDER_INVALID);
        }
        
        // 2. 校验订单中的商品是否存在
        Goods goods = goodsService.getById(order.getGoodsId());
        if (goods == null) {
            CastException.cast(ShopCode.SHOP_GOODS_NO_EXIST);
        }
        
        // 3. 校验下单用户是否存在
        User user = userService.getById(order.getUserId());
        if (user == null) {
            CastException.cast(ShopCode.SHOP_USER_NO_EXIST);
        }

        // 4. 校验商品单价是否合法
        if (order.getGoodsPrice().compareTo(goods.getGoodsPrice()) != 0) {
            CastException.cast(ShopCode.SHOP_GOODS_PRICE_INVALID);
        }
        
        // 5. 校验订单商品数量是否合法
        if (order.getGoodsNumber() >= goods.getGoodsNumber()) {
            CastException.cast(ShopCode.SHOP_GOODS_NUM_NOT_ENOUGH);
        }
        
        log.info("订单校验通过");
        
    }
}
