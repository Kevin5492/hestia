package com.ispan.hestia.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.hestia.dto.SalesNumbersDTO;
import com.ispan.hestia.model.Order;
import com.ispan.hestia.model.OrderDetails;
import com.ispan.hestia.model.OrderRefundRecord;
import com.ispan.hestia.model.RoomAvailableDate;
import com.ispan.hestia.model.State;
import com.ispan.hestia.repository.OrderDetailsRepository;
import com.ispan.hestia.repository.OrderRefundRecordRepository;
import com.ispan.hestia.repository.OrderRepository;
import com.ispan.hestia.repository.RoomAvailableDateRepository;
import com.ispan.hestia.repository.StateRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private StateRepository stateRepo;

	@Autowired
	private OrderDetailsRepository orderDetailsRepo;

	@Autowired
	private RoomAvailableDateRepository roomADRepo;

	@Autowired
	private OrderRefundRecordRepository orrRepo;

	@Transactional
	public void automaticallyCancelOrders() {// 定期將未付款的訂單改為未付款取消 之後可以把更新OrderDetail改寫
		Date currentTime = new Date();
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(currentTime);
		// calendar.add(Calendar.MINUTE, -2);
		// Date timeMinusTwoMinutes = calendar.getTime();
		Date timeMinusTwoMinutes = new Date(currentTime.getTime() - 2 * 60 * 1000);
		System.out.println("timeMinusTwoMinutes" + timeMinusTwoMinutes);
		System.out.println("currentTime" + currentTime);

		List<Order> unpaidOrders = orderRepo.findUnpaidOrders(timeMinusTwoMinutes);

		for (Order unpaidOrder : unpaidOrders) {
			for (OrderDetails orderDetail : unpaidOrder.getOrderDetails()) {
				RoomAvailableDate roomAvailableDate = orderDetail.getRoomAvailableDate();
				roomAvailableDate.setRoomSum(roomAvailableDate.getRoomSum() + 1);
				// 訂單被取消後要把房間加回去
				roomADRepo.save(roomAvailableDate);
			}
		}
		State uppaidState = stateRepo.findById(30).get(); // 找到未付款狀態
		State uppaidCancelState = stateRepo.findById(33).get(); // 找到未付款取消

		// roomADRepo.updateRoomSum(timeMinusTwoMinutes, uppaidState);// 把房間加回去

		orderRepo.updateUnpaidOrderState(timeMinusTwoMinutes, uppaidState, uppaidCancelState);// 更新所有未付款訂單

		orderDetailsRepo.updateUnpaidOrderDetailsState(timeMinusTwoMinutes, uppaidState, uppaidCancelState); // 更新所有未付款詳細訂單

		// for (Order unpaidOrder : unpaidOrders) {
		// unpaidOrder.setState(state);

		// Set<OrderDetails> orderDetails = unpaidOrder.getOrderDetails();
		// for (OrderDetails orderDetail : orderDetails) {
		// RoomAvailableDate roomAvailableDate = orderDetail.getRoomAvailableDate();
		// roomAvailableDate.setRoomSum(roomAvailableDate.getRoomSum() + 1);
		// // 訂單被取消後要把房間加回去
		// roomADRepo.save(roomAvailableDate);

		// orderDetail.setState(state);
		// orderDetailsRepo.save(orderDetail);
		// }
		// orderRepo.save(unpaidOrder);
		// }
	}

	@Transactional
	public boolean updateOrderStateToSuccess(Integer orderId) {// 完成付款 把狀態改為成功
		try {
			// 確認訂單是否存在
			Optional<Order> orderOptional = orderRepo.findById(orderId);
			if (orderOptional.isEmpty()) {
				System.out.println("is empty");
				return false;
			}

			// 獲取目標狀態
			State state = stateRepo.findById(38).get();

			// 更新訂單及其詳細信息的狀態
			Order order = orderOptional.get();
			System.out.println("order id: " + order.getOrderId());
			order.setState(state);
			for (OrderDetails orderDetail : order.getOrderDetails()) {
				orderDetail.setState(state);
				orderDetailsRepo.save(orderDetail);
			}

			// 保存訂單（會自動級聯保存 OrderDetails）
			orderRepo.save(order);

			return true;

		} catch (Exception e) {
			// 記錄錯誤，避免無處理的例外
			e.printStackTrace();
			return false;
		}
	}

	// @Transactional
	// public void updateOrderStateToSuccess(Integer orderId) {// 完成付款 把狀態改為成功
	// Optional<Order> orderOptional = orderRepo.findById(orderId);
	// if (orderOptional != null) {

	// State state = stateRepo.findById(38).get();
	// Order order = orderOptional.get();
	// Set<OrderDetails> orderDetails = order.getOrderDetails();
	// for (OrderDetails orderDetail : orderDetails) {
	// orderDetail.setState(state);
	// orderDetailsRepo.save(orderDetail);
	// }
	// order.setState(state);

	// orderRepo.save(order);

	// }
	// }

	@Transactional // 按照月份提供收入報表
	public List<SalesNumbersDTO> getMonthlySalesAndOrders(Date startDate, Date endDate, Integer providerId) {
		return orderRepo.getMonthlySalesAndOrdersAvailableDate(startDate, endDate, providerId);
	}

	@Transactional // 查詢使用者訂單
	public List<Object[]> findUserOrders(Date startDate, Date endDate, Integer userId, Integer stateId) {
		return orderRepo.findOrderForUser(startDate, endDate, userId, stateId);
	}

	@Transactional // 查詢房東訂單
	public List<Object[]> findProviderOrders(Date startDate, Date endDate, Integer providerId, Integer stateId) {
		return orderRepo.findOrderForProvider(startDate, endDate, providerId, stateId);
	}

	@Transactional // 檢查是否符合退款資格
	public boolean checkIfAutoRefundable(Integer orderId) {
		Date currentTime = new Date();
		Date orderDate = new Date(orderRepo.checkOrderDate(orderId).getTime() + 5 * 60 * 1000);// orderDate
																								// 找到訂單時間然後加5分鐘再去比較
		if (orderDate.compareTo(currentTime) < 0) {
			System.out.println("orderDate" + orderDate);
			System.out.println("currentTime" + currentTime);
			return false;
		}
		return true;
	}

	@Transactional // 申請退款 並且符合退款資格
	public boolean autoRefund(Integer orderId) {
		try {
			// 確認訂單是否存在
			Optional<Order> orderOptional = orderRepo.findById(orderId);
			if (orderOptional.isEmpty()) {
				return false;
			}
			Order order = orderOptional.get();
			State successState = stateRepo.findById(38).get();// 找到完成的狀態
			State refundingState = stateRepo.findById(35).get();// 找到退款中的狀態

			order.setState(refundingState);
			int updated = orderDetailsRepo.updateOrderDetailsState(orderId, successState, refundingState);
			System.out.println("共更新了 " + updated + " 筆");
			orderRepo.save(order);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Transactional // 申請退款 不符合退款資格 手動提出退款申請
	public boolean manualRefund(Integer orderId) {
		try {
			// 確認訂單是否存在
			Optional<Order> orderOptional = orderRepo.findById(orderId);
			if (orderOptional.isEmpty()) {
				return false;
			}
			Order order = orderOptional.get();
			State successState = stateRepo.findById(38).get();// 找到完成的狀態
			State applyingRefundState = stateRepo.findById(34).get();// 找到申請退款的狀態
			order.setState(applyingRefundState);
			orderDetailsRepo.updateOrderDetailsState(orderId, successState, applyingRefundState); // 將相應的OrderDetail
																									// 改為申請退款
			orderRepo.save(order);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Transactional // 申請退款 手動同意退款
	public boolean manualRefundAccepted(Integer orderId) {
		try {
			// 確認訂單是否存在
			Optional<Order> orderOptional = orderRepo.findById(orderId);
			if (orderOptional.isEmpty()) {
				return false;
			}
			Order order = orderOptional.get();
			State applyingRefundState = stateRepo.findById(34).get(); // 找到申請退款的狀態
			State refundingState = stateRepo.findById(35).get();// 找到退款中的狀態
			order.setState(refundingState);
			orderDetailsRepo.updateOrderDetailsState(orderId, applyingRefundState, refundingState);
			orderRepo.save(order);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Transactional // 申請退款 手動拒絕退款
	public boolean manualRefundDeclined(Integer orderId) {
		try {
			// 確認訂單是否存在
			Optional<Order> orderOptional = orderRepo.findById(orderId);
			if (orderOptional.isEmpty()) {
				return false;
			}

			Order order = orderOptional.get();
			OrderRefundRecord orderRefundRecord = new OrderRefundRecord();
			orderRefundRecord.setDate(new Date());
			orderRefundRecord.setOrder(order);
			orrRepo.save(orderRefundRecord);
			State applyingRefundState = stateRepo.findById(34).get(); // 找到申請退款的狀態
			State successState = stateRepo.findById(38).get();// 把狀態變回成功
			order.setState(successState);
			orderDetailsRepo.updateOrderDetailsState(orderId, applyingRefundState, successState);
			orderRepo.save(order);
			return true;
		} catch (Exception e) {
			System.err.println("處理訂單失敗，Order ID: " + orderId);
			e.printStackTrace();
			return false;
		}
	}
}
