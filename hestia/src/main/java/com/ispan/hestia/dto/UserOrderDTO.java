package com.ispan.hestia.dto;

import java.util.Date;

public record UserOrderDTO(
        Integer orderId,
        Date availableDates,
        Date checkInDate,
        Integer purchasedPrice,
        String providerName,
        String roomName,
        String state) {

}
