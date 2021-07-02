package com.goldprice.calculator.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.goldprice.calculator.common.GoldPriceConstants;
import com.goldprice.calculator.common.GoldPriceException;
import com.goldprice.calculator.model.GoldPriceCalculator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoldPriceCalculatorService {
	
	@Value("${goldprice.discount}")
	private double discount;
	
	@Value("${download.path}")
	private String downloadPath;
	
	public GoldPriceCalculator getDiscount() {
		return GoldPriceCalculator.builder().discount(discount).build();
	}

	public File download(GoldPriceCalculator data) {
	    File file = new File(downloadPath);
		try{
			if (!file.exists()) file.createNewFile();
		}catch(IOException e1) {
			log.error(GoldPriceConstants.FILE_CREATE_ERROR.getValue());
			throw new GoldPriceException();
		}
		
		try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
			fileOutputStream.write(data.toString().getBytes());
			fileOutputStream.flush();
		}catch(IOException e2) {
			log.error(GoldPriceConstants.FILE_WRITE_ERROR.getValue());
			throw new GoldPriceException();
		}
		return file;
	}

}