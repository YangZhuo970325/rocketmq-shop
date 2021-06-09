package com.yangzhuo.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yangzhuo
 * @since 2021-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("trade_coupon")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券id
     */
    @TableId(value = "coupon_id", type = IdType.ASSIGN_ID)
    private Long couponId;

    /**
     * 优惠券金额
     */
    private BigDecimal couponPrice;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 是否使用
     */
    private Integer isUsed;

    /**
     * 使用时间
     */
    private Date usedTime;


}
