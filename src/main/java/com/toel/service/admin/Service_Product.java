package com.toel.service.admin;

import java.util.Collections;
import java.util.Comparator;
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
import com.toel.model.AccountReport;
import com.toel.model.Product;
import com.toel.model.ProductReport;
import com.toel.repository.AccountReportRepository;
import com.toel.repository.ProductReportRepository;
import com.toel.repository.ProductRepository;
import com.toel.service.Email.EmailService;
import com.toel.service.Email.EmailTemplateType;
import com.toel.util.log.LogUtil;

@Service
public class Service_Product {
    @Autowired
    ProductReportRepository productReportRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    EmailService emailService;
    @Autowired
    LogUtil service_Log;

    public PageImpl<Response_ProductListFlashSale> getAll(int page, int size, Boolean sortBy, String column,
            String key, String option) {
        Page<Product> pageProduct;
        Pageable pageable;
        Double priceOrSale = parseStringToDouble(key);

        if ("sumbaocao".equalsIgnoreCase(column)) {
            pageable = PageRequest.of(0, 100000);
            pageProduct = pageProduct(option, pageable, priceOrSale, key, column);

        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, column));
            pageProduct = pageProduct(option, pageable, priceOrSale, key, column);

        }

        List<Response_ProductListFlashSale> list = pageProduct.stream()
                .map(product -> {
                    Response_ProductListFlashSale response = productMapper.tProductListFlashSale(product);
                    response.setSumBaoCao(product.getProductReports().size());
                    return response;
                })
                .collect(Collectors.toList());

        if ("sumbaocao".equalsIgnoreCase(column)) {
            list = list.stream()
                    .sorted(Comparator.comparing(Response_ProductListFlashSale::getSumBaoCao,
                            sortBy ? Comparator.reverseOrder() : Comparator.naturalOrder()))
                    .collect(Collectors.toList());
            pageable = PageRequest.of(page, size);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), list.size());
            if (start >= list.size()) {
                return new PageImpl<>(Collections.emptyList(), pageable, list.size());
            }
            List<Response_ProductListFlashSale> paginatedList = list.subList(start, end);
            return new PageImpl<>(paginatedList, pageable, list.size());
        } else {
            return new PageImpl<>(list, pageable, pageProduct.getTotalElements());
        }
    }

    private Page<Product> pageProduct(String option, Pageable pageable, Double priceOrSale, String key, String column) {
        Page<Product> pageProduct;
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
        return pageProduct;
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

    public Response_ProductListFlashSale updateStatus(int id, String contents, Integer accountID) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Product"));
        String active_Type;
        if (!entity.isDelete()) {
            emailService.push(entity.getAccount().getEmail(), "TOEL - Thông Báo Khóa Sản Phẩm",
                    EmailTemplateType.KHOATAIKHOAN, entity.getAccount().getFullname(), contents, "Sản phẩm");
            active_Type = "Mở khóa sản phẩm";
        } else {
            emailService.push(entity.getAccount().getEmail(), "TOEL - Thông Báo Mở Sản Phẩm",
                    EmailTemplateType.MOTAIKHOAN, entity.getAccount().getFullname(), contents, "Sản phẩm");
            active_Type = "Khóa sản phẩm";
        }
        entity.setDelete(!entity.isDelete());
        Product productNew = productRepository.save(entity);
        service_Log.setLog(getClass(), accountID, "INFO", "Product", productMapper.tProductListFlashSale(productNew),
                null,
                active_Type);
        return productMapper.tProductListFlashSale(productNew);
    }

    public Response_ProductListFlashSale updateActive(int id, Boolean status, String contents, Integer accountID) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Product"));
        String active_Type;
        if (status) {
            emailService.push(entity.getAccount().getEmail(), "TOEL - Thông Báo Duyệt Sản Phẩm",
                    EmailTemplateType.DUYET, entity.getAccount().getFullname(),
                    (contents == null || contents.isEmpty()) ? "Sản phẩm thỏa mãn các tiêu chí của sàn. " : contents,
                    "Sản phẩm", entity.getId().toString(), entity.getName(), entity.getCategory().getName());
            active_Type = "Duyệt sản phẩm";
            entity.setActive(true);
        } else {
            emailService.push(entity.getAccount().getEmail(), "TOEL - Thông Báo Hủy Sản Phẩm",
                    EmailTemplateType.HUYDUYET, entity.getAccount().getFullname(),
                    (contents == null || contents.isEmpty()) ? "Sản phẩm vi phạm chính sách sàn" : contents,
                    "Sản phẩm", entity.getId().toString(), entity.getName(), entity.getCategory().getName());
            entity.setActive(false);
            entity.setDelete(true);
            active_Type = "Không duyệt sản phẩm";
        }
        Product productNew = productRepository.save(entity);
        service_Log.setLog(getClass(), accountID, "INFO", "Product", productMapper.tProductListFlashSale(productNew),
                null,
                active_Type);
        return productMapper.tProductListFlashSale(productNew);
    }

    private Double parseStringToDouble(String value) {
        try {
            return (value != null && !value.isBlank()) ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer getCountProductByAccountId(Integer accountId) {
        return productRepository.countProductByAccountId(accountId);
    }

    // show Report sản phẩm
    public List<ProductReport> getReportsByAccountId(int accountId) {
        return productReportRepository.findByAccountId(accountId);
    }
}
