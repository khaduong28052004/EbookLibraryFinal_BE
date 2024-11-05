
package com.toel.service.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_BillDetail_User;
import com.toel.dto.user.response.Response_Bill_Product_User;
import com.toel.exception.CustomException;
import com.toel.model.Account;
import com.toel.model.Bill;
import com.toel.model.Cart;
import com.toel.model.Evalue;
import com.toel.model.OrderStatus;
import com.toel.model.Product;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.CartRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.OrderStatusRepository;

@Service
public class Service_BillDetail_User {
	@Autowired
	private BillRepository billRepository;
	@Autowired
	private BillDetailRepository billDetailRepository;
	@Autowired
	private OrderStatusRepository orderStatusRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private EvalueRepository evaluateRepository;
	@Autowired
	private CartRepository cartRepository;

	public Map<String, Object> getBillDetail(Integer billId) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<Response_BillDetail_User> shopListInBill = createBillsWithProductsInBillDetail(billId);
			response.put("data", shopListInBill);
			response.put("status", "success");
			response.put("message", "Retrieve data successfully");
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "An error occurred while retrieving orders.");
			response.put("error", e.getMessage());
		}
		return response;
	}

	public List<Response_BillDetail_User> createBillsWithProductsInBillDetail(Integer billId) {
		Map<Integer, Response_BillDetail_User> billMap = new HashMap<>();
		List<Object[]> billDetail = billDetailRepository.findBillDetailById(billId);
		List<Response_BillDetail_User> productDetail = new ArrayList<>();
		for (Object[] product : billDetail) {
			Integer billID = Integer.parseInt(product[1].toString());
			Response_BillDetail_User billData = billMap.getOrDefault(billID, createBillDetailUser(product));
			Response_Bill_Product_User productData = createBillProductUser(product);
			billData.getProducts().add(productData);
			billMap.put(billID, billData);
		}
		productDetail.addAll(billMap.values());
		return productDetail;
	}

	private Response_BillDetail_User createBillDetailUser(Object[] product) {
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
		Integer shopId = Integer.parseInt(product[18].toString());
		String shopName = product[19].toString();
		String shopAvatar = product[20].toString();
		String userFullname = product[21].toString();
		String userPhone = product[22].toString();
		Response_BillDetail_User billData = new Response_BillDetail_User();
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
		billData.setUserFullname(userFullname);
		billData.setUserPhone(userPhone);
		billData.setProducts(new ArrayList<>());
		return billData;
	}

	private Response_Bill_Product_User createBillProductUser(Object[] product) {
		Integer productID = Integer.parseInt(product[11].toString());
		String productName = product[12].toString();
		String productIntroduce = product[13].toString();
		Integer productQuantity = Integer.parseInt(product[14].toString());
		Double productPrice = Double.parseDouble(product[15].toString());
		Double productDiscountPrice = Double.parseDouble(product[16].toString());
		String productImageURL = product[17].toString();
		Integer userID = Integer.parseInt(product[0].toString());
		Integer billID = Integer.parseInt(product[1].toString());
		Response_Bill_Product_User productData = new Response_Bill_Product_User();
		productData.setProductId(productID);
		productData.setProductName(productName);
		productData.setProductIntroduce(productIntroduce);
		productData.setProductQuantity(productQuantity);
		productData.setProductPrice(productPrice);
		productData.setProductDiscountPrice(productDiscountPrice);
		productData.setProductImageURL(productImageURL);
		Evalue isEvalued = evaluateRepository.findByProductIdAndAccountId(userID, productID, billID);
		productData.setIsEvaluate(isEvalued != null);
		return productData;
	}

	public void cancelBill(Integer billId) {
		checkBillDetailStatus(billId, 6);

		Bill bill = billRepository.findById(billId).get();
		bill.setUpdateAt(new Date());
		bill.setOrderStatus(orderStatusRepository.findById(6).get());
		billRepository.saveAndFlush(bill);
	}

	public void confirmBill(Integer billId) {
		checkBillDetailStatus(billId, 5);

		Bill bill = billRepository.findById(billId).get();
		bill.setUpdateAt(new Date());
		bill.setOrderStatus(orderStatusRepository.findById(5).get());
		billRepository.saveAndFlush(bill);

	}

	public void reOrder(Integer billId) {
		checkBillDetailStatus(billId, 6);
		checkBillDetailStatus(billId, 5);
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

	private void checkBillDetailStatus(Integer billId, Integer orderStatusId) {
		if (billRepository.findById(billId).isEmpty() || billId == null ) {
			throw new CustomException("Đơn hàng không tồn tại", "error");
		}
		if (orderStatusRepository.findById(orderStatusId).isEmpty()) {
			throw new CustomException("Trạng thái đơn hàng không tồn tại", "error");
		}
	}

}