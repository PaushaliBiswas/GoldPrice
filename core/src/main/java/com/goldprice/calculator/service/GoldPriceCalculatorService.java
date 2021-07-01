package com.goldprice.calculator.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.goldprice.calculator.model.GoldPriceCalculator;

@Service
public class GoldPriceCalculatorService {
	
	@Value("${goldprice.discount}")
	private double discount;
	
	public GoldPriceCalculator getDiscount() {
		return GoldPriceCalculator.builder().discount(discount).build();
	}

	public File download(GoldPriceCalculator data) throws IOException {
	    File file = new File("./src/main/resources/GoldPrice.txt");
		if (!file.exists()) file.createNewFile();
		
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(data.toString().getBytes());
		fileOutputStream.flush();
		fileOutputStream.close();
		return file;
	}

}