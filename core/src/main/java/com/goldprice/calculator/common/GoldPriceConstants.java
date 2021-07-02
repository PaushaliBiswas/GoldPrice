package com.goldprice.calculator.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GoldPriceConstants {

	USERNAME_NOT_FOUND("Username not found!"),
	WRONG_PASSWORD("Wrong password!"),
	GET_USER_ERROR("Exception occured while getting user details"),
	FILE_CREATE_ERROR("Exception pccured while creating file"),
	FILE_WRITE_ERROR("Exception pccured while writing file");
	@Getter
	private final String value;
}
