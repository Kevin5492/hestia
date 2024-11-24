package com.ispan.hestia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesNumbersDTO {

	private String month;

	private Integer totalSales;

	private Long orderCount;

	SalesNumbersDTO(String month, Integer totalSales, Long orderCount) {
		this.month = month;
		this.totalSales = totalSales;
		this.orderCount = orderCount;
	}

}
