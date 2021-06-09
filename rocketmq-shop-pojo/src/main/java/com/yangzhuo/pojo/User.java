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
@TableName("trade_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
      @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 手机号
     */
    private String userMobile;

    /**
     * 积分
     */
    private Integer userScore;

    /**
     * 注册时间
     */
    private Date userRegTime;

    /**
     * 用户余额
     */
    private BigDecimal userMoney;


}
