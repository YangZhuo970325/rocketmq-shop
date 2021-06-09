package com.yangzhuo.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("trade_mq_producer_log")
public class MqProducerLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 生产者组名
     */
    private String groupName;

    /**
     * 消息主题
     */
    private String msgTopic;

    /**
     * tag
     */
    private String msgTag;

    /**
     * key
     */
    private String msgKey;

    /**
     * 消息内容
     */
    private String msgBody;

    /**
     * 0：未处理	1：已处理
     */
    private Boolean msgStatus;

    /**
     * 记录时间
     */
    private Date createTime;


}
