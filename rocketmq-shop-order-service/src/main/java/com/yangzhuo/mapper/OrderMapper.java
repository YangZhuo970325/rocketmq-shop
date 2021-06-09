package com.yangzhuo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yangzhuo.pojo.Order;
import org.apache.ibatis.annotations.Mapper;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yangzhuo
 * @since 2021-05-28
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
