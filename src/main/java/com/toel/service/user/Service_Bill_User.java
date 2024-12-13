package com.toel.service.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_Bill_User;
import com.toel.dto.BillDTO;
import com.toel.dto.user.resquest.Request_Bill_User;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.BillMapper;
import com.toel.model.Account;
import com.toel.model.Bill;
import com.toel.model.Cart;
import com.toel.model.FlashSaleDetail;
import com.toel.model.Product;
import com.toel.model.Voucher;
import com.toel.model.VoucherDetail;
import com.toel.repository.AccountRepository;
import com.toel.repository.AddressRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.CartRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.FlashSaleDetailRepository;
import com.toel.repository.OrderStatusRepository;
import com.toel.repository.PaymentMethodRepository;
import com.toel.repository.ProductRepository;
import com.toel.repository.VoucherDetailRepository;
import com.toel.repository.VoucherRepository;
import com.toel.service.Email.EmailService;
import com.toel.service.Email.EmailTemplateType;

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

	@Autowired
	BillMapper billMapper;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	VoucherDetailRepository voucherDetailRepository;

	@Autowired
	FlashSaleDetailRepository flashSaleDetailRepository;

	@Autowired
	VoucherRepository voucherRepository;

	// public Map<String, Object> getBills(Request_Bill_User requestBillDTO) {
	// Map<String, Object> response = new HashMap<>();
	// try {
	// List<Object[]> productsInBill = getBillsByOrderStatus(requestBillDTO);
	// List<Response_Bill_User> listConver =
	// convertToResponseBillUser(productsInBill);
	// List<BillDTO> shopListInBill =
	// createBillsWithProductsInBillDetail(listConver);

	// int page = requestBillDTO.getPage();
	// int size = requestBillDTO.getSize();
	// int totalItems = shopListInBill.size();
	// int totalPages = (int) Math.ceil((double) totalItems / size);

	// // Lấy dữ liệu cho trang hiện tại
	// int start = Math.min(page * size, totalItems);
	// int end = Math.min(start + size, totalItems);
	// List<BillDTO> paginatedList = shopListInBill.subList(start, end);

	// response.put("data", paginatedList); // Dữ liệu cho trang hiện tại
	// response.put("currentPage", page);
	// response.put("totalItems", totalItems);
	// response.put("totalPages", totalPages);
	// response.put("pageSize", size);

	// // response.put("data", shopListInBill);
	// response.put("status", "successfully");
	// response.put("message", "Retrieve data successfully");
	// } catch (Exception e) {
	// response.put("status", "error");
	// response.put("message", "An error occurred while retrieving orders.");
	// response.put("error", e.getMessage());
	// }
	// return response;
	// }

	public Map<String, Object> getBills(Request_Bill_User requestBillDTO) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Object[]> productsInBill = getBillsByOrderStatus(requestBillDTO);
			List<Response_Bill_User> listConver = convertToResponseBillUser(productsInBill);
			List<BillDTO> shopListInBill = createBillsWithProductsInBillDetail(listConver);

			// Lấy dữ liệu cho trang hiện tại

			response.put("data", shopListInBill); // Dữ liệu cho trang hiện tại
			// response.put("currentPage", page);
			// response.put("totalItems", totalItems);
			// response.put("totalPages", totalPages);
			// response.put("pageSize", size);

			// response.put("data", shopListInBill);
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
		Pageable pageable = PageRequest.of(0, BillShopRequestDTO.getSize());

		switch (orderStatus) {
			case "CHODUYET":
				return billRepository.findBillsByUserIdAndOrderStatusOrderedByCreateOrUpdate(userId, 1, "create",
						pageable); // Status
			case "DANGXULY":
				return billRepository.findBillsByUserIdAndOrderStatusOrderedByCreateOrUpdate(userId, 2, "update",
						pageable); // Status
			case "DANGGIAO":
				return billRepository.findBillsByUserIdAndOrderStatusOrderedByCreateOrUpdate(userId, 3, "update",
						pageable); // Status
			case "DAGIAO":
				return billRepository.findBillsByUserIdAndOrderStatusOrderedByCreateOrUpdate(userId, 4, "update",
						pageable); // Status
			case "HOANTHANH":
				return billRepository.findBillsByUserIdAndOrderStatusOrderedByCreateOrUpdate(userId, 5, "update",
						pageable); // Status
			case "DAHUY":
				return billRepository.findBillsByUserIdAndOrderStatusOrderedByCreateOrUpdate(userId, 6, "update",
						pageable); // Status
			default:
				return billRepository.findBillsByUserId(userId, pageable); // Default: No filter on order status
		}
	}

	public List<Response_Bill_User> convertToResponseBillUser(List<Object[]> result) {
		List<Response_Bill_User> bills = new ArrayList<>();
		for (Object[] row : result) {
			Response_Bill_User bill = new Response_Bill_User();
			bill.setBillId((Integer) row[0]);
			bill.setUserId((Integer) row[1]);
			bill.setTotalPriceBill((Double) row[2]);
			bill.setPriceShippingBill((Double) row[3]);
			bill.setTotalQuantityBill((Integer) row[4]);
			bill.setOrderStatus((String) row[5]);
			bill.setCreatedDatetime((Date) row[6]);
			bill.setUpdatedDatetime((Date) row[7]);
			bill.setPaymentMethod((String) row[8]);
			bills.add(bill);
		}
		return bills;
	}

	public List<BillDTO> createBillsWithProductsInBillDetail(List<Response_Bill_User> productsInBill) {
		List<BillDTO> billList = new ArrayList<>();

		for (Response_Bill_User bill : productsInBill) {
			BillDTO newBill = new BillDTO();
			Integer billId = bill.getBillId();
			Integer userId = bill.getUserId();
			Double totalPriceBill = bill.getTotalPriceBill();
			Double priceShippingBill = bill.getPriceShippingBill();
			Integer totalBillQuantity = bill.getTotalQuantityBill();
			String orderStatus = bill.getOrderStatus();
			Date createAt = bill.getCreatedDatetime();
			Date updateAt = bill.getUpdatedDatetime();
			String paymentMethod = bill.getPaymentMethod().toUpperCase();

			newBill.setBillId(billId);
			newBill.setUserId(userId);
			newBill.setTotalPriceBill(totalPriceBill);
			newBill.setPriceShippingBill(priceShippingBill);
			newBill.setOrderStatus(orderStatus);
			newBill.setCreatedDatetime(createAt);
			newBill.setUpdatedDatetime(updateAt);
			newBill.setTotalQuantityBill(totalBillQuantity);
			newBill.setPaymentMethod(paymentMethod);

			billList.add(newBill);
		}
		return billList;
	}

	public Bill cancelBill(Integer billId) {
		checkBillStatus(billId, 1);
		Bill bill = billRepository.findById(billId).get();
		Account user = bill.getAccount();

		// if (checkAndBlockUsers(user.getId()) > 2) {
		// throw new AppException(ErrorCode.OBJECT_SETUP,
		// "Tài khoản của bạn đã bị khóa, do đã hủy đơn quá nhiều lần trong ngày. Vui
		// lòng liên hệ TOEL để mở khóa");
		// }

		bill.setUpdateAt(new Date());
		bill.setFinishAt(new Date());
		bill.setOrderStatus(orderStatusRepository.findById(6).get());
		billRepository.saveAndFlush(bill);
		returnStatus(bill);

		sendNotification(bill);

		return billRepository.saveAndFlush(bill);
	}

	public void returnStatus(Bill bill) {
		try {
			List<Product> updatedProducts = new ArrayList<>();
			List<FlashSaleDetail> updatedFlashSaleDetails = new ArrayList<>();
			List<VoucherDetail> voucherDetailsToDelete = new ArrayList<>();
			List<Voucher> updatedVouchers = new ArrayList<>();

			bill.getBillDetails().forEach(billDetail -> {
				Product product = billDetail.getProduct();
				product.setQuantity(product.getQuantity() + billDetail.getQuantity());
				updatedProducts.add(product);

				if (billDetail.getFlashSaleDetail() != null) {
					FlashSaleDetail flashSaleDetail = billDetail.getFlashSaleDetail();
					flashSaleDetail.setQuantity(flashSaleDetail.getQuantity() + billDetail.getQuantity());
					updatedFlashSaleDetails.add(flashSaleDetail);
				}
			});

			if (bill.getVoucherDetails() != null) {
				bill.getVoucherDetails().forEach(voucherDetails -> {
					Voucher voucher = voucherDetails.getVoucher();
					voucher.setQuantity(voucher.getQuantity() + 1);
					updatedVouchers.add(voucher);
					voucherDetailsToDelete.add(voucherDetails);
				});
			}
			if (!updatedProducts.isEmpty()) {
				productRepository.saveAll(updatedProducts);
			}
			if (!updatedFlashSaleDetails.isEmpty()) {
				flashSaleDetailRepository.saveAll(updatedFlashSaleDetails);
			}
			if (!updatedVouchers.isEmpty()) {
				voucherRepository.saveAll(updatedVouchers);

			}
			if (!voucherDetailsToDelete.isEmpty()) {
				voucherDetailRepository.deleteAll(voucherDetailsToDelete);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "System");
		}
	}

	public void confirmBill(Integer billId) {
		checkBillStatus(billId, 4);
		Bill bill = billRepository.findById(billId).get();
		bill.setUpdateAt(new Date());
		bill.setFinishAt(new Date());
		bill.setOrderStatus(orderStatusRepository.findById(5).get());
		billRepository.saveAndFlush(bill);

	}

	// @Scheduled(fixedRate = 60000) // 60000ms = 1 phút
	@Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
	public void autoConfirmOrders() {
		System.out.println("Tự động xác nhận hóa đơn đang chạy lúc: " + LocalDateTime.now());
		// Get the current date and the date 7 days ago
		LocalDate currentDate = LocalDate.now();
		LocalDate sevenDaysAgo = currentDate.minusDays(7);
		LocalDateTime sevenDaysAgoDateTime = sevenDaysAgo.atStartOfDay();

		// Get all orders where 'finishAt' is older than 7 days and not yet confirmed
		// (orderStatus is not 'confirmed')
		List<Bill> billsToConfirm = billRepository.findBillsToAutoConfirm(sevenDaysAgoDateTime);

		// Loop through each bill and confirm it
		for (Bill bill : billsToConfirm) {
			confirmBill(bill.getId());
		}
	}

	private void sendNotification(Bill bill) {
		String email = bill.getAccount().getEmail();
		System.out.println("email " + email);
		String subject = "TOEL - Thông báo cập nhật trạng thái hủy đơn hàng ";
		String content = " Khách hàng đã hủy đơn của shop. Xin cảm ơn vì sử dụng dịch vụ bán hàng trên TOEL.";
		emailService.push(email, subject, EmailTemplateType.HUYDON,
				bill.getBillDetails().get(0).getProduct().getAccount().getShopName(),
				String.valueOf(bill.getId()), content);

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