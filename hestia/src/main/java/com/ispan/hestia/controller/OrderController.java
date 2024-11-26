package com.ispan.hestia.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.hestia.dto.OrderReponse;
import com.ispan.hestia.dto.ProviderDTO;
import com.ispan.hestia.dto.UserOrderDTO;
import com.ispan.hestia.service.OrderService;

@RestController
@CrossOrigin
@RequestMapping("/hestia")
public class OrderController {

    @RestController
    @RequestMapping("/userOrders")
    public static class UserOrderController {

        @Autowired
        private OrderService orderService;

        @PostMapping("/find")
        public OrderReponse findUserOrder(@RequestBody String entity) {
            try {
                JSONObject obj = new JSONObject(entity);
                Integer userId = obj.isNull("userId") ? null : obj.getInt("userId");

                Integer stateId = obj.isNull("stateId") ? null : obj.getInt("stateId");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                Date startSearchDate = obj.isNull("startSearchDate") ? null
                        : java.sql.Timestamp.valueOf(LocalDate.parse(obj.getString("startSearchDate"),
                                formatter).atStartOfDay());
                Date endSearchDate = obj.isNull("endSearchDate") ? null
                        : java.sql.Timestamp.valueOf(LocalDate.parse(obj.getString("endSearchDate"),
                                formatter).atStartOfDay());

                List<UserOrderDTO> result = orderService.findUserOrders(startSearchDate, endSearchDate, userId,
                        stateId);

                return new OrderReponse(true, "查詢成功", null, result, null);
            } catch (Exception e) {
                e.printStackTrace();
                return new OrderReponse(false, "查詢失敗", null, null, null);
            }
        }

        @PostMapping("/complete")
        public OrderReponse orderComplete(@RequestBody String entity) {
            String mssg = "付款失敗";
            try {
                JSONObject obj = new JSONObject(entity);
                Integer orderId = obj.isNull("orderId") ? null : obj.getInt("orderId");
                boolean result = orderService.updateOrderStateToSuccess(orderId);
                if (result) {
                    mssg = "付款成功";
                }
                return new OrderReponse(result, mssg, null, null, null);

            } catch (Exception e) {
                e.printStackTrace();
                return new OrderReponse(false, mssg, null, null, null);
            }
        }

        @GetMapping("/refund/check/{id}")
        public OrderReponse findOrdersUser(@PathVariable Integer orderId) {
            boolean refundable = orderService.checkIfAutoRefundable(orderId);
            String mssg = "不在退款區間內，可以手動申請退款";
            if (refundable) {
                mssg = "在退款區間內，可以直接退款";
            }
            return new OrderReponse(refundable, mssg, null, null, null);
        }

        @PostMapping("/refund/autoRefund")
        public OrderReponse autoRefund(@RequestBody String entity) {
            String mssg = "退款失敗";
            try {
                JSONObject obj = new JSONObject(entity);
                Integer orderId = obj.isNull("orderId") ? null : obj.getInt("orderId");

                boolean result = orderService.modifyOrderState(orderId, 38, 35);

                if (result) {
                    mssg = "退款成功";
                }

                return new OrderReponse(false, mssg, null, null, null);

            } catch (Exception e) {
                e.printStackTrace();
                return new OrderReponse(false, mssg, null, null, null);
            }
        }

        @PostMapping("/refund/applyRefund")
        public OrderReponse manualRefund(@RequestBody String entity) {
            String mssg = "退款申請送出失敗";
            try {
                JSONObject obj = new JSONObject(entity);
                Integer orderId = obj.isNull("orderId") ? null : obj.getInt("orderId");

                boolean result = orderService.modifyOrderState(orderId, 38, 34);

                if (result) {
                    mssg = "退款申請成功送出";
                }

                return new OrderReponse(result, mssg, null, null, null);

            } catch (Exception e) {
                e.printStackTrace();
                return new OrderReponse(false, mssg, null, null, null);
            }
        }

    }

    @RestController
    @RequestMapping("/providerOrders")
    public static class ProviderOrderController {

        @Autowired
        private OrderService orderService;

        @PostMapping("/refundAccepted")
        public OrderReponse manualAcceptingRefund(@RequestBody String entity) {
            String mssg = "無法成功接受退款";
            try {
                JSONObject obj = new JSONObject(entity);
                Integer orderId = obj.isNull("orderId") ? null : obj.getInt("orderId");

                boolean result = orderService.modifyOrderState(orderId, 34, 35);

                if (result) {
                    mssg = "成功接受退款";
                }

                return new OrderReponse(result, mssg, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
                return new OrderReponse(false, mssg, null, null, null);
            }
        }

        @PostMapping("/refundDeclined")
        public OrderReponse declinedTheRefund(@RequestBody String entity) {
            String mssg = "退款拒絕失敗";
            try {
                JSONObject obj = new JSONObject(entity);
                Integer orderId = obj.isNull("orderId") ? null : obj.getInt("orderId");

                boolean result = orderService.manualRefundDeclined(orderId, 34, 38);

                if (result) {
                    mssg = "成功拒絕退款";
                }

                return new OrderReponse(result, mssg, null, null, null);

            } catch (Exception e) {
                e.printStackTrace();
                return new OrderReponse(false, mssg, null, null, null);
            }

        }

        @PostMapping("/find")
        public OrderReponse findProviderOrder(@RequestBody String entity) {
            try {
                JSONObject obj = new JSONObject(entity);
                Integer providerId = obj.isNull("providerId") ? null : obj.getInt("providerId");

                Integer stateId = obj.isNull("stateId") ? null : obj.getInt("stateId");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                Date startSearchDate = obj.isNull("startSearchDate") ? null
                        : java.sql.Timestamp.valueOf(LocalDate.parse(obj.getString("startSearchDate"),
                                formatter).atStartOfDay());
                Date endSearchDate = obj.isNull("endSearchDate") ? null
                        : java.sql.Timestamp.valueOf(LocalDate.parse(obj.getString("endSearchDate"),
                                formatter).atStartOfDay());

                List<ProviderDTO> result = orderService.findProviderOrders(startSearchDate, endSearchDate, providerId,
                        stateId);

                return new OrderReponse(true, "查詢成功", null, null, result);
            } catch (Exception e) {
                e.printStackTrace();
                return new OrderReponse(false, "查詢失敗", null, null, null);
            }
        }

    }
}

// @PostMapping("/providerOrders/acceptRefund")
// public OrderReponse manualRefundAccepted(@RequestBody String entity){

// }

// String startSearchDateStr = obj.isNull("startSearchDate") ? null :
// obj.getString("startSearchDate");

// DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
// LocalDateTime startSearchDateTime = LocalDateTime.parse(startSearchDateStr,
// formatter);
// Date startSearchDate = java.sql.Timestamp.valueOf(startSearchDateTime);
