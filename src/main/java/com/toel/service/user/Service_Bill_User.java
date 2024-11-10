package com.toel.service.user;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_Bill_User;
import com.toel.dto.user.response.Response_Bill_Shop_User;
import com.toel.dto.user.response.Response_Bill_Product_User;
import com.toel.dto.user.resquest.Request_Bill_User;

import com.toel.model.Account;
import com.toel.model.Address;
import com.toel.model.Bill;
import com.toel.model.Cart;
import com.toel.model.Evalue;
import com.toel.model.OrderStatus;
import com.toel.model.Product;
import com.toel.repository.AccountRepository;
import com.toel.repository.AddressRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.CartRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.OrderStatusRepository;
import com.toel.repository.PaymentMethodRepository;
import com.toel.service.Email.EmailService;

@Service("userServiceBill")
public class Service_Bill_User {
	@Autowired
	BillRepository billRepository;
	@Autowired
	BillDetailRepository billDetailRepository;
	@Autowired
	OrderStatusRepository orderStatusRepository;
	@Autowired
	PaymentMethodRepository paymentMethodRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	EvalueRepository evaluateRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	OrderStatusRepository oderStatusRepository;
	@Autowired
	EmailService emailService;

	public Map<String, Object> getBills(Request_Bill_User requestBillDTO) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<Object[]> productsInBill = getBillsByOrderStatus(requestBillDTO);
			List<Response_Bill_User> shopListInBill = createBillsWithProductsInBillDetail(productsInBill);
			response.put("data", shopListInBill);
			response.put("status", "successfully");
			response.put("message", "Retrieve data successfully");
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "An error occurred while retrieving orders.");
			response.put("error", e.getMessage());
		}
		return response;
	}

	public List<Object[]> getBillsByOrderStatus(Request_Bill_User BillShopRequestDTO) {
		Integer userId = BillShopRequestDTO.getUserID();
		String orderStatus = BillShopRequestDTO.getOrderStatusFind() == null ? ""
				: BillShopRequestDTO.getOrderStatusFind();

		System.out.println("orderstatusL " + orderStatus);
		System.out.println("userId " + userId);

		switch (orderStatus) {
			case "CHODUYET":
				return billRepository.getBillsByUserIdNOrderStatusOrderByCreateAt(userId, 1);
			case "DANGXULY":
				return billRepository.getBillsByUserIdNOrderStatusOrderByUpdateAt(userId, 2);
			case "DANGGIAO":
				return billRepository.getBillsByUserIdNOrderStatusOrderByUpdateAt(userId, 3);
			case "DAGIAO":
				return billRepository.getBillsByUserIdNOrderStatusOrderByUpdateAt(userId, 4);
			case "HOANTHANH":
				return billRepository.getBillsByUserIdNOrderStatusOrderByUpdateAt(userId, 5);
			case "DAHUY":
				return billRepository.getBillsByUserIdNOrderStatusOrderByUpdateAt(userId, 6);
			default:
				return billRepository.getBillsByUserIdAll(userId);

		}
	}

	public List<Response_Bill_User> createBillsWithProductsInBillDetail(List<Object[]> productsInBill) {
		Map<Integer, Response_Bill_User> billMap = new HashMap<>(); // Map để lưu các bill với key là billID
		List<Response_Bill_User> bills = new ArrayList<>(); // Danh sách để trả về cuối cùng

		for (Object[] product : productsInBill) {
			Integer billID = Integer.parseInt(product[1].toString());
			Integer userID = Integer.parseInt(product[0].toString());
			Double billTotalPrice = Double.parseDouble(product[2].toString());
			Double billDiscountPrice = Double.parseDouble(product[3].toString());
			Double billTotalShippingPrice = Double.parseDouble(product[4].toString());
			Integer billTotalQuantity = Integer.parseInt(product[5].toString());
			String billAddress = product[6].toString();
			Integer orderStatusID = Integer.parseInt(product[7].toString());
			Date createdDatetime = (Date) product[8];
			Date updatedDatetime = (Date) product[9];
			Double billDiscountRate = Double.parseDouble(product[10].toString());
			Integer productID = Integer.parseInt(product[11].toString());
			String productName = product[12].toString();
			String productIntroduce = product[13].toString();
			Integer productQuantity = Integer.parseInt(product[14].toString());
			Double productPrice = Double.parseDouble(product[15].toString());
			Double productDiscountPrice = Double.parseDouble(product[16].toString());
			String productImageURL = product[17].toString();
			Integer shopId = Integer.parseInt(product[18].toString());
			String shopName = product[19].toString();
			String shopAvatar = product[20].toString();
			Integer billPaymentMethodId = Integer.parseInt(product[21].toString());

			String orderStatus = orderStatusRepository.findById(orderStatusID).get().getName();
			String billPaymentMethod = paymentMethodRepository.findById(billPaymentMethodId).get().getName();

			Response_Bill_User billData = billMap.get(billID);
			if (billData == null) {
				billData = new Response_Bill_User();
				billData.setBillID(billID);
				billData.setUserID(userID);
				billData.setBillTotalPrice(billTotalPrice);
				billData.setBillDiscountPrice(billDiscountPrice);
				billData.setBillTotalShippingPrice(billTotalShippingPrice);
				billData.setBillTotalQuantity(billTotalQuantity);
				billData.setBillAddress(billAddress);
				billData.setBillOrderStatusId(orderStatusID);
				billData.setBillOrderStatus(orderStatus);
				billData.setBillPaymentMethod(billPaymentMethod);
				billData.setCreatedDatetime(formatDate(createdDatetime.toString()));
				billData.setUpdatedDatetime(formatDate(updatedDatetime.toString()));
				billData.setBillDiscountRate(billDiscountRate);
				billData.setShopId(shopId);
				billData.setShopName(shopName);
				billData.setShopAvatar(shopAvatar);
				billData.setProducts(new ArrayList<>());
				billMap.put(billID, billData); // Thêm bill mới vào Map
			}

			Response_Bill_Product_User productData = new Response_Bill_Product_User();
			productData.setProductId(productID);
			productData.setProductName(productName);
			productData.setProductIntroduce(productIntroduce);
			productData.setProductQuantity(productQuantity);
			productData.setProductPrice(productPrice);
			productData.setProductDiscountPrice(productDiscountPrice);
			productData.setProductImageURL(productImageURL);

			Integer billDetailId = billDetailRepository.findBillDetailByProductIdAndAccountId(userID, productID,
					billID);
			productData.setBillDetailId(billDetailId);

			System.out.println("billDetailId " + billDetailId);
			Integer isEvalued = evaluateRepository.isEvaluate(billDetailId, productID, userID);
			productData.setIsEvaluate(isEvalued == 1);
			billData.getProducts().add(productData);
		}
		bills.addAll(billMap.values());
		bills.sort((bill1, bill2) -> bill2.getCreatedDatetime().compareTo(bill1.getCreatedDatetime())); // Sắp xếp giảm
																										// dần
		return bills;
	}

	public void cancelBill(Integer billId) {
		checkBillStatus(billId, 1);

		Bill bill = billRepository.findById(billId).get();
		bill.setUpdateAt(new Date());
		bill.setOrderStatus(orderStatusRepository.findById(6).get());
		billRepository.saveAndFlush(bill);
	}

	public void confirmBill(Integer billId) {

		checkBillStatus(billId, 4);

		Bill bill = billRepository.findById(billId).get();
		bill.setUpdateAt(new Date());
		bill.setOrderStatus(orderStatusRepository.findById(5).get());

		billRepository.saveAndFlush(bill);

	}

	public void reOrder(Integer billId) {
		checkBillStatus(billId, 6);
		checkBillStatus(billId, 5);

		List<Cart> cart = new ArrayList<Cart>();
		List<Object[]> originBills = billDetailRepository.getOriginBillsByBillId(billId);

		for (Object[] billDetail : originBills) {
			Integer quantityToAdd = Integer.parseInt(billDetail[0].toString());
			Integer accountId = Integer.parseInt(billDetail[1].toString());
			Integer productId = Integer.parseInt(billDetail[2].toString());

			Cart existingCartDetail = cartRepository.findCartByAccountIdAndProductId(productId, accountId);
			if (existingCartDetail != null) {
				existingCartDetail.setQuantity(existingCartDetail.getQuantity() + quantityToAdd);
				cartRepository.saveAndFlush(existingCartDetail);
			} else {
				Cart newCart = new Cart();

				Account account = new Account();
				account.setId(accountId);

				Product product = new Product();
				product.setId(productId);

				newCart.setAccount(account);
				newCart.setProduct(product);
				newCart.setQuantity(quantityToAdd);
				cart.add(newCart);
			}
		}

		if (!cart.isEmpty()) {
			cartRepository.saveAll(cart);
		}
	}

	private void checkBillStatus(Integer billId, Integer orderStatusId) {
		if (billRepository.findById(billId).isEmpty() || billId == null) {
			// throw new CustomException("Đơn hàng không tồn tại", "error");
		}
		if (orderStatusRepository.findById(orderStatusId).isEmpty()) {
			// throw new CustomException("Trạng thái đơn hàng không tồn tại", "error");
		}
	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void updateOrdersAutomatically() {
		List<Bill> bills = billRepository.findByOrderStatusId(4);
		LocalDateTime now = LocalDateTime.now();
		for (Bill bill : bills) {
			LocalDateTime lastUpdate = bill.getUpdateAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			if (ChronoUnit.DAYS.between(lastUpdate, now) >= 5 && ChronoUnit.DAYS.between(lastUpdate, now) < 7) {
				sendNotification(bill);
			} else if (ChronoUnit.DAYS.between(lastUpdate, now) >= 7) {
				OrderStatus updateOrderStatus = oderStatusRepository.findById(5).orElse(null);

				sendNotification(bill);
				if (updateOrderStatus != null) {
					System.out.println("GỬI MEO NÈ");
					bill.setOrderStatus(updateOrderStatus);
					bill.setUpdateAt(new Date());
					billRepository.saveAndFlush(bill);
				}

			}
		}
	}

	private void sendNotification(Bill bill) {
		String email = bill.getAccount().getEmail();
		String username = bill.getAccount().getUsername();

		String subject = "[TOEL] Thông báo cập nhật trạng thái xác nhận đơn hàng ";
		String content = "Dear " + username
				+ ", \n\nĐơn hàng của bạn sẽ được tự động cập nhật trạng thái sau 2 ngày. Vui lòng xác nhận trạng thái đã nhận hàng \n\n Xin cảm ơn vì đã mua hàng trên TOEL.";
		emailService.push(email, subject, content);
	}

	public static String formatDate(String inputDate) {
		// Định dạng đầu vào
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		// Định dạng đầu ra
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");

		try {
			// Phân tích chuỗi đầu vào thành đối tượng Date
			Date date = inputFormat.parse(inputDate);
			// Định dạng lại đối tượng Date thành chuỗi đầu ra
			return outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null; // Hoặc xử lý lỗi theo cách khác
		}
	}
}