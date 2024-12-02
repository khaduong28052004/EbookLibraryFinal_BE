
package com.toel.service.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_BillDetail_User;
import com.toel.dto.user.response.Response_Bill_Product_User;
import com.toel.dto.user.response.Response_Bill_Shop_User;
import com.toel.dto.user.response.Response_InfoBill_Billdetail_DTO;
import com.toel.dto.user.response.Response_InfoProduct_BillDetail_DTO;
import com.toel.dto.user.response.Response_InfoShop_Billdetail_DTO;
import com.toel.dto.user.response.Response_InfoUser_Billdetail_DTO;
import com.toel.mapper.user.ProductMaperUser;
//import com.toel.exception.CustomException;
import com.toel.model.Account;
import com.toel.model.Bill;
import com.toel.model.BillDetail;
import com.toel.model.Cart;
import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;
import com.toel.model.ImageProduct;
import com.toel.model.Product;
import com.toel.repository.AccountRepository;
import com.toel.repository.AddressRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.CartRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.OrderStatusRepository;
import com.toel.repository.ProductRepository;

@Service
public class Service_BillDetail_User {
	@Autowired
	private BillRepository billRepository;
	@Autowired
	private BillDetailRepository billDetailRepository;
	@Autowired
	private OrderStatusRepository orderStatusRepository;
	@Autowired
	private EvalueRepository evaluateRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	ProductMaperUser productMapperUser;
	@Autowired
	AccountRepository accountRepository;

	public Map<String, Object> getBillDetail(Integer billId) {
		return getInfoBill(billId);
	}

	private Map<String, Object> getInfoBill(Integer billId) {
		Map<String, Object> newBill = new HashMap<>();

		Map<String, Object> products = getBillDetails(billId);
		newBill.put("products", products);

		Map<String, Object> user = getInfoUser(billId);
		newBill.put("dataUser", user);

		Account shop = getInfoShop(billId);
		newBill.put("shop", shop);

		Map<String, Object> bill = getBill(billId);
		newBill.put("bill", bill);

		return newBill;
	}

	private Account getInfoShop(Integer billId) {
		Integer accountId = billDetailRepository.findShopIdByBillId(billId);
		Account shopInfo = accountRepository.findAccountById(accountId);
		return shopInfo;
	}

	private Map<String, Object> getBill(Integer billId) {
		Map<String, Object> result = new HashMap<>();
		Bill bill = billRepository.findById(billId).get();
		Map<String, Object> newBill = new HashMap<>();

		Double priceTemp = bill.getTotalPrice() - bill.getPriceShipping() + bill.getDiscountPrice();

		newBill.put("id", bill.getId());
		newBill.put("createAt", bill.getCreateAt());
		newBill.put("discountPrice", bill.getDiscountPrice());
		newBill.put("finishAt", bill.getFinishAt());
		newBill.put("priceShipping", bill.getPriceShipping());
		newBill.put("totalPrice", bill.getTotalPrice());
		newBill.put("totalQuantity", bill.getTotalQuantity());
		newBill.put("transaction", bill.getTransactions());
		newBill.put("updateAt", bill.getUpdateAt());
		newBill.put("priceTemp", priceTemp);

		String orderStatus = billRepository.findOrderStatusByBill(billId);
		String address = billRepository.findAddressByBill(billId) == null ? ""
				: billRepository.findAddressByBill(billId);

		result.put("bill", newBill);
		result.put("orderstatus", orderStatus);
		result.put("address", address);

		return result;

	}

