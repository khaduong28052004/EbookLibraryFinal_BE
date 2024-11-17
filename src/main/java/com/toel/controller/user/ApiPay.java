package com.toel.controller.user;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.user.resquest.pay.Request_Pay;
import com.toel.dto.user.resquest.pay.Request_SellerOrder;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/user")
public class ApiPay {
	@PostMapping("pay")
	public ApiResponse<Request_Pay> pay(@RequestBody Request_Pay request_Pay) {
		return ApiResponse.<Request_Pay>build().result(request_Pay);
	}

}
