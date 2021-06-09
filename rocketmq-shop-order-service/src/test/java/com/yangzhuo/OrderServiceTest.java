package com.yangzhuo;

import com.yangzhuo.api.OrderService;
import com.yangzhuo.pojo.Order;
import com.yangzhuo.utils.SnowFlake;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
public class OrderServiceTest {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private SnowFlake snowFlake;
    
    @Test
    public void confirmOrder() {
        
        Long couponId = 13211123352235L;
        Long goodsId = 975315804L;
        Long userId = 3131231243L;
        
        Order order = new Order();
        order.setGoodsId(goodsId);
        order.setUserId(userId);
        order.setCouponId(couponId);
        order.setAddress("上海");
        order.setGoodsNumber(9);
        order.setGoodsPrice(new BigDecimal(5000));
        order.setShippingFree(BigDecimal.ZERO);
        order.setOrderAmount(new BigDecimal(45000));
        order.setMoneyPaid(new BigDecimal(300));
        orderService.confirmOrder(order);
    }
    
    @Test
    public void getOrderId() {
        long orderId = snowFlake.nextId();
        System.out.println("orderId:" + orderId);
    }
}
