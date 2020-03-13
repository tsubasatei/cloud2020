package com.xt.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface LoadBalancer {

    // 从所有服务实例中选择要访问的服务实例
    ServiceInstance instances(List<ServiceInstance> serviceInstances);
}
