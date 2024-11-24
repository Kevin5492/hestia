package com.ispan.hestia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ispan.hestia.model.OrderDetails;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {

}
