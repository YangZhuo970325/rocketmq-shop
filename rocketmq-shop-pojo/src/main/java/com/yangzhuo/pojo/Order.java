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
@TableName("trade_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "order_id", type = IdType.ASSIGN_ID)
    private Long orderId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单状态	0：未确认	1：已确认	2：已取消	3：无效	4：退款
     */
    private Integer orderStatus;

    /**
     * 支付状态	0：未支付	1：支付中	2：已支付
     */
    private Integer payStatus;

    /**
     * 发货状态：	0：未发货	1：已发货	2：已退货
     */
    private Integer shippingStatus;

    /**
     * 地址
     */
    private String address;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品数量
     */
    private Integer goodsNumber;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品总价
     */
    private BigDecimal goodsAmount;

    /**
     * 运费
     */
    private BigDecimal shippingFree;

    /**
     * 订单价格
     */
    private BigDecimal orderAmount;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 优惠券
     */
    private BigDecimal couponPaid;

    /**
     * 已付金额
     */
    private BigDecimal moneyPaid;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 订单确认时间
     */
    private Date confirmTime;

    /**
     * 支付时间
     */
    private Date payTime;


}
