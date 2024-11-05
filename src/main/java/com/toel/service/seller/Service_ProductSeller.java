package com.toel.service.seller;

import java.io.IOException;
import java.util.ArrayList;
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

import com.toel.dto.seller.request.Product.Request_ProductCreate;
import com.toel.dto.seller.request.Product.Request_ProductUpdate;
import com.toel.dto.seller.response.Response_Product;
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
            Integer page, Integer size, boolean sortBy, String sortColum, Integer account_id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColum));
        Page<Product> pageProduct = productRepository.findByAccountId(account_id, pageable);
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
        product.setAccount(accountRepository.findById(request_Product.getAccount()).get());
        product.setCategory(categoryRepository.findById(request_Product.getCategory()).get());
        service_ImageProductSeller.createProductImages(product, request_Product.getImageProducts());
        return productMapper.response_Product(productRepository.saveAndFlush(product));
    }

    public Response_Product update(
            Request_ProductUpdate request_Product) throws IOException {
        Product product = productMapper.productUpdate(request_Product);
        product.setAccount(accountRepository.findById(request_Product.getAccount()).get());
        product.setCategory(categoryRepository.findById(request_Product.getCategory()).get());
        service_ImageProductSeller.updateProductImages(product, request_Product.getImageProducts());
        return productMapper.response_Product(productRepository.saveAndFlush(product));
    }

    public void delete(
            Integer product_id) {
        productRepository.findById(product_id).ifPresent(
                product -> {
                    product.setDelete(false);
                    productRepository.saveAndFlush(product);
                });
    }
}
