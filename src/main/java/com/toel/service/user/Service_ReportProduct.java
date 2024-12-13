package com.toel.service.user;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.toel.dto.user.resquest.Request_ProductReport;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.user.ProductReportUserMapper;
import com.toel.model.Account;
import com.toel.model.ImageProductReport;
import com.toel.model.Product;
import com.toel.model.ProductReport;
import com.toel.repository.AccountRepository;
import com.toel.repository.ImageProductReportRepository;
import com.toel.repository.ProductReportRepository;
import com.toel.repository.ProductRepository;
import com.toel.service.firebase.UploadImage;

import jakarta.persistence.EntityNotFoundException;

@Service
public class Service_ReportProduct {
	@Autowired
	ProductRepository productRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	ProductReportRepository productReportRepository;
	@Autowired
	ProductReportUserMapper mapper;
	@Autowired
	UploadImage firebaseUploadImages;
	@Autowired
	ImageProductReportRepository imageProductReportRepository;

	public boolean report(Request_ProductReport request) {
		Product product = productRepository.findById(request.getId_product()).get();
		Account account = accountRepository.findById(request.getId_user()).get();
		ProductReport productReport = mapper.productReportToRequesProductReport(request);
		productReport.setCreateAt(new Date());
		productReport.setStatus(false);
		productReport.setAccount(account);
		productReport.setProduct(product);
		productReport = productReportRepository.save(productReport);
		saveImages(request.getImages(), productReport);
		if (productReportRepository != null) {
			return true;
		}
		return false;
	}

	public Boolean checkReport(Integer id_user, Integer id_product) {
		try {
			Product product = productRepository.findById(id_product).get();
			Account account = accountRepository.findById(id_user).get();
			ProductReport productReport = productReportRepository.findByAccountAndProduct(account, product).get(0);
			if (productReport != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private void validateEntitiesExist(Request_ProductReport report) throws EntityNotFoundException {
		MultipartFile[] images = report.getImages();
		if (images != null) {
			for (MultipartFile image : images) {
				try {
					if (!isValidImageFormat(image)) {
						throw new AppException(ErrorCode.OBJECT_SETUP, "Chỉ chấp nhận các định dạng jpg, jpeg, và png");
					}
					checkImageAttributes(image);
				} catch (IOException e) {
					throw new AppException(ErrorCode.OBJECT_SETUP, "Lỗi khi xử lý ảnh: " + image.getOriginalFilename(),
							e);
				}
			}
		}

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
			throw new AppException(ErrorCode.OBJECT_SETUP, "Không thể đọc ảnh: " + image.getOriginalFilename());
		}

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		long size = image.getSize();

		if (size > 5 * 1024 * 1024) {
			throw new AppException(ErrorCode.OBJECT_SETUP, "Kích thước ảnh không vượt quá 5MB");
		}

	}

	private List<ImageProductReport> saveImages(MultipartFile[] imageFiles, ProductReport productReport) {
		List<ImageProductReport> imageEvalueList = new ArrayList<>();

		for (MultipartFile imageFile : imageFiles) {
			if (!imageFile.isEmpty()) {
				try {
					String imageFirebaseURL = firebaseUploadImages.uploadFile("evalue", imageFile);
					if (imageFirebaseURL == null || imageFirebaseURL.isEmpty()) {
						throw new AppException(ErrorCode.OBJECT_SETUP,
								"Failed to upload image to Firebase: " + imageFile.getOriginalFilename());
					}
					ImageProductReport imageReportProduct = new ImageProductReport();
					imageReportProduct.setSrc(imageFirebaseURL);
					imageReportProduct.setProductReport(productReport);
					imageProductReportRepository.saveAndFlush(imageReportProduct);

					imageEvalueList.add(imageReportProduct);
				} catch (Exception e) {
					e.printStackTrace();
					throw new AppException(ErrorCode.OBJECT_SETUP,
							"Error processing image: " + imageFile.getOriginalFilename(), e);
				}
			}
		}
		return imageEvalueList;
	}

}
