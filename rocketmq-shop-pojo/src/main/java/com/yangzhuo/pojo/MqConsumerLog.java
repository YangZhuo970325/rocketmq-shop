package com.yangzhuo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
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
@TableName("trade_mq_consumer_log")
public class MqConsumerLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息id
     */
    private String msgId;

    /**
     * 消费者组名
     */
    private String groupName;

    /**
     * tag
     */
    private String msgTag;

    /**
     * key
     */
    private String msgKey;

    /**
     * 消息体
     */
    private String msgBody;

    /**
     * 0：正在处理	1：处理成功	2：处理失败
     */
    private Boolean consumerStatus;

    /**
     * 消费次数
     */
    private Boolean consumerTimes;

    /**
     * 消费时间
     */
    private Date consumerTimestamp;

    /**
     * 备注
     */
    private String remark;


}