	public Map<String, Object> getBillDetails(Integer billId) {
		Map<String, Object> result = new HashMap<>();
		try {
			// Lấy Bill từ billId
			Bill bill = billRepository.findById(billId)
					.orElseThrow(() -> new NoSuchElementException("Bill không tồn tại: " + billId));

			List<BillDetail> billDetails = bill.getBillDetails();

			List<Map<String, Object>> billDetailList = new ArrayList<>();
			for (BillDetail product : billDetails) {
				Map<String, Object> billDetailMap = new HashMap<>();
				Integer productId = product.getProduct().getId();
				Integer quantity = product.getQuantity();
				Double price = product.getPrice();
				Integer flashsalePercent = product.getFlashSaleDetail() == null ? 0
						: product.getFlashSaleDetail().getSale();
				String productName = product.getProduct().getName();

				String urlImage = "";
				List<ImageProduct> imageProducts = product.getProduct().getImageProducts();
				if (imageProducts != null && !imageProducts.isEmpty()) {
					urlImage = imageProducts.get(0).getName();
				}

				Integer isEvalued = evaluateRepository.isEvaluated(product.getId(), product.getProduct().getId(),
						bill.getAccount().getId());

				// Lưu thông tin vào Map
				billDetailMap.put("productId", productId);
				billDetailMap.put("productName", productName);
				billDetailMap.put("quantity", quantity);
				billDetailMap.put("price", price);
				billDetailMap.put("flashsalePercent", flashsalePercent);
				billDetailMap.put("urlImge", urlImage);
				billDetailMap.put("isEvaluated", isEvalued == 1);
				billDetailMap.put("billDetailId", product.getId());

				// Thêm vào danh sách BillDetails
				billDetailList.add(billDetailMap);
			}

			// Trả về thông tin chi tiết về các BillDetail
			result.put("products", billDetailList);
		} catch (Exception e) {
			// Xử lý lỗi nếu có
			result.put("error", "An error occurred: " + e.getMessage());
		}
		return result;
	}

	private Map<String, Object> getInfoUser(Integer billId) {
		Map<String, Object> userInfo = new HashMap<>();

		// Lấy accountId từ billRepository
		Integer accountId = billRepository.findUserById(billId).get();

		if (accountId == null) {
			throw new NoSuchElementException("User không tồn tại: " + billId);
		}

		// Lấy thông tin người dùng từ accountRepository
		Account user = accountRepository.findById(accountId)
				.orElseThrow(() -> new NoSuchElementException("User không tồn tại với accountId: " + accountId));

		// Thêm thông tin người dùng vào Map
		userInfo.put("userFullname", user.getFullname());
		userInfo.put("userPhone", user.getPhone());

		// Lấy địa chỉ từ billRepository, nếu không có trả về chuỗi rỗng
		String address = billRepository.findAddressByBill(billId);
		userInfo.put("userAddress", (address != null) ? address : "");

		return userInfo;
	}

	// public List<Response_BillDetail_User>
	// createBillsWithProductsInBillDetail(Integer billId) {
	// // Map<Integer, Response_BillDetail_User> billMap = new HashMap<>();
	// // List<Object[]> billDetail =
	// billDetailRepository.findBillDetailById(billId);
	// // List<Response_BillDetail_User> productDetail = new ArrayList<>();
	// // Set<Integer> idProduct = new HashSet<>();
	// // for (Object[] product : billDetail) {
	// // Integer billID = Integer.parseInt(product[1].toString());

	// // Response_BillDetail_User billData = billMap.getOrDefault(billID,
	// // createBillDetailUser(product));
	// // // Response_Bill_Product_User productData =
	// createBillProductUser(product);

	// // // if (!idProduct.contains(productData.getProductId())) {
	// // // billData.getProducts().add(productData);
	// // // billMap.put(billID, billData);
	// // // }
	// // // idProduct.add(productData.getProductId());
	// // }
	// // productDetail.addAll(billMap.values());

	// Optional<Bill> billInfoOptional = billRepository.findById(billId);
	// Bill billInfo = billInfoOptional.get();

	// Object userInfo = billDetailRepository.findAccountById(billId);

	// Object[] productInfo = billDetailRepository.findProductById(billId);

	// Object shopInfo = productRepository.findByProduct(productInfo[0]);

	// return productDetail;
	// }

