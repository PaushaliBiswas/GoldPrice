package com.goldprice.calculator.controller;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goldprice.calculator.common.GoldPriceException;
import com.goldprice.calculator.model.GoldPriceCalculator;
import com.goldprice.calculator.model.User;
import com.goldprice.calculator.service.GoldPriceCalculatorService;
import com.goldprice.calculator.service.UserService;

@RestController
@RequestMapping("/api/gold-price")
public class GoldPriceController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GoldPriceCalculatorService goldPriceCalculatorService;
	
	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user) {
		try{
			return new ResponseEntity<>(userService.login(user.getUsername(), user.getPassword()), HttpStatus.OK);
		}catch(GoldPriceException e) {
			return new ResponseEntity<>(new User(e.getErrorMessage()), HttpStatus.valueOf(e.getErrorCode()));
		}
	}
	
	@GetMapping("/discount")
	public ResponseEntity<GoldPriceCalculator> getDiscount() {
		GoldPriceCalculator res = goldPriceCalculatorService.getDiscount();
		if(res != null) return new ResponseEntity<>(res, HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(path="/download", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> download(@RequestBody GoldPriceCalculator data){
		try {
			File file = goldPriceCalculatorService.download(data);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName()).body(resource);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
