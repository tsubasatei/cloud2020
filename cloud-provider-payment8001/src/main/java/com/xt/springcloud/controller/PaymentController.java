package com.xt.springcloud.controller;

import com.xt.springcloud.entity.CommonResult;
import com.xt.springcloud.entity.Payment;
import com.xt.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private DiscoveryClient discoveryClient;

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

    @GetMapping("/payment/discovery")
    public Object discovery () {
        List<String> services = discoveryClient.getServices();
        services.forEach((element) -> log.info("***element:" + element));  // cloud-payment-service

        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        instances.forEach((instance) -> log.info(instance.getServiceId() + "\t"  // CLOUD-PAYMENT-SERVICE
                + instance.getInstanceId() + "\t"  // payment8001
                + instance.getHost() + "\t"   // 192.168.239.1
                + instance.getPort() + "\t"   // 8001
                + instance.getUri() + "\n"   // http://192.168.239.1:8001
        ));
        return discoveryClient;
    }

    @GetMapping("/payment/lb")
    public String getPaymentLB() {
        return serverPort;
    }

    @GetMapping("/payment/feign/timeout")
    public String paymentFeignTimeout() {
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
        return serverPort;
    }
}
