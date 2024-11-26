package com.toel.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.user.resquest.pay.Request_Pay;
import com.toel.service.user.Service_Pay;


@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/user")
public class ApiPay {
	@Autowired
	Service_Pay service_Pay;

	@PostMapping("pay/{id}")
	public ApiResponse<Request_Pay> pay(@PathVariable("id") Integer id_user,
			@RequestParam("paymentMethod_id") Integer paymentMethod_id, @RequestBody Request_Pay request_Pay) {
		service_Pay.createOrder(request_Pay, id_user, paymentMethod_id);
		return ApiResponse.<Request_Pay>build().result(request_Pay);
	}

}
