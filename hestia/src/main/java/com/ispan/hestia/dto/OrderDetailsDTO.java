package com.ispan.hestia.dto;

import java.util.Date;

public record OrderDetailsDTO(
        Integer orderId, Date checkInDate,
        String roomName, Integer purchasedPrice, Integer singleBed,
        Integer doubleBed, Integer bedroomCount, Double chechinTime, Double chechoutTime) {

}
