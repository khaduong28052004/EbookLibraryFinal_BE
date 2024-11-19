package com.toel.service.admin;

import java.util.stream.Collectors;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.request.FlashSaleDetail.Resquest_FlashSaleDetailsCreate;
import com.toel.dto.admin.request.FlashSaleDetail.Resquest_FlashSaleDetailsUpdate;
import com.toel.dto.admin.response.Response_FlashSaleDetail;
import com.toel.dto.seller.response.Response_Product;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.FlashSaleDetailsMapper;
import com.toel.mapper.ProductMapper;
import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;
import com.toel.model.Product;
import com.toel.repository.FlashSaleDetailRepository;
import com.toel.repository.FlashSaleRepository;
import com.toel.repository.ProductRepository;

@Service
public class Service_FlashSaleDetail {
    @Autowired
    FlashSaleDetailsMapper flashSaleDetailsMapper;
    @Autowired
    FlashSaleDetailRepository flashSaleDetailRepository;
    @Autowired
    FlashSaleRepository flashSaleRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;

    public PageImpl<Response_Product> getAll(int page, int size, Boolean sortBy, String column, Boolean status, Integer idFlashSale) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, column));
        Page<Product> pageItems;
        if (idFlashSale != null) {
            flashSaleRepository.findById(idFlashSale)
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Flash sale"));
        }
        if (Boolean.TRUE.equals(status)) {
            pageItems = productRepository.selectAllProductInFlashSale(idFlashSale, pageable);
        } else {
            pageItems = productRepository.selectAllProductNotInFlashSale(idFlashSale, pageable);
        }
        List<Response_Product> list = pageItems.stream()
                .map(item -> productMapper.response_Product(item))
                .collect(Collectors.toList());

        return new PageImpl<>(list, pageable, pageItems.getTotalElements());
    }

    public Response_FlashSaleDetail create(Resquest_FlashSaleDetailsCreate entity) {
        FlashSale flashSale = flashSaleRepository.findById(entity.getFlashSale())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Flash sale"));
        Product product = productRepository.findById(entity.getProduct())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Product"));
        FlashSaleDetail flashSaleDetail = flashSaleDetailsMapper.toFlashSaleDetailCreate(entity);
        flashSaleDetail.setFlashSale(flashSale);
        flashSaleDetail.setProduct(product);
        return flashSaleDetailsMapper.toFlashSaleDetail(flashSaleDetailRepository.saveAndFlush(flashSaleDetail));
    }

    public Response_FlashSaleDetail update(Resquest_FlashSaleDetailsUpdate entity) {
        FlashSale flashSale = flashSaleRepository.findById(entity.getFlashSale())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Flash sale"));
        Product product = productRepository.findById(entity.getProduct())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Product"));
        FlashSaleDetail flashSaleDetail = flashSaleDetailRepository.findById(entity.getId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "FlashSaleDetail"));
        flashSaleDetailsMapper.toFlashSaleDetailUpdate(flashSaleDetail, entity);
        flashSaleDetail.setFlashSale(flashSale);
        flashSaleDetail.setProduct(product);
        return flashSaleDetailsMapper.toFlashSaleDetail(flashSaleDetailRepository.saveAndFlush(flashSaleDetail));
    }

    public void delete(Integer id) {
        FlashSaleDetail entity = flashSaleDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Không tìm thấy FlashSaleDetail"));
        flashSaleDetailRepository.delete(entity);
    }
}
