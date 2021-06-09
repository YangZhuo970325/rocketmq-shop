package com.yangzhuo.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MQEntity {
    
    // 订单id 优惠券id 用户id 余额 商品id 商品数量
    private Long orderId;
    
    private Long couponId;
    
    private Long userId;

    private BigDecimal userMoney;

    private Long goodsId;
    
    private Integer goodsNumber;
}
