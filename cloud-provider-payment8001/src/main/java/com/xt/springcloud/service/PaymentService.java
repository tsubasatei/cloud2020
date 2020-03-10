package com.xt.springcloud.service;

import com.xt.springcloud.entity.Payment;

public interface PaymentService {
    int create(Payment payment);
    Payment getPaymentById(Long id);
}
