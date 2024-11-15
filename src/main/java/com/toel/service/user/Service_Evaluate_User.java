package com.toel.service.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.toel.dto.user.resquest.Request_Evaluate_User;
import com.toel.model.Account;
import com.toel.model.BillDetail;
import com.toel.model.Evalue;
import com.toel.model.ImageEvalue;
import com.toel.model.Product;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.ImageEvalueRepository;
import com.toel.repository.ProductRepository;
import com.toel.service.firebase.UploadImage;

import jakarta.persistence.EntityNotFoundException;

@Service
public class Service_Evaluate_User {

	// Các repository
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
	@Autowired
	UploadImage firebaseUploadImages;

	public Map<String, Object> saveEvaluate(Request_Evaluate_User requestEvaluateDTO) {
		Map<String, Object> response = new HashMap<>();

		try {
			validateEntitiesExist(requestEvaluateDTO);

			Evalue evaluate = createEvaluate(requestEvaluateDTO);

			response.put("message", "Gửi đánh giá thành công");
			response.put("status", "successfully");
		} catch (IllegalArgumentException | EntityNotFoundException e) {
			response.put("message", e.getMessage());
			response.put("status", "fail");
		} catch (Exception e) {
			response.put("message", "An unexpected error occurred while saving the evaluation.");
			response.put("status", "error");
			e.printStackTrace();
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
		if (billDetailRepository.billDetailIsExisted(billDetailId, productId, accountId) == 0) {
			throw new EntityNotFoundException("Kiểm tra lại thông tin cần đánh giá");
		}
		if (evaluateRepository.isEvaluate(billDetailId, productId, accountId) == 1) {
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
		Evalue evaluate = new Evalue();
		evaluate.setCreateAt(new Date());
		evaluate.setIdParent(0);
		evaluate.setStar(requestEvaluateDTO.getStar());
		evaluate.setContent(requestEvaluateDTO.getContent());

		for (MultipartFile image : requestEvaluateDTO.getImages()) {
			System.out.println("image: "+image+"\n");
		}
		Account account = accountRepository.findById(requestEvaluateDTO.getAccountId())
				.orElseThrow(() -> new EntityNotFoundException("Tài khoản không tồn tại"));
		evaluate.setAccount(account);

		Product product = productRepository.findById(requestEvaluateDTO.getProductId())
				.orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tồn tại"));
		evaluate.setProduct(product);

		BillDetail billDetail = billDetailRepository.findById(requestEvaluateDTO.getBillDetailId())
				.orElseThrow(() -> new EntityNotFoundException("Đơn hàng không tồn tại"));
		evaluate.setBillDetail(billDetail);

		evaluateRepository.save(evaluate);

		if (requestEvaluateDTO.getImages() != null && requestEvaluateDTO.getImages().length > 0) {
			List<ImageEvalue> imageEvalues = saveImages(requestEvaluateDTO.getImages(), evaluate);
			evaluate.setImageEvalues(imageEvalues);
			evaluateRepository.save(evaluate);
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

		if (size > 5 * 1024 * 1024) {
			throw new IllegalArgumentException("Kích thước ảnh không vượt quá 5MB");
		}

	}

	private List<ImageEvalue> saveImages(MultipartFile[] imageFiles, Evalue evaluate) {
		List<ImageEvalue> imageEvalueList = new ArrayList<>();

		for (MultipartFile imageFile : imageFiles) {
			if (!imageFile.isEmpty()) {
				try {
					String imageFirebaseURL = firebaseUploadImages.uploadFile("evalue", imageFile);
					if (imageFirebaseURL == null || imageFirebaseURL.isEmpty()) {
						throw new IOException("Failed to upload image to Firebase: " + imageFile.getOriginalFilename());
					}

					ImageEvalue imageEvalue = new ImageEvalue();
					imageEvalue.setName(imageFirebaseURL);
					imageEvalue.setEvalue(evaluate);
					imageEvaluateRepository.saveAndFlush(imageEvalue);

					imageEvalueList.add(imageEvalue);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("Error processing image: " + imageFile.getOriginalFilename(), e);
				}
			}
		}

		return imageEvalueList;
	}
}
