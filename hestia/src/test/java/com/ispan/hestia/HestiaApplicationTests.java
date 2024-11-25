package com.ispan.hestia;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ispan.hestia.repository.OrderRepository;
import com.ispan.hestia.service.OrderService;

@SpringBootTest
class HestiaApplicationTests {
	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private OrderService orderService;

	@Test
	void testFindOrderForUser() {
		// System.out.println(orderService.checkIfAutoRefundable(4));
		System.out.println(orderService.autoRefund(3));

		// List<Object[]> results = orderRepo.getOrderDetailsByUserId(1);
		// int i = 1;
		// for(Object[] singleResult:results) {
		// System.out.println(i);
		// System.out.println("orderId"+singleResult[0]);
		// System.out.println("date"+singleResult[1]);
		// System.out.println("checkInDate"+singleResult[2]);
		// System.out.println("purchasedPrice"+singleResult[3]);
		// System.out.println("name"+singleResult[4]);
		// System.out.println("roomName"+singleResult[5]);
		// System.out.println("stateContent"+singleResult[6]);
		// i+=1;
		//
		// }
		// List<SalesNumbersDTO> results =
		// orderRepo.getMonthlySalesAndOrdersAvailableDate(null,null,1);
		// for(SalesNumbersDTO singleResult:results) {
		// System.out.print("月份: "+ singleResult.getMonth() +"，");
		// System.out.print("銷售額: "+singleResult.getTotalSales()+"元，");
		// System.out.println("銷售量: "+singleResult.getOrderCount()+"間");
		// }

		// List<Object[]> results2 =
		// orderRepo.getMonthlySalesAndOrdersByOrderDate(null,null,1);
		// for(Object[] singleResult:results2) {
		// System.out.print("month"+singleResult[0]+" ");
		// System.out.print("銷售額"+singleResult[1]+" ");
		// System.out.println("銷售量"+singleResult[2]+" ");
		// }

		// List<Object[]> results = orderRepo.findOrderForUser(null, null, 1, 33);
		// int i = 1;
		// for (Object[] singleResult : results) {
		// System.out.println(i);
		// System.out.println("orderId" + singleResult[0]);
		// System.out.println("date" + singleResult[1]);
		// System.out.println("checkInDate" + singleResult[2]);
		// System.out.println("purchasedPrice" + singleResult[3]);
		// System.out.println("providerName" + singleResult[4]);
		// System.out.println("roomName" + singleResult[5]);
		// System.out.println("stateContent" + singleResult[6]);
		// i += 1;
		// orderService.automaticallyCancelOrders();
		// }
		// System.out.println("退款測試: " + orderService.checkIfAutoRefundable(1));

		// Date date1 = new Date(); // 當前時間
		// Date date2 = new Date(date1.getTime() + 3600 * 1000);
		// Date date3 = new Date(-3600 * 1000); // 加1小時

		// if (date3.compareTo(date2) < 0) {
		// System.out.println("date3 在 date2 之前");
		// System.out.println(date3.compareTo(date2));

		// System.out.println("date3" + date3);
		// System.out.println("date2" + date2);
		// } else if (date3.compareTo(date2) > 0) {
		// System.out.println("date3 在 date2 之後");
		// System.out.println(date3.compareTo(date2));
		// System.out.println("date3" + date3);
		// System.out.println("date2" + date2);
		// } else {
		// System.out.println("date3 和 date2 相同");
		// System.out.println("date3" + date3);
		// System.out.println("date2" + date2);
		// }

		//
		// List<Object[]> results2 = orderRepo.findOrderForProvider(null,null,1,31);
		// int y = 1;
		// System.out.println("提供者");
		// for(Object[] singleResult:results2) {
		// System.out.println(y);
		// System.out.println("orderId"+singleResult[0]);
		// System.out.println("date"+singleResult[1]);
		// System.out.println("checkInDate"+singleResult[2]);
		// System.out.println("purchasedPrice"+singleResult[3]);
		// System.out.println("name"+singleResult[4]);
		// System.out.println("roomName"+singleResult[5]);
		// System.out.println("stateContent"+singleResult[6]);
		// y+=1;
		//
		// }
		// orderService.updateOrderStateToSuccess(1);

		// Date currentDate = new Date();
		// // Print the formatted date and time
		// System.out.println("Current Timestamp:" + currentDate);

		// List<Order> results = orderRepo.cancelOrders();
		// for(Order singleResult:results) {
		// System.out.println(singleResult.getOrderId());
		// }
		// Date currentTime = new Date();
		//
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(currentTime);
		//
		// // 減去 2 分鐘
		// calendar.add(Calendar.MINUTE, -2);
		//
		// // 計算出新的時間
		// Date timeMinusTwoMinutes = calendar.getTime();
		//
		//
		//
		//
		// List<Order> results = orderRepo.findUnpaidOrders(timeMinusTwoMinutes);
		//
		// System.out.println(results.size());
		// for (Order singleResult:results) {
		// System.out.print(singleResult.getOrderId()+" ");
		// System.out.println(singleResult.getDate());
		// }

	}

}
