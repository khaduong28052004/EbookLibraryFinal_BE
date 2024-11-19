package com.toel.service.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_Seller;
import com.toel.mapper.user.AccountMapperUser;
import com.toel.mapper.user.CartMapper;
import com.toel.mapper.user.UserMapper;
import com.toel.model.Account;
import com.toel.model.Cart;
import com.toel.model.TypeVoucher;
import com.toel.model.Voucher;
import com.toel.repository.AccountRepository;
import com.toel.repository.CartRepository;
import com.toel.repository.ProductRepository;
import com.toel.repository.TypeVoucherRepository;
import com.toel.repository.VoucherRepository;

@Service
public class Service_Cart {

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private AccountMapperUser accountMapper;

	@Autowired
	private CartMapper cartMapper;

	@Autowired
	AccountRepository accountRepository;
	@Autowired
	UserMapper userMapper;

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	ProductRepository productRepo;

	@Autowired
	TypeVoucherRepository typeVoucherRepository;

	public Map<String, Object> getCart(Integer id_User) {

		Map<String, Object> response = new HashMap<>();
		Optional<Account> userOptional = accountRepo.findById(id_User);
		Account user = userOptional.get();
		// Kiểm tra nếu người dùng tồn tại
		if (!userOptional.isPresent()) {
			response.put("error", "User not found");
			return response;
		}

		List<Cart> listCart = cartRepo.findAllByAccount(user);
		Map<Integer, Response_Seller> sellerMap = new HashMap<>();

		for (Cart item : listCart) {
			Integer sellerId = item.getProduct().getAccount().getId();

			// Nếu seller chưa có trong Map, tạo mới
			Response_Seller responseSeller = sellerMap.computeIfAbsent(sellerId,
					id -> accountMapper.Response_Seller_MapperTo_Account(item.getProduct().getAccount()));

			// Thêm sản phẩm vào giỏ hàng của seller
			responseSeller.getCart().add(cartMapper.response_Cart_MapperTo_Cart(item));
			List<Voucher> listVoucher = voucherRepository.findAllByAccount(item.getProduct().getAccount());
			responseSeller.setVouchers(new ArrayList<Voucher>());
			for (Voucher voucher : listVoucher) {
				responseSeller.getVouchers().add(voucher);
			}
			System.out.println("dem --------------------------");
		}

		// Thêm các seller vào response
		response.put("datas", new ArrayList<>(sellerMap.values()));
		response.put("user", userMapper.UserMapperToAccount(user));
		return response;
	}

	public Map<String, Object> addCart(Integer id_user, Integer quantity, Integer id_product) {
		Cart cart = new Cart();
		try {
			cart = cartRepo.findByProductAndAccount(productRepo.findById(id_product).get(),
					accountRepo.findById(id_user).get());
			cart.setQuantity(cart.getQuantity() + quantity);
			Map<String, Object> response = new HashMap<>();
			response.put("cart", cartRepo.save(cart));
			return response;
		} catch (Exception e) {
		}
		cart = new Cart();
		cart.setQuantity(quantity);
		cart.setProduct(productRepo.findById(id_product).get());
		cart.setAccount(accountRepo.findById(id_user).get());
		Map<String, Object> response = new HashMap<>();
		response.put("cart", cartRepo.save(cart));
		return response;

	}

	public Map<String, Object> removeCart(Integer id_cart) {
		Map<String, Object> response = new HashMap<>();
		try {
			cartRepo.deleteById(id_cart);
			response.put("cart", "success");
			return response;
		} catch (Exception e) {
		}
		return null;
	}

	public Map<String, Object> getVoucherAdmin() {
		TypeVoucher typeVoucher = typeVoucherRepository.findById(1).get();
		List<Voucher> listVouchers = voucherRepository.findAllByTypeVoucher(typeVoucher, new Date());
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("datas", listVouchers);
		return response;
	}
}
