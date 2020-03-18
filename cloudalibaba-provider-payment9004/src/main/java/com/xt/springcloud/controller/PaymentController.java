package com.xt.springcloud.controller;

import com.xt.springcloud.entity.CommonResult;
import com.xt.springcloud.entity.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    public static HashMap<Long, Payment> hashMap = new HashMap<>();

    static {
        hashMap.put(1L, new Payment(1L, "34ff0ce9e5f346db8fdebc54eee5838b"));
        hashMap.put(2L, new Payment(2L, "9de8bb40a7f0448682c1c664de7fb785"));
        hashMap.put(3L, new Payment(3L, "c2ad21cf8d1d4eafa028a8eb8f7631a5"));
    }

    @GetMapping("/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id) {
        Payment payment = hashMap.get(id);
        return new CommonResult<>(200, "from mysql, serverPort:" + serverPort, payment);
    }

    /*public static void main(String[] args) {
        String string = UUID.randomUUID().toString().replace("-", "");
        System.out.println(string);
    }*/
}
