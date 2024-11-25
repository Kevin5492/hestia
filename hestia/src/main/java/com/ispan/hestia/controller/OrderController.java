package com.ispan.hestia.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.hestia.dto.OrderReponse;
import com.ispan.hestia.service.OrderService;

@RestController
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("path")
    public OrderReponse orderComplete(@RequestBody String entity) {
        try {
            JSONObject obj = new JSONObject(entity);
            Integer orderId = obj.isNull("orderId") ? null : obj.getInt("orderId");
            orderService.updateOrderStateToSuccess(orderId);

            return new OrderReponse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // String startSearchDateStr = obj.isNull("startSearchDate") ? null :
    // obj.getString("startSearchDate");

    // DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    // LocalDateTime startSearchDateTime = LocalDateTime.parse(startSearchDateStr,
    // formatter);
    // Date startSearchDate = java.sql.Timestamp.valueOf(startSearchDateTime);

}
