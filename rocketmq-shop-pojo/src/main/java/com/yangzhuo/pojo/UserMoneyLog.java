package com.yangzhuo.pojo;

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
@TableName("trade_user_money_log")
public class UserMoneyLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 日志操作	1：订单付款	2：订单退款
     */
    private Boolean moneyLogType;

    /**
     * 操作金额
     */
    private BigDecimal useMoney;

    /**
     * 日志时间
     */
    private Date createTime;


}
