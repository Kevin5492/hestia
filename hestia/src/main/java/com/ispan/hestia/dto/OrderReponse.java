package com.ispan.hestia.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderReponse(
        boolean updateOrderStateToSuccess,
        String updateToSuccessMessage,
        List<SalesNumbersDTO> salesNumbers

) {
}
