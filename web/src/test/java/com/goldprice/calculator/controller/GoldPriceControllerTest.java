package com.goldprice.calculator.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldprice.calculator.common.GoldPriceException;
import com.goldprice.calculator.model.GoldPriceCalculator;
import com.goldprice.calculator.model.User;
import com.goldprice.calculator.service.GoldPriceCalculatorService;
import com.goldprice.calculator.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
@WebMvcTest(GoldPriceController.class)
public class GoldPriceControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	@MockBean 
	private GoldPriceCalculatorService goldPriceService;
	
	@Test
	void loginTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		User user = User.builder().username("User1").password("123").build();
		given(userService.login("User1", "123")).willReturn(user);
		
		mockMvc.perform(post("/api/gold-price/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(user))
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(jsonPath("$.username", is("User1")));
	}
	
	@Test
	void loginTestFailure() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		User user = User.builder().username("User1").password("123").build();
		try{
			given(userService.login("User1", "123")).willThrow(GoldPriceException.class);
		}catch(Exception e) {
			mockMvc.perform(post("/api/gold-price/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(user))
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		}
	}
	
	@Test
	void getDiscountTest() throws Exception {
		GoldPriceCalculator goldPrice = GoldPriceCalculator.builder().discount(2.0).build();
		given(goldPriceService.getDiscount()).willReturn(goldPrice);
		
		mockMvc.perform(get("/api/gold-price/discount")).andExpect(status().isOk()).andExpect(jsonPath("$.discount", is(2.0)));
	}
	
	@Test
	void getDiscountTestFailure() throws Exception {
		try{
			given(goldPriceService.getDiscount()).willThrow(GoldPriceException.class);
		}catch(Exception e) {
			mockMvc.perform(get("/api/gold-price/discount")).andExpect(status().isInternalServerError());
		}
	}
	
	@Test
	void downloadTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		GoldPriceCalculator goldPrice = GoldPriceCalculator.builder().rate(5000.0).weight(10.0).discount(2.0).price(49000.0).build();
		File file = new File("Users.txt");
		file.createNewFile();
		try(FileOutputStream fos = new FileOutputStream(file)){
			fos.write(new String("Hello World").getBytes());
		}catch(Exception e) {
			log.error("Exception while writing file in Test");
		}
		given(goldPriceService.download(goldPrice)).willReturn(file);
		
		mockMvc.perform(post("/api/gold-price/download")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(goldPrice))
				.accept(MediaType.APPLICATION_OCTET_STREAM)).andExpect(status().isOk());
	}
	
	@Test
	void downloadTestFailure() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		GoldPriceCalculator goldPrice = GoldPriceCalculator.builder().rate(5000.0).weight(10.0).discount(2.0).price(49000.0).build();
		
		try{
			given(goldPriceService.download(goldPrice)).willThrow(GoldPriceException.class);
		}catch(Exception e) {
			mockMvc.perform(post("/api/gold-price/download")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(goldPrice))
					.accept(MediaType.APPLICATION_OCTET_STREAM)).andExpect(status().isNotFound());
		}
		
	}
}
