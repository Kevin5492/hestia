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
            if (orderService.updateOrderStateToSuccess(orderId)) {
                return new OrderReponse(true, "付款成功", null);
            }
            return new OrderReponse(false, "付款失敗", null);

        } catch (Exception e) {
            e.printStackTrace();
            return new OrderReponse(false, "付款失敗", null);
        }
    }

    @PostMapping("path2")
    public OrderReponse findOrdersUser(@RequestBody String entity) {

        return new OrderReponse(true, null, null);
    }

    // String startSearchDateStr = obj.isNull("startSearchDate") ? null :
    // obj.getString("startSearchDate");

    // DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    // LocalDateTime startSearchDateTime = LocalDateTime.parse(startSearchDateStr,
    // formatter);
    // Date startSearchDate = java.sql.Timestamp.valueOf(startSearchDateTime);

}
