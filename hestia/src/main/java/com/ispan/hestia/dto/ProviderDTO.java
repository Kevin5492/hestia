package com.ispan.hestia.dto;

import java.util.Date;

public record ProviderDTO(
        Integer orderId,
        Date availableDates,
        Date checkInDate,
        Integer purchasedPrice,
        String userName,
        String roomName,
        String state) {

}
