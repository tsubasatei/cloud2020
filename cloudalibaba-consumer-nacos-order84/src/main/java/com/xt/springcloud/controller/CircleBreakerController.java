package com.xt.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.xt.springcloud.entity.CommonResult;
import com.xt.springcloud.entity.Payment;
import com.xt.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class CircleBreakerController {

    @Value("${service-url.nacos-user-service}")
    private String serviceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "/consumer/fallback/{id}")
//    @SentinelResource(value = "fallback")
//    @SentinelResource(value = "fallback", fallback = "handlerFallback") // 只负责业务异常
//    @SentinelResource(value = "fallback", blockHandler = "blockHandler") // 只负责sentinel控制台配置违规
//    @SentinelResource(value = "fallback", fallback = "handlerFallback", blockHandler = "blockHandler") // 被限流降级而抛出BlockException时只会进入blockHandler
    @SentinelResource(value = "fallback", fallback = "handlerFallback", blockHandler = "blockHandler",
            exceptionsToIgnore = IllegalArgumentException.class) // 假如报该异常，不再有fallback方法兜底，没有降级效果
    public CommonResult<Payment> fallback(@PathVariable("id") Long id) {
        CommonResult<Payment> result = restTemplate.getForObject(serviceUrl + "/paymentSQL/" + id, CommonResult.class);
        if (id == 4) {
            throw new IllegalArgumentException("IllegalArgumentException, 非法参数异常：4");
        } else if (result.getData() == null) {
            throw new NullPointerException("NullPointerException, 空指针异常，该ID没有对应的记录：" + id);
        }
        return result;
    }

    // fallback
    public CommonResult<Payment> handlerFallback(@PathVariable("id") Long id, Throwable e) {
        Payment payment = new Payment(id, null);
        return new CommonResult<>(444, "兜底异常handlerFallback, exception内容：" + e.getMessage(), payment);
    }

    // blockHandler
    public CommonResult<Payment> blockHandler(@PathVariable("id") Long id, BlockException e) {
        Payment payment = new Payment(id, null);
        return new CommonResult<>(445, "blockHandler-sentinel限流，无此流水号, BlockException：" + e.getMessage(), payment);
    }

    @GetMapping("/consumer/paymentFeign/{id}")
    public CommonResult<Payment> paymentFeign(@PathVariable("id") Long id) {
        return paymentService.paymentSQL(id);
    }
}
