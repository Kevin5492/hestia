package com.ispan.hestia.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.ispan.hestia.dao.OrderDAO;
import com.ispan.hestia.dto.ProviderDTO;
import com.ispan.hestia.dto.SalesNumbersDTO;
import com.ispan.hestia.dto.UserOrderDTO;
import com.ispan.hestia.model.City;
import com.ispan.hestia.model.Order;
import com.ispan.hestia.model.OrderDetails;
import com.ispan.hestia.model.Provider;
import com.ispan.hestia.model.Room;
import com.ispan.hestia.model.RoomAvailableDate;
import com.ispan.hestia.model.State;
import com.ispan.hestia.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class OrderDAOImpl implements OrderDAO {
	@PersistenceContext
	private EntityManager entityManager;

	// 利用時間區間提供報表
	public List<SalesNumbersDTO> getMonthlySalesAndOrdersAvailableDate(Date startDate, Date endDate,
			Integer providerId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SalesNumbersDTO> criteriaQuery = criteriaBuilder.createQuery(SalesNumbersDTO.class);

		Root<Order> orderRoot = criteriaQuery.from(Order.class);
		Join<Order, OrderDetails> odJoin = orderRoot.join("orderDetails");
		Join<OrderDetails, RoomAvailableDate> radJoin = odJoin.join("roomAvailableDate");
		Join<RoomAvailableDate, Room> roomJoin = radJoin.join("room");
		Join<Room, Provider> providerJoin = roomJoin.join("provider");
		Join<Order, State> stateJoin = odJoin.join("state");

		Expression<String> monthExpression = criteriaBuilder.function("FORMAT", String.class,
				radJoin.get("availableDates"),
				criteriaBuilder.literal("yyyy-MM"));
		// criteriaQuery.multiselect(monthExpression.alias("month"),
		// criteriaBuilder.sum(odJoin.get("purchasedPrice")).alias("totalSales"),
		// criteriaBuilder.count(odJoin.get("orderRoomId")).alias("orderCount"));

		criteriaQuery.select(criteriaBuilder.construct(
				SalesNumbersDTO.class,
				monthExpression.alias("month"), // 必須與 DTO 的參數名或別名對應
				criteriaBuilder.sum(odJoin.get("purchasedPrice")).alias("totalSales"),
				criteriaBuilder.count(odJoin.get("orderRoomId")).alias("orderCount")));

		Predicate statePredicate = criteriaBuilder.equal(stateJoin.get("stateId"), 38);
		Predicate providerPredicate = criteriaBuilder.equal(providerJoin.get("providerId"), providerId);
		Predicate datePredicate = criteriaBuilder.conjunction(); // 初始條件為 TRUE
		if (startDate != null) {
			datePredicate = criteriaBuilder.and(datePredicate,
					criteriaBuilder.greaterThanOrEqualTo(radJoin.get("availableDates"), startDate));
		}
		if (endDate != null) {
			datePredicate = criteriaBuilder.and(datePredicate,
					criteriaBuilder.lessThanOrEqualTo(radJoin.get("availableDates"), endDate));
		}
		criteriaQuery.where(criteriaBuilder.and(datePredicate, providerPredicate, statePredicate));

		criteriaQuery.groupBy(monthExpression);

		// 按月份排序
		criteriaQuery.orderBy(criteriaBuilder.asc(monthExpression));

		// 執行查詢並返回結果
		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	// @Override
	// public List<Object[]> getMonthlySalesAndOrdersByOrderDate(Date startDate,
	// Date endDate, Integer providerId) {
	// CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	// CriteriaQuery<Object[]> criteriaQuery =
	// criteriaBuilder.createQuery(Object[].class);
	// Root<Order> orderRoot = criteriaQuery.from(Order.class);
	// Join<Order, OrderDetails> odJoin = orderRoot.join("orderDetails");
	// Join<OrderDetails, RoomAvailableDate> radJoin =
	// odJoin.join("roomAvailableDate");
	// Join<RoomAvailableDate, Room> roomJoin = radJoin.join("room");
	// Join<Room, Provider> providerJoin = roomJoin.join("provider");
	// Join<Order, State> stateJoin = orderRoot.join("state");

	// Expression<String> monthExpression = criteriaBuilder.function("FORMAT",
	// String.class, orderRoot.get("date"),
	// criteriaBuilder.literal("yyyy-MM"));
	// criteriaQuery.multiselect(monthExpression.alias("month"),
	// criteriaBuilder.sum(odJoin.get("purchasedPrice")).alias("totalSales"),
	// criteriaBuilder.count(odJoin.get("orderRoomId")).alias("totalOrders"));
	// Predicate statePredicate = criteriaBuilder.equal(stateJoin.get("stateId"),
	// 38);
	// Predicate providerPredicate =
	// criteriaBuilder.equal(providerJoin.get("providerId"), providerId);
	// Predicate datePredicate = criteriaBuilder.conjunction(); // 初始條件為 TRUE
	// if (startDate != null) {
	// datePredicate = criteriaBuilder.and(datePredicate,
	// criteriaBuilder.greaterThanOrEqualTo(orderRoot.get("date"), startDate));
	// }
	// if (endDate != null) {
	// datePredicate = criteriaBuilder.and(datePredicate,
	// criteriaBuilder.lessThanOrEqualTo(orderRoot.get("date"), endDate));
	// }
	// criteriaQuery.where(criteriaBuilder.and(datePredicate, providerPredicate));

	// criteriaQuery.groupBy(monthExpression);

	// // 按月份排序
	// criteriaQuery.orderBy(criteriaBuilder.asc(monthExpression));

	// // 執行查詢並返回結果
	// return entityManager.createQuery(criteriaQuery).getResultList();
	// }

	@Override
	public Page<UserOrderDTO> findOrderForUserPage(Date startDate, Date endDate, Integer userId, Integer stateId,
			String searchInput, Integer pageNumber, Integer pageSize) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserOrderDTO> criteriaQuery = criteriaBuilder.createQuery(UserOrderDTO.class);

		Root<Order> orderRoot = criteriaQuery.from(Order.class);
		Join<Order, OrderDetails> odJoin = orderRoot.join("orderDetails");
		Join<OrderDetails, RoomAvailableDate> radJoin = odJoin.join("roomAvailableDate");
		Join<RoomAvailableDate, Room> roomJoin = radJoin.join("room");
		Join<Order, State> stateJoin = orderRoot.join("state");
		Join<Order, User> userJoin = orderRoot.join("user");
		Join<Room, Provider> providerJoin = roomJoin.join("provider");
		Join<Provider, User> providerUserNameJoin = providerJoin.join("user");
		Join<Room, City> cityJoin = roomJoin.join("city");

		String search = (searchInput != null && !searchInput.trim().isEmpty()) ? "%" + searchInput.trim() + "%" : null;

		criteriaQuery.select(criteriaBuilder.construct(
				UserOrderDTO.class,
				orderRoot.get("orderId").alias("orderId"),
				radJoin.get("availableDates").alias("availableDates"),
				odJoin.get("checkInDate").alias("checkInDate"),
				odJoin.get("purchasedPrice").alias("purchasedPrice"),
				providerUserNameJoin.get("name").alias("providerName"),
				roomJoin.get("roomName").alias("roomName"),
				stateJoin.get("stateContent").alias("state")));

		Predicate userPredicate = criteriaBuilder.equal(userJoin.get("userId"), userId);

		Predicate statePredicate = stateId != null
				? criteriaBuilder.equal(stateJoin.get("stateId"), stateId)
				: criteriaBuilder.conjunction();

		Predicate datePredicate = criteriaBuilder.conjunction();
		if (startDate != null) {
			datePredicate = criteriaBuilder.and(datePredicate,
					criteriaBuilder.greaterThanOrEqualTo(orderRoot.get("date"), startDate));
		}
		if (endDate != null) {
			datePredicate = criteriaBuilder.and(datePredicate,
					criteriaBuilder.lessThanOrEqualTo(orderRoot.get("date"), endDate));
		}

		Predicate searchPredicate = criteriaBuilder.conjunction();
		if (search != null) {
			searchPredicate = criteriaBuilder.or(
					criteriaBuilder.like(roomJoin.get("roomName"), search),
					criteriaBuilder.like(cityJoin.get("cityName"), search),
					criteriaBuilder.like(roomJoin.get("roomAddr"), search),
					criteriaBuilder.like(roomJoin.get("roomContent"), search));
		}

		criteriaQuery.where(criteriaBuilder.and(userPredicate, statePredicate, datePredicate, searchPredicate));

		// 分頁查詢
		TypedQuery<UserOrderDTO> query = entityManager.createQuery(criteriaQuery);
		query.setFirstResult(pageNumber * pageSize);
		query.setMaxResults(pageSize);
		List<UserOrderDTO> results = query.getResultList();

		// 計數查詢
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<Order> countRoot = countQuery.from(Order.class);
		countQuery.select(criteriaBuilder.count(countRoot));
		countQuery.where(criteriaQuery.getRestriction());
		Long totalElements = entityManager.createQuery(countQuery).getSingleResult();

		return new PageImpl<>(results, PageRequest.of(pageNumber, pageSize), totalElements);

	}

	@Override
	public List<UserOrderDTO> findOrderForUser(Date startDate, Date endDate, Integer userId, Integer stateId,
			String searchInput) { // List<Object[]>
		// 會回傳userID
		// availableDates
		// checkInDate
		// purchasedPrice
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserOrderDTO> criteriaQuery = criteriaBuilder.createQuery(UserOrderDTO.class);

		Root<Order> orderRoot = criteriaQuery.from(Order.class);
		Join<Order, OrderDetails> odJoin = orderRoot.join("orderDetails");
		Join<OrderDetails, RoomAvailableDate> radJoin = odJoin.join("roomAvailableDate");
		Join<RoomAvailableDate, Room> roomJoin = radJoin.join("room");
		Join<Order, State> stateJoin = orderRoot.join("state");
		Join<Order, User> userJoin = orderRoot.join("user");
		Join<Room, Provider> providerJoin = roomJoin.join("provider");
		Join<Provider, User> providerUserNameJoin = providerJoin.join("user");
		Join<Room, City> cityJoin = roomJoin.join("city");

		// criteriaQuery.select(criteriaBuilder.construct(null));
		// criteriaQuery.multiselect(orderRoot.get("orderId"),
		// radJoin.get("availableDates"), odJoin.get("checkInDate"),
		// odJoin.get("purchasedPrice").alias("price"),
		// // userJoin.get("name").alias("username"),
		// providerUserNameJoin.get("name").alias("providerName"),
		// roomJoin.get("roomName"),
		// stateJoin.get("stateContent").alias("state"));
		String search = "%" + searchInput + "%";
		criteriaQuery.select(criteriaBuilder.construct(
				UserOrderDTO.class,
				orderRoot.get("orderId").alias("orderId"),
				radJoin.get("availableDates").alias("availableDates"),
				odJoin.get("checkInDate").alias("checkInDate"),
				odJoin.get("purchasedPrice").alias("purchasedPrice"),
				providerUserNameJoin.get("name").alias("providerName"),
				roomJoin.get("roomName").alias("roomName"),
				stateJoin.get("stateContent").alias("state")));

		Predicate userPredicate = criteriaBuilder.equal(userJoin.get("userId"), userId);

		Predicate statePredicate = criteriaBuilder.conjunction();
		Predicate datePredicate = criteriaBuilder.conjunction(); // 初始化為真條件
		Predicate roomNamePredicate = criteriaBuilder.conjunction();
		Predicate cityNamePredicate = criteriaBuilder.conjunction();
		Predicate addressPredicate = criteriaBuilder.conjunction();
		Predicate roomContentPredicate = criteriaBuilder.conjunction();

		if (searchInput != null) {
			roomNamePredicate = criteriaBuilder.like(roomJoin.get("roomName"), search);
			addressPredicate = criteriaBuilder.like(roomJoin.get("roomAddr"), search);
			roomContentPredicate = criteriaBuilder.like(roomJoin.get("roomContent"), search);
			cityNamePredicate = criteriaBuilder.like(cityJoin.get("cityName"), search);
		}

		if (stateId != null) {
			statePredicate = criteriaBuilder.equal(stateJoin.get("stateId"), stateId);
		}
		// 如果 startDate 不為空，加入 "大於或等於" 條件
		if (startDate != null) {
			datePredicate = criteriaBuilder.and(datePredicate,
					criteriaBuilder.greaterThanOrEqualTo(orderRoot.get("date"), startDate));
		}

		// 如果 endDate 不為空，加入 "小於或等於" 條件
		if (endDate != null) {
			datePredicate = criteriaBuilder.and(datePredicate,
					criteriaBuilder.lessThanOrEqualTo(orderRoot.get("date"), endDate));
		}
		Predicate orPredicate = criteriaBuilder.or(
				roomNamePredicate,
				cityNamePredicate,
				addressPredicate,
				roomContentPredicate);

		criteriaQuery.where(criteriaBuilder.and(userPredicate, statePredicate, datePredicate, orPredicate));
		// criteriaQuery.groupBy(orderRoot.get("orderId"));
		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public List<ProviderDTO> findOrderForProvider(Date startDate, Date endDate, Integer providerId, Integer stateId) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProviderDTO> criteriaQuery = criteriaBuilder.createQuery(ProviderDTO.class);

		Root<Order> orderRoot = criteriaQuery.from(Order.class);
		Join<Order, OrderDetails> odJoin = orderRoot.join("orderDetails");
		Join<OrderDetails, RoomAvailableDate> radJoin = odJoin.join("roomAvailableDate");
		Join<RoomAvailableDate, Room> roomJoin = radJoin.join("room");
		Join<Order, State> stateJoin = orderRoot.join("state");
		Join<Order, User> userJoin = orderRoot.join("user");
		Join<Room, Provider> providerJoin = roomJoin.join("provider");
		Join<Provider, User> providerUserNameJoin = providerJoin.join("user");

		// criteriaQuery.multiselect(orderRoot.get("orderId"),
		// radJoin.get("availableDates"), odJoin.get("checkInDate"),
		// odJoin.get("purchasedPrice").alias("price"),
		// userJoin.get("name").alias("username"),
		// providerUserNameJoin.get("name"),
		// stateJoin.get("stateContent").alias("state"));

		criteriaQuery.select(criteriaBuilder.construct(
				ProviderDTO.class,
				orderRoot.get("orderId").alias("orderId"),
				radJoin.get("availableDates").alias("availableDates"),
				odJoin.get("checkInDate").alias("checkInDate"),
				odJoin.get("purchasedPrice").alias("purchasedPrice"),
				userJoin.get("name").alias("userName"),
				roomJoin.get("roomName").alias("roomName"),
				stateJoin.get("stateContent").alias("state")));
		Predicate providerPredicate = criteriaBuilder.equal(providerJoin.get("providerId"), providerId);
		Predicate statePredicate = criteriaBuilder.conjunction();
		Predicate datePredicate = criteriaBuilder.conjunction(); // 初始化為真條件

		if (stateId != null) {
			statePredicate = criteriaBuilder.equal(stateJoin.get("stateId"), stateId);
		}
		// 如果 startDate 不為空，加入 "大於或等於" 條件
		if (startDate != null) {
			datePredicate = criteriaBuilder.and(datePredicate,
					criteriaBuilder.greaterThanOrEqualTo(orderRoot.get("date"), startDate));
		}

		// 如果 endDate 不為空，加入 "小於或等於" 條件
		if (endDate != null) {
			datePredicate = criteriaBuilder.and(datePredicate,
					criteriaBuilder.lessThanOrEqualTo(orderRoot.get("date"), endDate));
		}

		criteriaQuery.where(criteriaBuilder.and(providerPredicate, statePredicate, datePredicate));
		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	// @Override
	// public void completeThePurchase(Integer orderId,State state) {
	// CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	// CriteriaUpdate<Order> criteriaUpdate =
	// criteriaBuilder.createCriteriaUpdate(Order.class);
	// Root<Order> orderRoot = criteriaUpdate.from(Order.class);
	// }
	// @Override
	// public List<Object[]> getOrderDetailsByUserId(Integer userId){
	// CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	// CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
	// Root<Order> order = query.from(Order.class);
	//
	// // Join tables
	// Join<Order, OrderDetails> orderDetails = order.join("orderDetails");
	// Join<OrderDetails, RoomAvailableDate> roomAvailableDate =
	// orderDetails.join("roomAvailableDate");
	// Join<RoomAvailableDate, Room> room = roomAvailableDate.join("room");
	// Join<Order, State> state = order.join("state");
	// Join<Order, User> user = order.join("user");
	// Join<Room, Provider> provider = room.join("provider");
	// Join<Provider, User> providerUser = provider.join("user");
	//
	// // Select fields
	// query.multiselect(
	// order.get("orderId"),
	// order.get("date"),
	// orderDetails.get("checkInDate"),
	// orderDetails.get("purchasedPrice"),
	// user.get("name"),
	// providerUser.get("name"),
	// state.get("stateContent")
	// );
	//
	// // Where clause
	// query.where(cb.equal(user.get("id"), userId));
	//
	// List<Object[]> results = entityManager.createQuery(query).getResultList();
	// return results;
	// }

	// @Override
	// public List<Object[]> getOrderDetailsByProviderId(Integer providerId) {
	// // 1. 獲取 CriteriaBuilder 和 CriteriaQuery
	// CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	// CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
	//
	// // 2. 定義根實體和 Join
	// Root<Order> orderRoot = query.from(Order.class);
	// Join<Order, OrderDetails> odJoin = orderRoot.join("orderDetails");
	// Join<OrderDetails, RoomAvailableDate> radJoin =
	// odJoin.join("availableDates");
	// Join<RoomAvailableDate, Room> roomJoin = radJoin.join("room");
	// Join<Order, State> stateJoin = orderRoot.join("state");
	// Join<Order, User> userJoin = orderRoot.join("user");
	//
	// // 3. 選擇查詢的欄位
	// query.multiselect(
	// orderRoot.get("orderId"),
	// radJoin.get("date"),
	// radJoin.get("checkInDate"),
	// odJoin.get("purchasedPrice").alias("price"),
	// userJoin.get("name").alias("username"),
	// roomJoin.get("roomName").alias("roomName"),
	// stateJoin.get("stateContent").alias("state")
	// );
	//
	// // 4. 設定查詢條件
	// query.where(cb.equal(roomJoin.get("providerId"), providerId));
	//
	// // 5. 執行查詢並返回結果
	// return entityManager.createQuery(query).getResultList();
	// }

}
