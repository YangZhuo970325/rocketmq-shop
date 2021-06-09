package com.yangzhuo.mq;

import com.alibaba.fastjson.JSON;
import com.yangzhuo.constant.ShopCode;
import com.yangzhuo.entity.MQEntity;
import com.yangzhuo.mapper.OrderMapper;
import com.yangzhuo.pojo.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic}",
        consumerGroup = "${mq.order.consumer.group.name}",
        messageModel = MessageModel.BROADCASTING)
public class CancelMQListener implements RocketMQListener<MessageExt> {
    
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void onMessage(MessageExt messageExt) {
        // 1. 解析消息的内容
        try {
            String body = new String(messageExt.getBody(), "UTF-8");
            log.info("接收到消息");
            MQEntity mqEntity = JSON.parseObject(body, MQEntity.class);
            // 2. 查询
            if (mqEntity.getOrderId() != null) {
                Order order = orderMapper.selectById(mqEntity.getOrderId());
                order.setOrderStatus(ShopCode.SHOP_ORDER_MESSAGE_STATUS_CANCEL.getCode());
                orderMapper.updateById(order);
                log.info("订单状态设置为取消");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.info("订单状态设置为取消失败");
        }
    }
}
