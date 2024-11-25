package com.ispan.hestia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class SchedulerService {

    @Autowired
    OrderService orderService;

    @Scheduled(fixedRate = 1000)
    public void cancelOrder() {
        orderService.automaticallyCancelOrders();
    }
}
