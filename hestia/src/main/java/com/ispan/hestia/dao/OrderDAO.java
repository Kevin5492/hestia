package com.ispan.hestia.dao;


import java.util.Date;
import java.util.List;

import com.ispan.hestia.dto.SalesNumbersDTO;

public interface OrderDAO {
	
	public List<SalesNumbersDTO> getMonthlySalesAndOrdersAvailableDate(Date startDate, Date endDate, Integer providerId);
	
	public List<Object[]> getMonthlySalesAndOrdersByOrderDate(Date startDate,Date endDate,Integer providerId);
	
	public List<Object[]> findOrderForUser(Date startDate,Date endDate,Integer userId,Integer stateId);
	
	public List<Object[]> findOrderForProvider(Date startDate,Date endDate,Integer providerId,Integer stateId);
	
//	public void completeThePurchase(Integer orderId,State state);
	
//	public List<Object[]> getOrderDetailsByUserId(Integer userId);

}
