package com.toel.service.seller;

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

import com.toel.dto.seller.request.Request_Product;
import com.toel.dto.seller.response.Response_Product;
import com.toel.mapper.seller.Seller_ProductMapper;
import com.toel.model.Product;
import com.toel.repository.ProductRepository;

@Service
public class Service_ProductSeller {
    @Autowired
    Seller_ProductMapper productMapper;
    @Autowired
    ProductRepository productRepository;

    public PageImpl<Response_Product> getAll(
            Integer page, Integer size, boolean sortBy, String sortColum, Integer account_id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC));
        Page<Product> pageProduct = productRepository.findByAccountId(account_id, pageable);
        List<Response_Product> list = pageProduct.stream()
                .map(product -> productMapper.response_Product(product))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageProduct.getTotalElements());
    }

    public Response_Product save(
            Request_Product request_Product) {
        return productMapper.response_Product(productRepository.saveAndFlush(productMapper.product(request_Product)));
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
