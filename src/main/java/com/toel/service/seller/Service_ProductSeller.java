package com.toel.service.seller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.multipart.MultipartFile;

import com.toel.dto.seller.request.Product.Request_ProductCreate;
import com.toel.dto.seller.request.Product.Request_ProductUpdate;
import com.toel.dto.seller.response.Response_Product;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.ProductMapper;
import com.toel.model.Product;
import com.toel.repository.AccountRepository;
import com.toel.repository.CategoryRepository;
import com.toel.repository.ProductRepository;

@Service
public class Service_ProductSeller {
        @Autowired
        ProductMapper productMapper;
        @Autowired
        ProductRepository productRepository;
        @Autowired
        Service_ImageProductSeller service_ImageProductSeller;
        @Autowired
        AccountRepository accountRepository;
        @Autowired
        CategoryRepository categoryRepository;

        public PageImpl<Response_Product> getAll(
                        Integer page, Integer size, boolean sortBy, String sortColum, Integer account_id,
                        String search) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColum));
                Page<Product> pageProduct = productRepository.findByAccountId(account_id, search, pageable);
                List<Response_Product> list = pageProduct.hasContent()
                                ? pageProduct.stream()
                                                .map(product -> productMapper.response_Product(product))
                                                .collect(Collectors.toList())
                                : new ArrayList<>();
                return new PageImpl<>(list, pageable, pageProduct.getTotalElements());
        }

        public Response_Product create(
                        Request_ProductCreate request_Product) throws IOException {
                Product product = productMapper.productCreate(request_Product);
                product.setAccount(accountRepository.findById(request_Product.getAccount())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account")));
                product.setCategory(
                                categoryRepository.findById(request_Product.getCategory())
                                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
                                                                "Category")));
                product.setCreateAt(new Date());
                return productMapper.response_Product(productRepository.saveAndFlush(product));
        }

        public Response_Product update(
                        Request_ProductUpdate request_Product) throws IOException {
                Product product = productMapper.productUpdate(request_Product);
                product.setAccount(accountRepository.findById(request_Product.getAccount())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account")));
                product.setCategory(categoryRepository.findById(request_Product.getCategory())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Category")));
                product.setCreateAt(product.getCreateAt());
                return productMapper.response_Product(productRepository.saveAndFlush(product));
        }

        public boolean saveImgCreate(
                        Integer product_id,
                        List<MultipartFile> images) throws IOException {
                return service_ImageProductSeller.createProductImages(
                                productRepository.findById(product_id).orElseThrow(
                                                () -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Product")),
                                images);
        }

        public boolean saveImgUpdate(
                        Integer product_id,
                        List<MultipartFile> images) throws IOException {
                return service_ImageProductSeller.updateProductImages(
                                productRepository.findById(product_id).orElseThrow(
                                                () -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Product")),
                                images);
        }

        public Response_Product edit(
                        Integer product_id) {
                return productMapper.response_Product(productRepository.findById(product_id)
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Product")));
        }

        public void delete(
                        Integer product_id) {
                productRepository.findById(product_id).ifPresent(
                                product -> {
                                        product.setDelete(true);
                                        productRepository.saveAndFlush(product);
                                });
        }

        public Request_ProductCreate checkCreate(Request_ProductCreate request_ProductCreate) {

                if (productRepository.findByAccountId(request_ProductCreate.getAccount()).stream()
                                .anyMatch(productCheck -> request_ProductCreate.getName()
                                                .equalsIgnoreCase(productCheck.getName()))) {
                        throw new AppException(ErrorCode.OBJECT_SETUP, "Tên sản phẩm đã tồn tại.");
                }

                if (request_ProductCreate.getPrice() < 1000
                                || request_ProductCreate.getPrice() < request_ProductCreate.getSale()) {
                        String message = (request_ProductCreate.getPrice() < 1000) ? "Giá sản phẩm phải lớn hơn 1000đ."
                                        : "Giá sản phẩm phải lớn hơn giá giảm.";
                        throw new AppException(ErrorCode.OBJECT_SETUP, message);
                }
                return request_ProductCreate;
        }

        public Request_ProductUpdate checkUpdate(Request_ProductUpdate request_ProductUpdate) {

                if (productRepository.findByAccountId(request_ProductUpdate.getAccount()).stream()
                                .anyMatch(productCheck -> !request_ProductUpdate.getId().equals(productCheck.getId())
                                                && request_ProductUpdate.getName()
                                                                .equalsIgnoreCase(productCheck.getName()))) {
                        throw new AppException(ErrorCode.OBJECT_SETUP, "Tên sản phẩm đã tồn tại.");
                }

                if (request_ProductUpdate.getPrice() < 1000
                                || request_ProductUpdate.getPrice() < request_ProductUpdate.getSale()) {
                        String message = (request_ProductUpdate.getPrice() < 1000) ? "Giá sản phẩm phải lớn hơn 1000đ."
                                        : "Giá sản phẩm phải lớn hơn giá giảm.";
                        throw new AppException(ErrorCode.OBJECT_SETUP, message);
                }
                return request_ProductUpdate;
        }
}
