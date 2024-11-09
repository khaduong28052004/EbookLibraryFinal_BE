package com.toel.service.admin.Thongke;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.response.ThongKe.Response_TK_Product;
import com.toel.mapper.ProductMapper;
import com.toel.model.Product;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.LikeRepository;
import com.toel.repository.ProductRepository;

@Service
public class Service_ThongKe_Product {
        @Autowired
        ProductRepository productRepository;
        @Autowired
        ProductMapper productMapper;
        @Autowired
        BillDetailRepository billDetailRepository;
        @Autowired
        LikeRepository likeRepository;
        @Autowired
        EvalueRepository evalueRepository;

        public PageImpl<Response_TK_Product> get_TKDT_Product(Date dateStart, Date dateEnd, String option,
                        String search, Integer page, Integer size, Boolean sortBy, String sortColumn) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Sort.Direction.DESC : Sort.Direction.ASC, sortColumn));
                Calendar calStart = Calendar.getInstance();
                calStart.set(Calendar.DAY_OF_MONTH, 1);
                Date finalDateStart = (dateStart == null) ? calStart.getTime() : dateStart;
                Calendar calEnd = Calendar.getInstance();
                calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date finalDateEnd = (dateEnd == null) ? calEnd.getTime() : dateEnd;
                // System.out.println("Ngày bắt đầu: " + finalDateStart);
                // System.out.println("Ngày Kết thúc: " + finalDateEnd);
                Page<Product> pageProduct;
                if (option.equalsIgnoreCase("bill")) {
                        pageProduct = (search == null || search.isBlank())
                                        ? billDetailRepository.selectAll(finalDateStart, finalDateEnd, pageable)
                                        : billDetailRepository.selectAllByFinishAt(search, finalDateStart, finalDateEnd,
                                                        pageable);
                } else if (option.equalsIgnoreCase("danhgia")) {
                        pageProduct = (search == null || search.isBlank())
                                        ? evalueRepository.sellectAll(finalDateStart, finalDateEnd, pageable)
                                        : evalueRepository.sellectAllByCreateAt(search, finalDateStart, finalDateEnd,
                                                        pageable);
                } else if (option.equalsIgnoreCase("yeuthich")) {
                        pageProduct = (search == null || search.isBlank())
                                        ? evalueRepository.sellectAll(finalDateStart, finalDateEnd, pageable)
                                        : evalueRepository.sellectAllByCreateAt(search, finalDateStart, finalDateEnd,
                                                        pageable);
                } else {
                        pageProduct = (search == null || search.isBlank())
                                        ? productRepository.findAllByIsDeleteAndIsActiveAndCreateAtBetween(false, true,
                                                        finalDateStart, finalDateEnd, pageable)
                                        : productRepository.selectAllMatchingAttributesByDateStartAndDateEnd(search,
                                                        finalDateStart, finalDateEnd, pageable);

                }
                List<Response_TK_Product> list = pageProduct.stream()
                                .map(product -> {
                                        Response_TK_Product response_TK_Product = productMapper
                                                        .toResponse_TK_Product(product);
                                        response_TK_Product.setSumBill(
                                                        billDetailRepository.calculateByFinishAtAndProduct(
                                                                        finalDateStart, finalDateEnd, product));
                                        response_TK_Product.setAvgStar(evalueRepository
                                                        .calculateAverageStarByProduct(product.getId()));
                                        response_TK_Product.setSumEvalue(
                                                        evalueRepository.findAllByProduct(product).size());
                                        response_TK_Product.setSumLike(likeRepository.countByProduct(product));
                                        return response_TK_Product;
                                })
                                .collect(Collectors.toList());
                return new PageImpl<>(list, pageable, pageProduct.getTotalElements());
        }

}