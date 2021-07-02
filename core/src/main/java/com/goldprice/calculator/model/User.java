package com.goldprice.calculator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private String username;
	private String password;
	private String userType;
	private String errorMsg;
	
	public User(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
