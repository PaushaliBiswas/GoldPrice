package com.goldprice.calculator.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoldPriceException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private String errorMessage;
	private int errorCode;
	
	public GoldPriceException(String errorMessage, int errorCode) {
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}
}
