package com.toel.service.user;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.user.resquest.pay.Request_Cart;
import com.toel.dto.user.resquest.pay.Request_Pay;
import com.toel.dto.user.resquest.pay.Request_SellerOrder;
import com.toel.model.Account;
import com.toel.model.Address;
import com.toel.model.Bill;
import com.toel.model.BillDetail;
import com.toel.model.Cart;
import com.toel.model.DiscountRate;
import com.toel.model.OrderStatus;
import com.toel.model.PaymentMethod;
import com.toel.model.Product;
import com.toel.model.Voucher;
import com.toel.model.VoucherDetail;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.CartRepository;
import com.toel.repository.DiscountRateRepository;
import com.toel.repository.OrderStatusRepository;
import com.toel.repository.PaymentMethodRepository;
import com.toel.repository.ProductRepository;
import com.toel.repository.VoucherDetailRepository;
import com.toel.repository.VoucherRepository;

import jakarta.mail.FetchProfile.Item;

@Service
public class Service_Pay {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	DiscountRateRepository discountRateRepository;
	@Autowired
	BillRepository billRepository;
	@Autowired
	VoucherRepository voucherRepository;
	@Autowired
	VoucherDetailRepository voucherDetailRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	BillDetailRepository billDetailRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	PaymentMethodRepository paymentMethodRepository;
	@Autowired
	OrderStatusRepository orderStatusRepository;

	public void createOrder(Request_Pay pay, Integer id_user, Integer paymentMethod_id) {
		OrderStatus orderStatus = orderStatusRepository.findById(1).get();
		PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethod_id).get();
		DiscountRate discountRate = discountRateRepository.find(LocalDateTime.now());
		Account user = accountRepository.findById(id_user).get();
		Address toAddress = user.getAddresses().stream().filter(address -> address.isStatus() == true).findFirst()
				.orElse(null);
		for (Request_SellerOrder sellerItem : pay.getDatas()) {
			Bill bil = new Bill();
			bil.setAccount(user);
			bil.setAddress(toAddress);
			bil.setCreateAt(new Date());
			bil.setDiscountPrice(sellerItem.getSale());
			bil.setFinishAt(null);
			bil.setPriceShipping(sellerItem.getService_fee());
			bil.setDiscountRate(discountRate);
			bil.setTotalPrice(sellerItem.getTotal());
			bil.setPaymentMethod(paymentMethod);
			bil.setOrderStatus(orderStatus);
			bil = billRepository.save(bil);
			try {
				if (sellerItem.getVoucher() != null) {
					VoucherDetail voucherSeller = new VoucherDetail();
					Voucher voucher = voucherRepository.findById(sellerItem.getVoucher().getId()).get();
					voucherSeller.setBill(bil);
					voucherSeller.setAccount(user);
					voucherSeller.setVoucher(voucher);
					voucherSeller = voucherDetailRepository.save(voucherSeller);
					voucher.setQuantity(voucher.getQuantity() - 1);
					voucherRepository.save(voucher);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				if (sellerItem.getVoucherAdmin() != null) {
					VoucherDetail voucherAdmin = new VoucherDetail();
					Voucher voucher = voucherRepository.findById(sellerItem.getVoucherAdmin().getId()).get();
					voucherAdmin.setBill(bil);
					voucherAdmin.setAccount(user);
					voucherAdmin.setVoucher(voucher);
					voucherAdmin = voucherDetailRepository.save(voucherAdmin);
					voucher.setQuantity(voucher.getQuantity() - 1);
					voucherRepository.save(voucher);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			for (Request_Cart cartItem : sellerItem.getCart()) {
				Product product = productRepository.findById(cartItem.getProduct().getId()).get();
				BillDetail billDetail = new BillDetail();
				billDetail.setBill(bil);
				billDetail.setPrice(product.getPrice() - ((product.getSale() * product.getPrice()) / 100));
				billDetail.setQuantity(cartItem.getQuantity());
				billDetail.setProduct(product);
				billDetail = billDetailRepository.save(billDetail);
				product.setQuantity(product.getQuantity() - cartItem.getQuantity());
				product = productRepository.save(product);
				cartRepository.deleteById(cartItem.getId());
				bil.setTotalQuantity(bil.getTotalQuantity() + cartItem.getQuantity());
				bil = billRepository.save(bil);
			}
		}

	}

}
