package com.ispan.hestia.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.hestia.model.RoomAvailableDate;
import com.ispan.hestia.model.State;

public interface RoomAvailableDateRepository extends JpaRepository<RoomAvailableDate, Integer> {

    // @Query("Update RoomAvailableDate rad set rad.roomSum = rad.roomSum + 1 where
    // rad.orderDetails.order.date < :timeMinusTwoMinutes and rad.orderDetails.state
    // = :preState")
    // int updateRoomSum(@Param("timeMinusTwoMinutes") Date timeMinusTwoMinutes,
    // @Param("preState") State preState);

}