package com.ispan.hestia.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.hestia.dao.OrderDAO;
import com.ispan.hestia.dto.SalesNumbersDTO;
import com.ispan.hestia.model.Order;
import com.ispan.hestia.model.State;

public interface OrderRepository extends JpaRepository<Order, Integer>, OrderDAO {
	@Query("from Order o join o.state s where s.stateId = 30 and o.date < :timeMinusTwoMinutes")
	public List<Order> findUnpaidOrders(@Param("timeMinusTwoMinutes") Date timeMinusTwoMinutes);

	@Query("select date from Order where orderId = :orderId")
	public Date checkOrderDate(@Param("orderId") int orderId);

	@Modifying
	@Query("UPDATE Order o SET o.state = :postState WHERE o.date < :timeMinusTwoMinutes AND o.state = :preState")
	int updateUnpaidOrderState(@Param("timeMinusTwoMinutes") Date timeMinusTwoMinutes,
			@Param("preState") State preState,
			@Param("postState") State postState);

	@Query("SELECT ('Total', SUM(od.purchasedPrice), COUNT(od.orderRoomId)) " +
			"FROM Order o " +
			"JOIN o.orderDetails od " +
			"JOIN od.roomAvailableDate rad " +
			"JOIN rad.room r " +
			"JOIN r.provider p " +
			"JOIN od.state s " +
			"WHERE s.stateId = 38 AND p.providerId = :providerId " +
			"AND rad.availableDates BETWEEN :startDate AND :endDate")
	public List<Object[]> getTotalSalesAndOrders(@Param("startDate") Date startDate,
			@Param("endDate") Date endDate,
			@Param("providerId") Integer providerId);
	// @Query()
	// public void completeThePurchase(Integer orderId);

	// @Query("from Order where userId = :userID")
	// public List<Order> findOrdersForUsers(@Param("userID")Integer userId);
	//
	// @Query("select o.orderId,od.orderDetailId from OrderDetails od Join od.order
	// o where userId = :userID")
	// public List<Object[]>findOrdersForProvider();
	// @Query("SELECT " +
	// "o.orderId AS orderId, " +
	// "o.date AS date, " +
	// "od.checkInDate AS checkInDate, " +
	// "od.purchasedPrice AS price, " +
	// "u.name AS username, " +
	// "pu.name AS providerName, " +
	// "s.stateContent AS state " +
	// "FROM " +
	// "Order o " +
	// "JOIN " +
	// "o.orderDetails od " +
	// "JOIN " +
	// "od.roomAvailableDate rad " +
	// "JOIN " +
	// "rad.room r " +
	// "JOIN " +
	// "o.state s " +
	// "JOIN " +
	// "o.user u " +
	// "JOIN " +
	// "r.provider p " +
	// "JOIN " +
	// "p.user pu " +
	// "WHERE " +
	// "o.user.id = :userId " ) // + "AND s.id = :stateId")
	// public List<Object[]> getOrderDetailsByUserId(@Param("userId")Integer
	// userId);

	// @Query(value ="SELECT o.order_id,o.[date],od.check_in_date,od.purchased_price
	// AS price,u.name AS username,pu.name AS provider_name,s.state_content AS
	// state"
	// +"FROM [orders] o "
	// + "JOIN order_details od ON o.order_id = od.order_id"
	// + "JOIN room_available_date rad ON od.room_available_date_id =
	// rad.room_available_date_id "
	// + "JOIN room r ON rad.room_id = r.room_id"
	// + "JOIN [state] s ON o.state_id = s.state_id JOIN [users] u ON o.[user_id] =
	// u.[user_id] "
	// + "JOIN [provider] p ON r.provider_id = p.provider_id"
	// +"JOIN order_details od ON o.order_id = od.order_id\r\n"
	// + "JOIN room_available_date rad ON od.room_available_date_id =
	// rad.room_available_date_id "
	// + "JOIN room r ON rad.room_id = r.room_id "
	// + "JOIN [state] s ON o.state_id = s.state_id"
	// + "JOIN [user] u ON o.[user_id] = u.[user_id]"
	// + "JOIN [provider] p ON r.provider_id = p.provider_id"
	// + "JOIN [user] pu ON p.[user_id] = pu.[user_id]\r\n"
	// + " WHERE o.[user_id] = :userId", nativeQuery = true)// + "AND s.id =
	// :stateId")
}
