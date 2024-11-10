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
import com.toel.repository.BillRepository;
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
	    Map<String, String> errors = new HashMap<>();

		try {
			validateEntitiesExist(requestEvaluateDTO);
			
		      if (!errors.isEmpty()) {
		            // Nếu có lỗi, trả về thông báo lỗi
		            response.put("status", "fail");
		            response.put("errors", errors);
		        } else {
		            Evalue evaluate = createEvaluate(requestEvaluateDTO);
		            response.put("message", "Gửi đánh giá thành công");
		            response.put("status", "success");
		        }
		    } catch (IllegalArgumentException e) {
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
		
		Integer star = requestEvaluateDTO.getStar();
		String content = requestEvaluateDTO.getContent();
		evaluate.setStar(star);
		evaluate.setContent(content);

		Integer accountId = requestEvaluateDTO.getAccountId();
		Account account = new Account();
		account.setId(accountId);
		evaluate.setAccount(account);

		Integer productId = requestEvaluateDTO.getProductId();
		Product product = new Product();
		product.setId(productId);
		evaluate.setProduct(product);

		Integer billDetailId = requestEvaluateDTO.getBillDetailId();
		BillDetail billDetail = new BillDetail();
		billDetail.setId(billDetailId);
		evaluate.setBillDetail(billDetail);

		evaluateRepository.save(evaluate);

		MultipartFile[] images = requestEvaluateDTO.getImages();
		if (images != null && images.length > 0) {
			List<ImageEvalue> imageEvalues = saveImages(images, evaluate);
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

		if (width > 1920 || height > 1080) {
			throw new IllegalArgumentException("Độ phân giải ảnh không vượt quá 1920 x 1080");
		}
		if (size > 5 * 1024 * 1024) {
			throw new IllegalArgumentException("Kích thước ảnh không vượt quá 5MB");
		}

	}

	private List<ImageEvalue> saveImages(MultipartFile[] imageFiles, Evalue evaluate) {
		List<ImageEvalue> imageEvaluate = new ArrayList<>();

		for (MultipartFile imageFile : imageFiles) {
			if (!imageFile.isEmpty()) {
				try {
					ImageEvalue image = new ImageEvalue();

					String imageFirsebaseURL = firebaseUploadImages.uploadFile("evalue", imageFile);
					if (imageFirsebaseURL == null || imageFirsebaseURL.isEmpty()) {
						throw new RuntimeException("Failed to upload image to Firebase.");
					}

					image.setName(imageFirsebaseURL);
					image.setEvalue(evaluate);
					imageEvaluateRepository.saveAndFlush(image);

					imageEvaluate.add(image);
				} catch (Exception e) {
					e.printStackTrace();
					throw new EntityNotFoundException("");
				}
			}
		}

		return imageEvaluate;
	}
}
