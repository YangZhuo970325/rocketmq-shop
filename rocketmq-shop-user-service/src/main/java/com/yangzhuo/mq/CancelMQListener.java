package com.yangzhuo.mq;

import com.alibaba.fastjson.JSON;
import com.yangzhuo.api.UserService;
import com.yangzhuo.constant.ShopCode;
import com.yangzhuo.entity.MQEntity;
import com.yangzhuo.pojo.Coupon;
import com.yangzhuo.pojo.UserMoneyLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic}",
        consumerGroup = "${mq.order.consumer.group.name}",
        messageModel = MessageModel.BROADCASTING)
public class CancelMQListener implements RocketMQListener<MessageExt> {
    
    @Autowired
    private UserService userService;

    @Override
    public void onMessage(MessageExt messageExt) {
        // 1. 解析消息的内容
        try {
            String body = new String(messageExt.getBody(), "UTF-8");
            log.info("接收到消息");
            MQEntity mqEntity = JSON.parseObject(body, MQEntity.class);
            // 2. 调用业务层，进行余额回退
            if (mqEntity.getUserMoney() != null && mqEntity.getUserMoney().compareTo(BigDecimal.ZERO) > 0) {
                UserMoneyLog userMoneyLog = new UserMoneyLog();
                userMoneyLog.setUseMoney(mqEntity.getUserMoney());
                userMoneyLog.setMoneyLogType(ShopCode.SHOP_USER_MONEY_REFUND.getCode());
                userMoneyLog.setUserId(mqEntity.getUserId());
                userMoneyLog.setOrderId(mqEntity.getOrderId());
                userService.reduceMoneyPaid(userMoneyLog);
                log.info("余额回退成功");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.info("余额回退失败");
        }
    }
}
