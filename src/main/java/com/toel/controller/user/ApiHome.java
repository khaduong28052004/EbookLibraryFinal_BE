package com.toel.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.repository.AccountRepository;

@RestController
@RequestMapping("api/v1/user")
public class ApiHome {
	@Autowired
	AccountRepository accountRepository;

	@RequestMapping("home")
	public String getHome() {
		accountRepository.getById(1);
		return "hello";
	}

}