	// private Response_BillDetail_User createBillDetailUser(Object[] product) {
	// Integer billID = Integer.parseInt(product[1].toString());
	// Integer userID = Integer.parseInt(product[0].toString());
	// Double billTotalPrice = Double.parseDouble(product[2].toString());
	// Double billDiscountPrice = Double.parseDouble(product[3].toString());
	// Double billTotalShippingPrice = Double.parseDouble(product[4].toString());
	// Integer billTotalQuantity = Integer.parseInt(product[5].toString());
	// Integer billAddressId = Integer.parseInt(product[6].toString());
	// Integer orderStatusID = Integer.parseInt(product[7].toString());
	// String createdDatetime = product[8].toString();
	// String updatedDatetime = product[9].toString();
	// Double billDiscountRate = Double.parseDouble(product[10].toString());
	// Integer shopId = Integer.parseInt(product[18].toString());
	// String shopName = product[19].toString();
	// String shopAvatar = product[20].toString();
	// String userFullname = product[21].toString();
	// String userPhone = product[22].toString();
	// String orderStatus =
	// orderStatusRepository.findById(orderStatusID).get().getName();
	// String address =
	// addressRepository.findById(billAddressId).get().getFullNameAddress();
	// Double billTempPrice = billTotalPrice + billDiscountPrice -
	// billTotalShippingPrice;

	// Response_BillDetail_User billData = new Response_BillDetail_User();
	// billData.setBillId(billID);
	// billData.setUserId(userID);
	// billData.setTotalPrice(billTotalPrice);
	// billData.setDiscountPrice(billDiscountPrice);
	// billData.setTempPrice(billTempPrice);
	// billData.setTotalShippingPrice(billTotalShippingPrice);
	// billData.setTotalQuantity(billTotalQuantity);
	// billData.setAddress(address);
	// billData.setOrderStatusId(orderStatusID);
	// billData.setOrderStatus(orderStatus);
	// billData.setCreatedDatetime(formatDate(createdDatetime));
	// billData.setUpdatedDatetime(formatDate(updatedDatetime));
	// billData.setDiscountPrice(billDiscountRate);
	// billData.setUserFullname(userFullname);
	// billData.setUserPhone(userPhone);

	// Response_Bill_Shop_User shopData = new Response_Bill_Shop_User();
	// shopData.setShopId(shopId);
	// shopData.setShopName(shopName);
	// shopData.setShopAvatar(shopAvatar);
	// billData.setShop(shopData);

	// billData.setProducts(new ArrayList<>());
	// return billData;
	// }

	// private Response_Bill_Product_User createBillProductUser(Object[] product) {
	// Integer productID = Integer.parseInt(product[11].toString());
	// String productName = product[12].toString();
	// String productIntroduce = product[13].toString();
	// Integer productQuantity = Integer.parseInt(product[14].toString());
	// Double productPrice = Double.parseDouble(product[15].toString());
	// Double productDiscountPrice = Double.parseDouble(product[16].toString());
	// String productImageURL = product[17].toString();
	// Integer userID = Integer.parseInt(product[0].toString());
	// Integer billID = Integer.parseInt(product[1].toString());
	// Response_Bill_Product_User productData = new Response_Bill_Product_User();
	// productData.setProductId(productID);
	// productData.setProductName(productName);
	// productData.setProductIntroduce(productIntroduce);
	// productData.setProductQuantity(productQuantity);
	// productData.setProductPrice(productPrice);
	// productData.setProductDiscountPrice(productDiscountPrice);
	// productData.setProductImageURL(productImageURL);

	// Integer billDetailId =
	// billDetailRepository.findBillDetailByProductIdAndAccountId(userID, productID,
	// billID);
	// productData.setBillDetailId(billDetailId);

	// Integer isEvalued = evaluateRepository.isEvaluate(billDetailId, productID,
	// userID);
	// // productData.setIsEvaluate(isEvalued == 1);

	// return productData;
	// }

	public void cancelBill(Integer billId) {
		checkBillDetailStatus(billId, 1);
		Bill bill = billRepository.findById(billId).get();
		bill.setUpdateAt(new Date());
		bill.setFinishAt(new Date());
		bill.setOrderStatus(orderStatusRepository.findById(6).get());
		billRepository.saveAndFlush(bill);
	}

	public void confirmBill(Integer billId) {
		checkBillDetailStatus(billId, 4);
		Bill bill = billRepository.findById(billId).get();
		bill.setUpdateAt(new Date());
		bill.setFinishAt(new Date());
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
		if (billRepository.findById(billId).isEmpty() || billId == null) {
			// throw new CustomException("Đơn hàng không tồn tại", "error");
		}
		if (orderStatusRepository.findById(orderStatusId).isEmpty()) {
			// throw new CustomException("Trạng thái đơn hàng không tồn tại", "error");
		}
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