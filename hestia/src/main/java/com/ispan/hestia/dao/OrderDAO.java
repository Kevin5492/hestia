package com.ispan.hestia.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import com.ispan.hestia.dto.ProviderDTO;
import com.ispan.hestia.dto.SalesNumbersDTO;
import com.ispan.hestia.dto.UserOrderDTO;

public interface OrderDAO {

	public List<SalesNumbersDTO> getMonthlySalesAndOrdersAvailableDate(Date startDate, Date endDate,
			Integer providerId);

	// public List<Object[]> getMonthlySalesAndOrdersByOrderDate(Date startDate,
	// Date endDate, Integer providerId);

	public List<UserOrderDTO> findOrderForUser(Date startDate, Date endDate, Integer userId, Integer stateId,
			String searchInput);

	public List<ProviderDTO> findOrderForProvider(Date startDate, Date endDate, Integer providerId, Integer stateId);

	public Page<UserOrderDTO> findOrderForUserPage(Date startDate, Date endDate, Integer userId, Integer stateId,
			String searchInput, Integer pageNumber, Integer pageSize);

	// public void completeThePurchase(Integer orderId,State state);

	// public List<Object[]> getOrderDetailsByUserId(Integer userId);

}
