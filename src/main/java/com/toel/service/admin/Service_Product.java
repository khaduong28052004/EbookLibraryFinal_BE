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

import com.toel.dto.admin.response.Response_Account;
import com.toel.dto.admin.response.Response_ProductListFlashSale;
import com.toel.dto.seller.response.Response_Product;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.ProductMapper;
import com.toel.model.Account;
import com.toel.model.Product;
import com.toel.repository.ProductRepository;

@Service
public class Service_Product {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;

    public PageImpl<Response_Product> getAll(int page, int size, Boolean sortBy, String column,String key ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, column));
        Page<Product> pageProduct;
        if (key.isBlank()) {
            pageProduct = productRepository.findAllByIsDeleteAndIsActive(false,true, pageable);
        } else {
            pageProduct = productRepository.findAllByIsDelete(false, pageable);
        }
        List<Response_Product> list = pageProduct.stream()
                .map(Product -> productMapper.response_Product(Product))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageProduct.getTotalElements());
    }

    public Response_ProductListFlashSale getId(Integer id) {
        Product Product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Product"));
        return productMapper.tProductListFlashSale(Product);
    }

    public Response_Product updateActive(int id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Product"));
        entity.setActive(!entity.isActive());
        return productMapper.response_Product(productRepository.save(entity));
    }

}
