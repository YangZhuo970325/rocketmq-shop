package com.yangzhuo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yangzhuo.entity.Result;
import com.yangzhuo.pojo.Goods;
import com.yangzhuo.pojo.GoodsNumberLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yangzhuo
 * @since 2021-05-28
 */
public interface GoodsService extends IService<Goods> {
    
    Result reduceGoodsNum(GoodsNumberLog goodsNumberLog);

}
