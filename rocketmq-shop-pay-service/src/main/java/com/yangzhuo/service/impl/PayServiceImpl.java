package com.yangzhuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yangzhuo.api.PayService;
import com.yangzhuo.pojo.Pay;
import com.yangzhuo.mapper.PayMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangzhuo
 * @since 2021-05-28
 */
@Service
public class PayServiceImpl extends ServiceImpl<PayMapper, Pay> implements PayService {

}
