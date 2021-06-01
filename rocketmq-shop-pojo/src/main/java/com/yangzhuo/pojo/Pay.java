package com.yangzhuo.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

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
@TableName("trade_pay")
public class Pay implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付编号
     */
      @TableId(value = "pay_id", type = IdType.ASSIGN_ID)
    private Long payId;

    /**
     * 支付编号
     */
    private Long orderId;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 是否已支付	1：否	2：是
     */
    private Boolean isPaid;


}
