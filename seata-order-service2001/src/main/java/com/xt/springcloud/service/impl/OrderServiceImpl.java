package com.xt.springcloud.service.impl;

import com.xt.springcloud.dao.OrderDao;
import com.xt.springcloud.domain.Order;
import com.xt.springcloud.service.AccountService;
import com.xt.springcloud.service.OrderService;
import com.xt.springcloud.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private StorageService storageService;
    @Autowired
    private AccountService accountService;

    // name: 唯一性，不冲突，随意
//    @GlobalTransactional(name = "fsp-create-order", rollbackFor = Exception.class)
    @Override
    public void create(Order order) {
        log.info("--->开始新建订单");
        // 1. 新建订单
        orderDao.create(order);

        log.info("--->订单微服务开始调用库存，做扣减Count");
        // 2. 扣减库存
        storageService.decrease(order.getUserId(), order.getCount());
        log.info("--->订单微服务调用库存，做扣减Count结束");

        log.info("--->订单微服务开始调用账户，做扣减Money");
        // 3. 账户扣减
        accountService.decrease(order.getProductId(), order.getMoney());
        log.info("--->订单微服务调用账户，做扣减Money结束");

        // 4. 修改订单状态，从0到1，表示订单已完成
        log.info("--->修改订单状态");
        orderDao.update(order.getUserId(), 0);
        log.info("--->修改订单状态结束");

        log.info("-->下订单结束");

    }
}
