package com.toel.service.user;

import java.security.Timestamp;
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
import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_Bill;
import com.toel.dto.user.response.Response_Bill_Shop;
import com.toel.dto.user.response.Response_Bill_Product;
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
import com.toel.repository.ProductReportRepository;

@Service("userServiceBill")
public class Service_Bill_User {
	@Autowired
	BillRepository billRepository;
	@Autowired
	BillDetailRepository billDetailRepository;
	@Autowired
	OrderStatusRepository orderStatusRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	ProductReportRepository productRepository;
	@Autowired
	EvalueRepository evaluateRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	OrderStatusRepository oderStatusRepository;

	public Map<String, Object> getBills(Request_Bill_User requestBillDTO) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<Object[]> productsInBill = getBillsByOrderStatus(requestBillDTO);
			List<Response_Bill> shopListInBill = createBillsWithProductsInBillDetail(productsInBill);
			response.put("data", shopListInBill);
			response.put("status", "success");
			response.put("message", "Retrieve data successfully");
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "An error occurred while retrieving orders.");
			response.put("error", e.getMessage());
			return (Map<String, Object>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return response;
	}

	public List<Object[]> getBillsByOrderStatus(Request_Bill_User BillShopRequestDTO) {
		Integer userId = BillShopRequestDTO.getUserID();
		String orderStatus = BillShopRequestDTO.getOrderStatusFind() == null ? ""
				: BillShopRequestDTO.getOrderStatusFind();

		switch (orderStatus) {
		case "CHUANBI":
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

	public List<Response_Bill> createBillsWithProductsInBillDetail(List<Object[]> productsInBill) {
		Map<Integer, Response_Bill> billMap = new HashMap<>(); // Map để lưu các bill với key là billID
		List<Response_Bill> bills = new ArrayList<>(); // Danh sách để trả về cuối cùng

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

			Response_Bill billData = billMap.get(billID);
			if (billData == null) {
				billData = new Response_Bill();
				billData.setBillID(billID);
				billData.setUserID(userID);
				billData.setBillTotalPrice(billTotalPrice);
				billData.setBillDiscountPrice(billDiscountPrice);
				billData.setBillTotalShippingPrice(billTotalShippingPrice);
				billData.setBillTotalQuantity(billTotalQuantity);
				billData.setBillAddress(billAddress);
				billData.setBillOrderStatusId(orderStatusID);
				billData.setCreatedDatetime(createdDatetime);
				billData.setUpdatedDatetime(updatedDatetime);
				billData.setBillDiscountRate(billDiscountRate);
				billData.setShopId(shopId);
				billData.setShopName(shopName);
				billData.setShopAvatar(shopAvatar);
				billData.setProducts(new ArrayList<>());
				billMap.put(billID, billData); // Thêm bill mới vào Map
			}

			Response_Bill_Product productData = new Response_Bill_Product();
			productData.setProductId(productID);
			productData.setProductName(productName);
			productData.setProductIntroduce(productIntroduce);
			productData.setProductQuantity(productQuantity);
			productData.setProductPrice(productPrice);
			productData.setProductDiscountPrice(productDiscountPrice);
			productData.setProductImageURL(productImageURL);

			Evalue isEvalued = evaluateRepository.findByProductIdAndAccountId(userID, productID, billID);
			productData.setIsEvaluate(isEvalued != null);
			billData.getProducts().add(productData);
		}
		bills.addAll(billMap.values());
		return bills;
	}

	public Map<String, Object> cancelBill(Integer billId) {
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<Bill> billOptional = billRepository.findById(billId);
		Optional<OrderStatus> orderStatusOptional = orderStatusRepository.findById(6);

		if (orderStatusOptional.isEmpty()) {
			response.put("message", "Trạng thái đơn hàng không tồn tại");
			response.put("status", "fail");
			return response;
		}

		if (billOptional.isEmpty()) {
			response.put("message", "Đơn hàng không tồn tại");
			response.put("status", "fail");
		}

		try {
			Bill bill = billOptional.get();
			bill.setUpdateAt(new Date());
			bill.setOrderStatus(orderStatusOptional.get());
			billRepository.saveAndFlush(bill);
			response.put("message", "Hủy đơn thành công");
			response.put("status", "successfully");
		} catch (Exception e) {
			response.put("message", "Hủy đơn thất bại");
			response.put("status", "error");
			response.put("server-message", e.getMessage());
		}
		return response;
	}

	public Map<String, Object> confirmBill(Integer billId) {
		Map<String, Object> response = new HashMap<String, Object>();

		Optional<OrderStatus> orderStatusOptional = orderStatusRepository.findById(5);
		if (orderStatusOptional.isEmpty()) {
			response.put("message", "Trạng thái đơn hàng không tồn tại");
			response.put("status", "fail");
		}

		Optional<Bill> billOptional = billRepository.findById(billId);
		if (billOptional.isEmpty()) {
			response.put("message", "Đơn hàng không tồn tại");
			response.put("status", "fail");
		}

		try {
			Bill bill = billOptional.get();
			bill.setUpdateAt(new Date());
			bill.setOrderStatus(orderStatusOptional.get());
			billRepository.saveAndFlush(bill);

			response.put("message", "Đã xác nhận thành công");
			response.put("status", "successfully");
		} catch (Exception e) {
			response.put("message", "Xác nhận thất bại");
			response.put("status", "error");
			response.put("server-message", e.getMessage());
		}

		return response;
	}

	public Map<String, Object> reOrder(Integer billId) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<Cart> cart = new ArrayList<Cart>();

		List<Object[]> originBills = billDetailRepository.getOriginBillsByBillId(billId);
		if (originBills.isEmpty()) {
			response.put("data", false);
			response.put("message", "Không có chi tiết hóa đơn nào được tìm thấy.");
			response.put("status", "fail");
			return response;
		}

		try {
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

			response.put("data", true);
			response.put("message", "Đã thêm vào giỏ hàng");
			response.put("status", "successfully");
		} catch (Exception e) {
			response.put("data", false);
			response.put("message", "Lỗi thêm vào giỏ hàng");
			response.put("status", "error");
			response.put("error", e.getMessage());
		}

		return response;
	}

}