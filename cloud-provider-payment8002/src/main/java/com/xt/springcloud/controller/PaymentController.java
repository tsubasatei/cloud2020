package com.xt.springcloud.controller;

import com.xt.springcloud.entity.CommonResult;
import com.xt.springcloud.entity.Payment;
import com.xt.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        log.info("查询结果：" + payment);
        if (payment != null) {
            return new CommonResult<>(200, "查询成功" + "\t 服务端口：" + serverPort, payment);
        }
        return new CommonResult<>(444, "没有对应记录, 查询ID：" + id, null);
    }

    @PostMapping("/payment/create")
    public CommonResult create (@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        log.info("***插入结果：" + result);
        if (result > 0) {
            return new CommonResult(200, "插入数据成功,返回结果 " + result + "\t 服务端口：" + serverPort, payment);
        }
        return new CommonResult(444, "插入数据失败", null);
    }
}
