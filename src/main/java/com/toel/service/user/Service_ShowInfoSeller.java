package com.toel.service.user;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.toel.dto.user.response.Response_Product;
import com.toel.dto.user.resquest.Request_ReportShop_DTO;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.user.ProductMaperUser;
import com.toel.model.Account;
import com.toel.model.AccountReport;
import com.toel.model.FlashSaleDetail;
import com.toel.model.ImageAccountReport;
import com.toel.model.Product;
import com.toel.repository.AccountRepository;
import com.toel.repository.ImageAccountReportReposity;
import com.toel.repository.ProductRepository;
import com.toel.repository.ReportRepository;
import com.toel.service.firebase.UploadImage;

import jakarta.persistence.EntityNotFoundException;

@Service
public class Service_ShowInfoSeller {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    UploadImage firebaseUploadImages;
    @Autowired
    ImageAccountReportReposity imageAccountReport;
    @Autowired
    ProductRepository productRepo;
    @Autowired
    ProductMaperUser productMaper;

    public Map<String, Object> createReportShop(Request_ReportShop_DTO reportDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            validateEntitiesExist(reportDTO);
            createReport(reportDTO);
            response.put("message", "Báo cáo đã được gửi");
            response.put("status", HttpStatus.SC_OK);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("status", HttpStatus.SC_BAD_REQUEST);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("status", HttpStatus.SC_BAD_REQUEST);
            e.printStackTrace();
        }
        return response;
    }

    private void validateEntitiesExist(Request_ReportShop_DTO reportDTO) throws EntityNotFoundException {
        Integer accountId = reportDTO.getAccountId() == null ? 0 : reportDTO.getAccountId();
        Integer shopId = reportDTO.getShopId() == null ? null : reportDTO.getShopId();
        String content = reportDTO.getContent() == null ? "" : reportDTO.getContent();
        Date createAt = reportDTO.getCreateAt() == null ? null : reportDTO.getCreateAt();
        String title = reportDTO.getTitle() == null ? "" : reportDTO.getTitle();
        MultipartFile[] images = reportDTO.getImages();

        if (!accountRepository.existsById(accountId) || accountId == null) {
            throw new AppException(ErrorCode.OBJECT_SETUP, "Vui lòng đăng nhập");
        }
        if (!accountRepository.existsById(shopId) || shopId == null) {
            throw new AppException(ErrorCode.OBJECT_SETUP, "Cửa hàng không tồn tại");
        }
        if (content.trim().isBlank() || content == "") {
            throw new AppException(ErrorCode.OBJECT_SETUP, "Cần điền nội dung báo cáo");
        }
        System.out.println("createAt" + reportDTO.getCreateAt());
        if (createAt == null) {
            throw new AppException(ErrorCode.OBJECT_SETUP, "Thiếu trường createAt");
        }
        if (title == null) {
            throw new AppException(ErrorCode.OBJECT_SETUP, "Thiếu trường title");
        }
        if (reportRepository.waitAfterReport(accountId, shopId) == 1) {
            throw new AppException(ErrorCode.OBJECT_SETUP, "Bạn đã báo cáo. Đang trong quá trình xử lý");
        }

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

    private AccountReport createReport(Request_ReportShop_DTO reportDTO) {
        AccountReport newReport = new AccountReport();
        Account account = accountRepository.findById(reportDTO.getAccountId()).get();
        Account shop = accountRepository.findById(reportDTO.getShopId()).get();
        newReport.setAccount(account);
        newReport.setShop(shop);
        newReport.setContent(reportDTO.getContent());
        newReport.setCreateAt(reportDTO.getCreateAt());
        newReport.setStatus(reportDTO.getStatus());
        newReport.setTitle(reportDTO.getTitle());
        reportRepository.save(newReport);

        if (reportDTO.getImages() != null) {
            List<ImageAccountReport> imageEvalues = saveImages(reportDTO.getImages(), newReport);
            newReport.setImageAccountReports(imageEvalues);
            reportRepository.save(newReport);
        }

        return newReport;
    }

    private List<ImageAccountReport> saveImages(MultipartFile[] imageFiles, AccountReport report) {
        List<ImageAccountReport> imagesAccountReport = new ArrayList<>();

        for (MultipartFile imageFile : imageFiles) {
            if (!imageFile.isEmpty()) {
                try {
                    String imageFirebaseURL = firebaseUploadImages.uploadFile("report",
                            imageFile);
                    if (imageFirebaseURL == null || imageFirebaseURL.isEmpty()) {
                        throw new AppException(ErrorCode.OBJECT_SETUP,
                                "Failed to upload image to Firebase: " + imageFile.getOriginalFilename());
                    }

                    ImageAccountReport imageReport = new ImageAccountReport();
                    imageReport.setSrc(imageFirebaseURL);
                    imageReport.setAccountReport(report);
                    imageAccountReport.saveAndFlush(imageReport);

                    imagesAccountReport.add(imageReport);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AppException(ErrorCode.OBJECT_SETUP,
                            "Error processing image: " + imageFile.getOriginalFilename(), e);
                }
            }
        }

        return imagesAccountReport;
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

    public Map<String, Object> selectTop3ProductHomeShop(List<FlashSaleDetail> flashSaleDetails, Integer id_Shop) {
        // Pageable pageable = PageRequest.of(0, 3); // Chỉ lấy 3 sản phẩm đầu tiên
        List<Product> pageProducts = productRepo.findTop3ProductsByShopId(id_Shop);
        List<Response_Product> response_Products = new ArrayList<Response_Product>();
        for (Product product : pageProducts) {
            if (product.getAccount().getId() == id_Shop) {
                response_Products.add(productMaper.productToResponse_Product(product));
            }
        }
        Map<String, Object> response = new HashMap<String, Object>();
        if (response_Products.size() > 0) {
            response.put("datas", response_Products);
            // response.put("totalPages", pageProducts.getTotalPages() *
            // response_Products.size());
        } else {
            response.put("error", 1002);
        }
        return response;
    }
}
