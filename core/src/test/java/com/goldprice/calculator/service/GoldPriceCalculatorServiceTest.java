package com.goldprice.calculator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.goldprice.calculator.model.GoldPriceCalculator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
class GoldPriceCalculatorServiceTest {

	
	@InjectMocks
	GoldPriceCalculatorService goldService;
	
	@BeforeEach
	public void init() {
		ReflectionTestUtils.setField(goldService, "discount", 3.0);
		ReflectionTestUtils.setField(goldService, "downloadPath", "downloadPath");
	}
	
	@Test
	void getDiscountTest() {
		assertEquals(goldService.getDiscount().getDiscount(), 3.0);
	}
	
	@Test
	void downloadSuccess() {
		GoldPriceCalculator gpc = GoldPriceCalculator.builder().rate(500.0).weight(3.0).discount(0).price(1500.0).build();
		File file = mock(File.class);
		try(FileOutputStream os = mock(FileOutputStream.class)){
			os.write(gpc.toString().getBytes());
			assertEquals(goldService.download(gpc), file);
		}catch(Exception e) {
			log.error(e.getMessage());
		}
	}
}
