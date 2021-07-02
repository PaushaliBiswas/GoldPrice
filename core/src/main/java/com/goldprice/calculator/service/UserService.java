package com.goldprice.calculator.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldprice.calculator.common.GoldPriceConstants;
import com.goldprice.calculator.common.GoldPriceException;
import com.goldprice.calculator.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	@Value("${user.filepath}")
	private String filepath;
	
	public User getUser(String username){
		ObjectMapper mapper = new ObjectMapper();
		User user = null;
		try {
			User[] usAr = mapper.readValue(Paths.get(filepath).toFile(), User[].class);
			List<User> users = Arrays.asList(usAr);
			for(User u : users) {
				if(u.getUsername().equals(username)){
					user = u;
					break;
				}
			}
			if(user != null) return user;
			else{
				log.error(GoldPriceConstants.USERNAME_NOT_FOUND.getValue());
				throw new GoldPriceException(GoldPriceConstants.USERNAME_NOT_FOUND.getValue(), HttpStatus.UNAUTHORIZED.value());
			}
		}catch(IOException e) {
			log.error(GoldPriceConstants.GET_USER_ERROR.getValue());
			throw new GoldPriceException(GoldPriceConstants.GET_USER_ERROR.getValue(), HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	public User login(String username, String password){
		User user = null;
		user = getUser(username);
		if (user.getPassword().equals(password)) return user;
		else{
			log.error(GoldPriceConstants.WRONG_PASSWORD.getValue());
			throw new GoldPriceException(GoldPriceConstants.WRONG_PASSWORD.getValue(), HttpStatus.UNAUTHORIZED.value());
		}
	}
	
}
