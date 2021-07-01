package com.goldprice.calculator.service;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldprice.calculator.model.User;

@Service
public class UserService {
	
	public User getUser(String username){
		ObjectMapper mapper = new ObjectMapper();
		String filepath = "../core/src/main/resources/Users.json";
		try {
			List<User> users = Arrays.asList(mapper.readValue(Paths.get(filepath).toFile(), User[].class));
			for(User user : users) {
				if(user.getUsername().equals(username))
					return user;
			}
		}catch(Exception e) {
			
		}
		return null;
	}

	public User login(String username, String password) {
		User user = getUser(username);
		if(user != null && user.getPassword().equals(password))
			return user;
		return null;
	}
	
}
