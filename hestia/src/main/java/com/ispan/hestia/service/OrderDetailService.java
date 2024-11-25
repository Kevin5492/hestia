package com.ispan.hestia.service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.hestia.model.Order;
import com.ispan.hestia.model.OrderDetails;
import com.ispan.hestia.model.State;
import com.ispan.hestia.repository.OrderDetailsRepository;
import com.ispan.hestia.repository.OrderRepository;
import com.ispan.hestia.repository.StateRepository;

@Service
public class OrderDetailService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderDetailsRepository orderDetailsRepo;

    @Autowired
    private StateRepository stateRepo;

    @Transactional
    public boolean checkIfAutoRefundable(Integer orderRoomId) {
        Date currentTime = new Date();
        // Date orderDate = new
        // Date(orderDetailsRepo.checkOrderDate(orderRoomId).getTime() + 5 * 60 *
        // 1000);// orderDate
        // 找到訂單時間然後加5分鐘再去比較
        Date orderDetailsDate = orderDetailsRepo.findById(orderRoomId).get().getOrder().getDate();

        Date orderDate = new Date(orderDetailsDate.getTime() + 5 * 60 * 1000);
        if (orderDate.compareTo(currentTime) < 0) {
            System.out.println("orderDate" + orderDate);
            System.out.println("currentTime" + currentTime);
            return false;
        }
        System.out.println("orderDate" + orderDate);
        System.out.println("currentTime" + currentTime);
        return true;
    }

    @Transactional
    public boolean autoRefundOrderDetails(Integer orderRoomId) {
        Optional<OrderDetails> odOptional = orderDetailsRepo.findById(orderRoomId);
        if (odOptional.isEmpty()) {
            return false;
        }
        OrderDetails orderDetails = odOptional.get();
        State successState = stateRepo.findById(38).get();// 找到完成的狀態
        State refundingState = stateRepo.findById(35).get();// 找到退款中的狀態
        boolean statesCheck = true;
        Order order = orderDetails.getOrder();
        try {
            orderDetails.setState(refundingState);
            for (OrderDetails singleOrderDetails : order.getOrderDetails()) {
                if (!singleOrderDetails.getState().equals(refundingState)) {
                    statesCheck = false;
                    break;
                }

            }
            if (statesCheck) {
                order.setState(refundingState);
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
