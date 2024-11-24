package com.ispan.hestia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.hestia.repository.OrderDetailsRepository;
import com.ispan.hestia.repository.OrderRepository;

@Service
public class OrderDetailService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderDetailsRepository orderDetailsRepo;
}
