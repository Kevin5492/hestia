package com.ispan.hestia.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ispan.hestia.model.Order;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderReponse(
        boolean success,
        String message,
        List<SalesNumbersDTO> salesNumbers,
        List<UserOrderDTO> userOrders,
        List<ProviderDTO> providerOrders,
        List<OrderDetailsDTO> orderDetails) {
}
