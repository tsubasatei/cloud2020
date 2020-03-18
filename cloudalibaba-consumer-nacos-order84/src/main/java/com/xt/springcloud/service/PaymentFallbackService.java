package com.xt.springcloud.service;

import com.xt.springcloud.entity.CommonResult;
import com.xt.springcloud.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentFallbackService implements PaymentService{
    @Override
    public CommonResult<Payment> paymentSQL(Long id) {
        return new CommonResult<>(444, "服务降级返回，--PaymentFallbackService", new Payment(id, "error"));
    }
}
