package com.toel.service.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.response.Response_ProductListFlashSale;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.ProductMapper;
import com.toel.model.Product;
import com.toel.repository.ProductRepository;
import com.toel.service.Email.EmailService;
import com.toel.service.Email.EmailTemplateType;

@Service
public class Service_Product {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    EmailService emailService;

    public PageImpl<Response_ProductListFlashSale> getAll(int page, int size, Boolean sortBy, String column,
            String key, String option) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, column));
        Page<Product> pageProduct;
        Double priceOrSale = parseStringToDouble(key);
        if (option.equalsIgnoreCase("choduyet")) {
            if (key.isBlank()) {
                pageProduct = productRepository.findAllByIsDeleteAndIsActive(false, false, pageable);
            } else if (priceOrSale != null) {
                pageProduct = productRepository.selectAllByActiveAndDeleteAndMatchingAttributes(null, priceOrSale,
                        pageable);
            } else {
                pageProduct = productRepository.selectAllByActiveAndDeleteAndMatchingAttributes(key, null,
                        pageable);
            }
        } else if (option.equalsIgnoreCase("daduyet")) {
            if (key.isBlank() || key == null) {
                pageProduct = productRepository.findByIsActive(true, pageable);
            } else if (priceOrSale != null) {
                pageProduct = productRepository.selectAllByActiveAndMatchingKey(null, priceOrSale, pageable);
            } else {
                pageProduct = productRepository.selectAllByActiveAndMatchingKey(key, null,
                        pageable);
            }
        } else {
            if (key.isBlank() || key == null) {
                pageProduct = productRepository.findAllByIsActiveNotAndIsDeleteNot(false, true, pageable);
            } else if (priceOrSale != null) {
                pageProduct = productRepository.selectAllMatchingKey(null, priceOrSale, pageable);
            } else {
                pageProduct = productRepository.selectAllMatchingKey(key, null,
                        pageable);
            }
        }
        List<Response_ProductListFlashSale> list = pageProduct.stream()
                .map(Product -> productMapper.tProductListFlashSale(Product))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageProduct.getTotalElements());
    }

    public PageImpl<Response_ProductListFlashSale> getAllBrowse(int page, int size, Boolean sortBy, String column,
            String key) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, column));
        Page<Product> pageProduct;
        Double priceOrSale = parseStringToDouble(key);

        if (key.isBlank()) {
            pageProduct = productRepository.findAllByIsDeleteAndIsActive(false, false, pageable);
        } else if (priceOrSale != null) {
            pageProduct = productRepository.selectAllByActiveAndDeleteAndMatchingAttributes(null, priceOrSale,
                    pageable);
        } else {
            pageProduct = productRepository.selectAllByActiveAndDeleteAndMatchingAttributes(key, null,
                    pageable);
        }
        List<Response_ProductListFlashSale> list = pageProduct.stream()
                .map(Product -> productMapper.tProductListFlashSale(Product))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageProduct.getTotalElements());
    }

    // public Response_ProductListFlashSale getId(Integer id) {
    // Product Product = productRepository.findById(id)
    // .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Product"));
    // return productMapper.tProductListFlashSale(Product);
    // }

    public Response_ProductListFlashSale updateStatus(int id, String contents) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Product"));
        if (!entity.isDelete()) {
            emailService.push(entity.getAccount().getEmail(), "TOEL - Thông Báo Khóa Sản Phẩm",
                    EmailTemplateType.KHOATAIKHOAN, entity.getAccount().getFullname(), contents, "Sản phẩm");
        } else {
            emailService.push(entity.getAccount().getEmail(), "TOEL - Thông Báo Mở Sản Phẩm",
                    EmailTemplateType.MOTAIKHOAN, entity.getAccount().getFullname(), contents, "Sản phẩm");
        }
        entity.setDelete(!entity.isDelete());
        return productMapper.tProductListFlashSale(productRepository.save(entity));
    }

    public Response_ProductListFlashSale updateActive(int id, Boolean status, String contents) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Product"));
        if (status) {
            emailService.push(entity.getAccount().getEmail(), "TOEL - Thông Báo Duyệt Sản Phẩm",
                    EmailTemplateType.DUYET, entity.getAccount().getFullname(),
                    (contents == null || contents.isEmpty()) ? "Sản phẩm thỏa mãn các tiêu chí của sàn. " : contents,
                    "Sản phẩm", entity.getId().toString(), entity.getName(), entity.getCategory().getName());
            entity.setActive(true);
        } else {
            emailService.push(entity.getAccount().getEmail(), "TOEL - Thông Báo Hủy Sản Phẩm",
                    EmailTemplateType.HUYDUYET, entity.getAccount().getFullname(),
                    (contents == null || contents.isEmpty()) ? "Sản phẩm vi phạm chính sách sàn" : contents,
                    "Sản phẩm", entity.getId().toString(), entity.getName(), entity.getCategory().getName());
            entity.setActive(false);
            entity.setDelete(true);
        }
        return productMapper.tProductListFlashSale(productRepository.save(entity));
    }

    private Double parseStringToDouble(String value) {
        try {
            return (value != null && !value.isBlank()) ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
