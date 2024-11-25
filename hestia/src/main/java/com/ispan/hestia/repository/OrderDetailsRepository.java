package com.ispan.hestia.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.hestia.model.OrderDetails;
import com.ispan.hestia.model.State;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {

    @Modifying
    @Query("UPDATE OrderDetails od SET od.state = :postState WHERE od.order.id = :orderId AND od.state = :preState")
    int updateOrderDetailsState(@Param("orderId") Integer orderId,
            @Param("preState") State preState,
            @Param("postState") State postState);

    @Modifying
    @Query("UPDATE OrderDetails od SET od.state = :postState WHERE od.order.date < :timeMinusTwoMinutes AND od.state = :preState")
    int updateUnpaidOrderDetailsState(@Param("timeMinusTwoMinutes") Date timeMinusTwoMinutes,
            @Param("preState") State preState,
            @Param("postState") State postState);

    @Query("select od.order.date from OrderDetails od where orderRoomId = :orderRoomId")
    public Date checkOrderDate(@Param("orderRoomId") int orderRoomId);

}
