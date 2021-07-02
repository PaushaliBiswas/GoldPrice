package com.goldprice.calculator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.goldprice.calculator.common.GoldPriceConstants;
import com.goldprice.calculator.common.GoldPriceException;
import com.goldprice.calculator.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	@InjectMocks
	private UserService userService;
	
	private String filepath = "./src/test/resources/Users.json";
	
	@BeforeEach
	public void setup() {
		ReflectionTestUtils.setField(userService, "filepath", filepath);
	}
	@Test
	void loginTestSuccess() {
		User expectedUser1 = User.builder().username("User1").password("123").userType(GoldPriceConstants.PRIVILEGED.getValue()).build();
		User actualUser1 = userService.login(expectedUser1.getUsername(), expectedUser1.getPassword());
		assertEquals(expectedUser1.getUserType(), actualUser1.getUserType());
		User expectedUser2 = User.builder().username("User2").password("123").userType(GoldPriceConstants.REGULAR.getValue()).build();
		User actualUser2 = userService.login(expectedUser2.getUsername(), expectedUser2.getPassword());
		assertEquals(expectedUser2.getUserType(), actualUser2.getUserType());
	}
	
	@Test
	void loginTestWrongUsername() {
		User expectedUser = User.builder().username("User3").password("123").userType(GoldPriceConstants.PRIVILEGED.getValue()).build();
		try{
			userService.login(expectedUser.getUsername(), expectedUser.getPassword());
		}catch(Exception e) {
			assertTrue(e instanceof GoldPriceException);
			assertEquals(((GoldPriceException)e).getErrorMessage(), GoldPriceConstants.USERNAME_NOT_FOUND.getValue());
		}
	}
	
	@Test
	void loginTestWrongPassword() {
		User expectedUser = User.builder().username("User1").password("12345").build();
		try{
			userService.login(expectedUser.getUsername(), expectedUser.getPassword());
		}catch(Exception e) {
			assertTrue(e instanceof GoldPriceException);
			assertEquals(((GoldPriceException)e).getErrorMessage(), GoldPriceConstants.WRONG_PASSWORD.getValue());
		}
	}
}
