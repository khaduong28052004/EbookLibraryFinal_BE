package com.toel.controller.user;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.user.resquest.pay.Request_Pay;
import com.toel.service.user.Service_Pay;

import jakarta.servlet.http.HttpServletResponse;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

@RestController
//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("api/v1/user")
public class ApiPayOS {
	private final PayOS payOS;
	Request_Pay requestPay;
	Integer id_address;
	Integer id_user;

	public ApiPayOS(PayOS payOS) {
		super();
		this.payOS = payOS;
	}

	@Autowired
	Service_Pay service_Pay;

	@RequestMapping(value = "/success")
	public void Success(HttpServletResponse response) throws IOException {
		service_Pay.createOrder(requestPay, id_user, 3, id_address);
		response.sendRedirect("http://localhost:5173/profile#order");
	}

	@RequestMapping(value = "/cancel")
	public void Cancel(HttpServletResponse response) throws IOException {
		response.sendRedirect("http://localhost:5173/cart");
	}

	@PostMapping("/create-payment-link")
	public void checkout(@RequestBody Request_Pay pay,
			@RequestParam(name = "id_user", defaultValue = "0") Integer idUser,
			@RequestParam(name = "id_address", defaultValue = "0") Integer idAddress,
			HttpServletResponse httpServletResponse) {
		id_address = idAddress;
		id_user = idUser;
		try {
			requestPay = pay;
			final String productName = "THANH TOAN " + pay.getDatas().size() + " DON HANG TOEL";
			final String description = "Thanh toán đơn hàng";
			final String returnUrl = "http://localhost:8080/api/v1/user/success";
			final String cancelUrl = "http://localhost:8080/api/v1/user/cancel";
			String total = String.valueOf(requestPay.getTotal()).replace(".0", "");

			final int price = Integer.parseInt(total);

			// Gen order code
			String currentTimeString = String.valueOf(new Date().getTime());
			long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
			ItemData item = ItemData.builder().name(productName).quantity(pay.getDatas().size()).price(price).build();
			PaymentData paymentData = PaymentData.builder().orderCode(orderCode).amount(price).description(description)
					.returnUrl(returnUrl).cancelUrl(cancelUrl).item(item).build();

			// Tạo link thanh toán
			CheckoutResponseData data = payOS.createPaymentLink(paymentData);
			String checkoutUrl = data.getCheckoutUrl();

			// Trả lại URL checkout cho frontend
			httpServletResponse.setContentType("application/json");
			httpServletResponse.getWriter().write("{\"checkoutUrl\":\"" + checkoutUrl + "\"}");
		} catch (Exception e) {
			e.printStackTrace();
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
