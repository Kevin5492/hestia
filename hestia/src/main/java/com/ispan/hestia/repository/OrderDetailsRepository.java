package com.ispan.hestia.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.hestia.dto.OrderDetailsDTO;
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

        @Query("SELECT new com.ispan.hestia.dto.OrderDetailsDTO(o.id, od.checkInDate, r.roomName, od.purchasedPrice,r.singleBed, r.doubleBed, r.bedroomCount, r.chechinTime, r.chechoutTime) "
                        +
                        "FROM Order o JOIN o.orderDetails od " +
                        "JOIN od.roomAvailableDate rad " +
                        "JOIN rad.room r JOIN od.state s " +
                        "WHERE o.id = :orderId")
        List<OrderDetailsDTO> findOrderDetailsByOrderId(@Param("orderId") Integer orderId);

}
