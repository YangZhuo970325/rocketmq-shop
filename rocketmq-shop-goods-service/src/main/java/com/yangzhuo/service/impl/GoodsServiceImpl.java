package com.yangzhuo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yangzhuo.api.GoodsService;
import com.yangzhuo.constant.ShopCode;
import com.yangzhuo.entity.Result;
import com.yangzhuo.exception.CastException;
import com.yangzhuo.mapper.GoodsMapper;
import com.yangzhuo.mapper.GoodsNumberLogMapper;
import com.yangzhuo.pojo.Goods;
import com.yangzhuo.pojo.GoodsNumberLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangzhuo
 * @since 2021-05-28
 */
@Component
@Service(interfaceClass = GoodsService.class)
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    
    @Autowired
    private GoodsNumberLogMapper goodsNumberLogMapper;
    
    
    @Override
    public Result reduceGoodsNum(GoodsNumberLog goodsNumberLog) {
        if (goodsNumberLog == null || goodsNumberLog.getGoodsId() == null || goodsNumberLog.getOrderId() == null ||
            goodsNumberLog.getGoodsNumber() == null || goodsNumberLog.getGoodsNumber() <= 0) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        Goods goods = goodsMapper.selectById(goodsNumberLog.getGoodsId());
        if (goods.getGoodsNumber() < goodsNumberLog.getGoodsNumber()) {
            //库存不足
            CastException.cast(ShopCode.SHOP_GOODS_NUM_NOT_ENOUGH);
        }
        //减库存
        goods.setGoodsNumber(goods.getGoodsNumber() - goodsNumberLog.getGoodsNumber());
        goodsMapper.updateById(goods);
        
        //记录库存操作日志
        goodsNumberLog.setGoodsNumber(-(goodsNumberLog.getGoodsNumber()));
        goodsNumberLog.setLogTime(new Date());
        goodsNumberLogMapper.insert(goodsNumberLog);
        
        return new Result(ShopCode.SHOP_SUCCESS.getSuccess(), ShopCode.SHOP_SUCCESS.getMessage());
    }
}
