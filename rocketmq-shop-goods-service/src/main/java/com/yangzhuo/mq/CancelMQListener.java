package com.yangzhuo.mq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yangzhuo.constant.ShopCode;
import com.yangzhuo.entity.MQEntity;
import com.yangzhuo.mapper.GoodsMapper;
import com.yangzhuo.mapper.GoodsNumberLogMapper;
import com.yangzhuo.mapper.MqConsumerLogMapper;
import com.yangzhuo.pojo.Goods;
import com.yangzhuo.pojo.GoodsNumberLog;
import com.yangzhuo.pojo.MqConsumerLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic}",
        consumerGroup = "${mq.order.consumer.group.name}",
        messageModel = MessageModel.BROADCASTING)
public class CancelMQListener implements RocketMQListener<MessageExt> {
    
    @Value("${mq.order.consumer.group.name}")
    private String groupName;
    
    @Autowired
    private MqConsumerLogMapper mqConsumerLogMapper;
    
    @Autowired
    private GoodsMapper goodsMapper;
    
    @Autowired
    private GoodsNumberLogMapper goodsNumberLogMapper;

    @Override
    public void onMessage(MessageExt messageExt) {

        // 1. 解析消息的内容
        String msgId = null;
        String tags = null;
        String keys = null;
        String body = null;

        try {
            // 1. 解析消息的内容
            msgId = messageExt.getMsgId();
            tags = messageExt.getTags();
            keys = messageExt.getKeys();
            body = new String(messageExt.getBody(), "UTF-8");

            // 2. 查询消息消费记录
            QueryWrapper<MqConsumerLog> wrapper = new QueryWrapper<>();
            wrapper.eq("msg_tag", tags);
            wrapper.eq("group_name", groupName);
            wrapper.eq("msg_key", keys);
            MqConsumerLog mqConsumerLog = mqConsumerLogMapper.selectOne(wrapper);
            
            if (mqConsumerLog != null) {
                // 3. 判断如果消费过
                // 获得消息处理状态
                int status = mqConsumerLog.getConsumerStatus().intValue();
                
                // 处理过 ... 返回
                if (ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode().intValue() == status) {
                    log.info("消息：" + msgId + " ,已经处理过");
                    return;
                } else if (ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode().intValue() == status) {
                    log.info("消息：" + msgId + " ,正在处理");
                    return;
                } else if (ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode().intValue() == status) {
                    //获得消息处理次数
                    int times = mqConsumerLog.getConsumerTimes();
                    if (times > 3) {
                        log.info("消息：" + msgId + ", 消息处理超过3次，不能再进行处理了");
                        return;
                    }
                    mqConsumerLog.setConsumerStatus(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode());
                    wrapper.eq("consumer_times", mqConsumerLog.getConsumerTimes());
                    int r = mqConsumerLogMapper.update(mqConsumerLog, wrapper);
                    if (r < 1) {
                        //未修改成功
                        log.info("并发修改，稍后处理");
                    }
                }
                
            } else {
                // 4. 判断如果没有消费过
                mqConsumerLog = new MqConsumerLog();
                mqConsumerLog.setMsgTag(tags);
                mqConsumerLog.setMsgKey(keys);
                mqConsumerLog.setConsumerStatus(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode());
                mqConsumerLog.setMsgBody(body);
                mqConsumerLog.setMsgId(msgId);
                mqConsumerLog.setConsumerTimes(0);
                
                //将消息处理信息添加到数据库
                mqConsumerLogMapper.insert(mqConsumerLog);
            }
            
            // 5. 回退库存
            MQEntity mqEntity = JSON.parseObject(body, MQEntity.class);
            Long goodsId = mqEntity.getGoodsId();
            Goods goods = goodsMapper.selectById(goodsId);
            goods.setGoodsNumber(goods.getGoodsNumber() + mqEntity.getGoodsNumber());
            goodsMapper.updateById(goods);

            // 6. 记录库存操作日志
            GoodsNumberLog goodsNumberLog = new GoodsNumberLog();
            goodsNumberLog.setOrderId(mqEntity.getOrderId());
            goodsNumberLog.setGoodsId(goodsId);
            goodsNumberLog.setGoodsNumber(mqEntity.getGoodsNumber());
            goodsNumberLog.setLogTime(new Date());
            goodsNumberLogMapper.insert(goodsNumberLog);
            
            // 7. 将消息的处理状态改为处理成功
            mqConsumerLog.setConsumerStatus(ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode());
            mqConsumerLog.setConsumerTimestamp(new Date());
            mqConsumerLogMapper.updateById(mqConsumerLog);
            
            log.info("回退库存成功");
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            //  查询消息消费记录
            QueryWrapper<MqConsumerLog> wrapper = new QueryWrapper<>();
            wrapper.eq("msg_tag", tags);
            wrapper.eq("group_name", groupName);
            wrapper.eq("msg_key", keys);
            MqConsumerLog mqConsumerLog = mqConsumerLogMapper.selectOne(wrapper);
            if (mqConsumerLog == null) {
                //数据库没有记录
                mqConsumerLog = new MqConsumerLog();
                mqConsumerLog = new MqConsumerLog();
                mqConsumerLog.setMsgTag(tags);
                mqConsumerLog.setMsgKey(keys);
                mqConsumerLog.setConsumerStatus(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode());
                mqConsumerLog.setMsgBody(body);
                mqConsumerLog.setMsgId(msgId);
                mqConsumerLog.setConsumerTimes(1);
            } else {
                mqConsumerLog.setConsumerTimes(mqConsumerLog.getConsumerTimes() + 1);
                mqConsumerLogMapper.updateById(mqConsumerLog);
            }
        }
    }
}
