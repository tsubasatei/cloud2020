package com.xt.springcloud.controller;

import com.xt.springcloud.lb.LoadBalancer;
import com.xt.springcloud.entity.CommonResult;
import com.xt.springcloud.entity.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    private DiscoveryClient discoveryClient;

//    public static final String PAYMENT_URL = "http://localhost:8001";
    // 通过在eureka上注册过的微服务名称调用
    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";

//    @Autowired
//    private LoadBalancer loadBalancer;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment) {
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
    }

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id) {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
    }

    @PostMapping("/consumer/payment/createForEntity")
    public CommonResult<Payment> createForEntity(Payment payment) {
        ResponseEntity<CommonResult> responseEntity = restTemplate.postForEntity(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
        return responseEntity.getBody();
    }

    @GetMapping("/consumer/payment/getForEntity/{id}")
    public CommonResult<Payment> getPaymentForEntity(@PathVariable("id") Long id) {
        ResponseEntity<CommonResult> responseEntity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        }
        return new CommonResult<>(444, "ForEntity查询失败，id：" + id);
    }

    /*@GetMapping("/consumer/payment/lb")
    public String getPaymentLB() {
//        return restTemplate.getForObject(PAYMENT_URL + "/payment/lb", String.class);
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if (instances == null || instances.size() == 0) {
            return null;
        }
        ServiceInstance serviceInstance = loadBalancer.instances(instances);
        URI uri = serviceInstance.getUri();
        return restTemplate.getForObject(uri + "/payment/lb", String.class);
    }*/

    @GetMapping("/consumer/payment/zipkin")
    public String paymentZipkin() {

        return restTemplate.getForObject(PAYMENT_URL + "/payment/zipkin", String.class);
    }
}
