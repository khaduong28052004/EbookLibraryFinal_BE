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

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageInputStreamImpl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.toel.dto.user.response.Response_Bill_User;
import com.toel.dto.user.response.Response_Bill_Shop_User;
import com.toel.dto.user.response.Response_Bill_Product_User;
import com.toel.dto.user.resquest.Request_Bill_User;
import com.toel.dto.user.resquest.Request_Evaluate_User;
import com.toel.model.Account;
import com.toel.model.Address;
import com.toel.model.Bill;
import com.toel.model.BillDetail;
import com.toel.model.Cart;
import com.toel.model.Evalue;
import com.toel.model.ImageEvalue;
import com.toel.model.OrderStatus;
import com.toel.model.Product;
import com.toel.repository.AccountRepository;
import com.toel.repository.AddressRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.CartRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.ImageEvalueRepository;
import com.toel.repository.OrderStatusRepository;
import com.toel.repository.ProductReportRepository;
import com.toel.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class Service_Evaluate_User {

	// Các repository
	@Autowired
	private BillRepository billRepository;
	@Autowired
	private BillDetailRepository billDetailRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private EvalueRepository evaluateRepository;
	@Autowired
	private ImageEvalueRepository imageEvaluateRepository;

	public Map<String, Object> saveEvaluate(Request_Evaluate_User requestEvaluateDTO) {
		Map<String, Object> response = new HashMap<>();

		try {
			validateEntitiesExist(requestEvaluateDTO);
			createEvaluate(requestEvaluateDTO);
			response.put("message", "Gửi đánh giá thành công");
			response.put("status", "success");
		} catch (EntityNotFoundException | IllegalArgumentException e) {
			response.put("message", e.getMessage());
			response.put("status", "fail");
		}

		return response;
	}

	private void validateEntitiesExist(Request_Evaluate_User requestEvaluateDTO) throws EntityNotFoundException {
		Integer star = requestEvaluateDTO.getStar() == null ? 0 : requestEvaluateDTO.getStar();
		Integer billDetailId = requestEvaluateDTO.getBillDetailId();
		Integer productId = requestEvaluateDTO.getProductId();
		Integer accountId = requestEvaluateDTO.getAccountId();
		MultipartFile[] images = requestEvaluateDTO.getImages();

		if (star <= 0) {
			throw new EntityNotFoundException("Vui lòng để lại đánh giá");
		}
		if (!accountRepository.existsById(accountId)) {
			throw new EntityNotFoundException("Tài khoản không tồn tại");
		}
		if (!productRepository.existsById(productId)) {
			throw new EntityNotFoundException("Sản phẩm không tồn tại");
		}
		if (!billDetailRepository.existsById(billDetailId)) {
			throw new EntityNotFoundException("Đơn hàng không tồn tại");
		}
		if (evaluateRepository.isEvaluate(billDetailId) == 1) {
			throw new EntityNotFoundException("Sản phẩm đã đánh giá");
		}

		if (images != null) {
			for (MultipartFile image : images) {
				try {
					if (!isValidImageFormat(image)) {
						throw new IllegalArgumentException("Chỉ chấp nhận các định dạng jpg, jpeg, và png");
					}
					checkImageAttributes(image);
				} catch (IOException e) {
					throw new IllegalArgumentException("Lỗi khi xử lý ảnh: " + image.getOriginalFilename(), e);
				}
			}
		}
	}

	private Evalue createEvaluate(Request_Evaluate_User requestEvaluateDTO) {
		Integer billDetailId = requestEvaluateDTO.getBillDetailId();
		Integer productId = requestEvaluateDTO.getProductId();
		Integer accountId = requestEvaluateDTO.getAccountId();
		String content = requestEvaluateDTO.getContent();
		Integer star = requestEvaluateDTO.getStar();
		MultipartFile[] images = requestEvaluateDTO.getImages();

		Evalue evaluate = new Evalue();
		evaluate.setStar(star);
		evaluate.setContent(content);

		Account account = new Account();
		account.setId(accountId);
		evaluate.setAccount(account);

		Product product = new Product();
		product.setId(productId);
		evaluate.setProduct(product);

		BillDetail billDetail = new BillDetail();
		billDetail.setId(billDetailId);
		evaluate.setBillDetail(billDetail);

		evaluateRepository.saveAndFlush(evaluate);

		if (images != null && images.length > 0) {
			List<ImageEvalue> imageList = saveImages(images, evaluate);
			evaluate.setImageEvalues(imageList);
			evaluateRepository.saveAndFlush(evaluate);
		}

		return evaluate;
	}

	private boolean isValidImageFormat(MultipartFile image) {
		try (ImageInputStream iis = ImageIO.createImageInputStream(image.getInputStream())) {
			Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				String formatName = reader.getFormatName().toLowerCase();
				return formatName.equals("jpg") || formatName.equals("jpeg") || formatName.equals("png");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void checkImageAttributes(MultipartFile image) throws IOException {

		BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

		if (bufferedImage == null) {
			throw new IllegalArgumentException("Không thể đọc ảnh: " + image.getOriginalFilename());
		}

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		long size = image.getSize();

		if (width > 1920 || height > 1080) {
			throw new IllegalArgumentException("Độ phân giải ảnh không vượt quá 1920 x 1080");
		}
		if (size > 5 * 1024 * 1024) {
			throw new IllegalArgumentException("Kích thước ảnh không vượt quá 5MB");
		}

	}

	private List<ImageEvalue> saveImages(MultipartFile[] imageFiles, Evalue evaluate) {
		List<ImageEvalue> images = new ArrayList<>();
		for (MultipartFile imageFile : imageFiles) {
			if (!imageFile.isEmpty()) {
				try {
					// String imageFirsebase = firebaseStorageService.uploadFile("evalue",imageFile);
					String imageFirsebase = "img templ, need change code";
					ImageEvalue image = saveImageMetadata(imageFirsebase, evaluate); // Lưu thông tin ảnh
					images.add(image);
				} catch (Exception e) {
					e.printStackTrace();
					throw new EntityNotFoundException("");
				}
			}
		}
		return images;
	}

	private ImageEvalue saveImageMetadata(String imageFile, Evalue evaluate) {
		ImageEvalue image = new ImageEvalue();
		image.setName(imageFile);
		image.setEvalue(evaluate);
		return imageEvaluateRepository.save(image);
	}
}
