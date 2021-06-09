package com.yangzhuo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yangzhuo.mapper.UserMoneyLogMapper;
import com.yangzhuo.pojo.UserMoneyLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class UserServiceTest {
    
    @Autowired
    private UserMoneyLogMapper userMoneyLogMapper;
    
    @Test
    public void getCount() {
        QueryWrapper<UserMoneyLog> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id", 1);
        wrapper.eq("order_id", 2);
        int r = userMoneyLogMapper.selectCount(wrapper);
        System.out.println("r:" + r);
    }
}
