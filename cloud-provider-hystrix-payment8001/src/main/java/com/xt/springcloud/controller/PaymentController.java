package com.xt.springcloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xt.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value = "/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id) {
        String result = paymentService.paymentInfo_OK(id);
        log.info("***result: " + result);
        return result;
    }

    @GetMapping("/payment/hystrix/timeout/{id}")
    public String paymentInfo_Timeout(@PathVariable("id") Integer id) {
        String result = paymentService.paymentInfo_Timeout(id);
        log.info("***result: " + result);
        return result;
    }

    @GetMapping("/payment/hystrix/{id}")
    @HystrixCommand(fallbackMethod = "paymentInfoHandlerException")
    public String paymentInfo(@PathVariable("id") Integer id) {
        if (id >= 0) {
            return "调用支付接口hystrix服务：\t" + serverPort + "\t entity ID: " + id;
        } else {
            throw new RuntimeException("ID不能是负数");
        }
    }

    public String paymentInfoHandlerException(Integer id) {
        return "调用支付接口hystrix服务出现异常：\t" + serverPort + "\t ID不能是负数";
    }

    // ===服务熔断
    @GetMapping("/payment/circuit/{id}")
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        String result = paymentService.paymentCircuitBreaker(id);
        log.info("***result: " + result);
        return result;
    }
}
