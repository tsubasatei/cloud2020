package com.xt.springcloud.service.impl;

import com.xt.springcloud.dao.AccountDao;
import com.xt.springcloud.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public void decrease(Long userId, BigDecimal money) {
        log.info("--->account-service中扣减余额开始");
        // 模拟超时异常，全局事务回滚
//        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
        accountDao.decrease(userId, money);
        log.info("--->account-service中扣减余额结束");
    }
}
