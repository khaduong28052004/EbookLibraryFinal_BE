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

import com.toel.dto.admin.response.ThongKe.Page_TK_Product;
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

        public Page_TK_Product get_TKDT_Product(Date dateStart, Date dateEnd, String option,
                        String search, Integer page, Integer size, Boolean sortBy, String sortColumn) {

                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Sort.Direction.DESC : Sort.Direction.ASC, sortColumn));
                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);

                List<Product> allProducts;
                Page<Product> pageProduct;

                if (option.equalsIgnoreCase("bill")) {
                        allProducts = (search == null || search.isBlank())
                                        ? billDetailRepository.selectAll(finalDateStart, finalDateEnd)
                                        : billDetailRepository.selectAllByFinishAt(search, finalDateStart,
                                                        finalDateEnd);
                } else if (option.equalsIgnoreCase("danhgia")) {
                        allProducts = (search == null || search.isBlank())
                                        ? evalueRepository.sellectAll(finalDateStart, finalDateEnd)
                                        : evalueRepository.sellectAllByCreateAt(search, finalDateStart, finalDateEnd);
                } else if (option.equalsIgnoreCase("yeuthich")) {
                        allProducts = (search == null || search.isBlank())
                                        ? likeRepository.selectAllProduct(finalDateStart, finalDateEnd)
                                        : likeRepository.selectAllProductByDateStartDateEnd(search, finalDateStart,
                                                        finalDateEnd);
                } else {
                        allProducts = (search == null || search.isBlank())
                                        ? productRepository.findAllByIsDeleteAndIsActiveAndCreateAtBetween(false, true,
                                                        finalDateStart, finalDateEnd)
                                        : productRepository.selectAllMatchingAttributesByDateStartAndDateEnd(search,
                                                        finalDateStart, finalDateEnd);
                }

                Integer totalBills = allProducts.stream()
                                .mapToInt(product -> calculateProductRevenue(product, finalDateStart, finalDateEnd)
                                                .getSumBill())
                                .sum();
                Integer totalLikes = allProducts.stream()
                                .mapToInt(product -> calculateProductRevenue(product, finalDateStart, finalDateEnd)
                                                .getSumLike())
                                .sum();
                Integer totalEvalues = allProducts.stream()
                                .mapToInt(product -> calculateProductRevenue(product, finalDateStart, finalDateEnd)
                                                .getSumEvalue())
                                .sum();

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
                                        ? likeRepository.selectAllProduct(finalDateStart, finalDateEnd, pageable)
                                        : likeRepository.selectAllProductByDateStartDateEnd(search, finalDateStart,
                                                        finalDateEnd,
                                                        pageable);
                } else {
                        pageProduct = (search == null || search.isBlank())
                                        ? productRepository.findAllByIsDeleteAndIsActiveAndCreateAtBetween(false, true,
                                                        finalDateStart, finalDateEnd, pageable)
                                        : productRepository.selectAllMatchingAttributesByDateStartAndDateEnd(search,
                                                        finalDateStart, finalDateEnd, pageable);
                }

                List<Response_TK_Product> list = pageProduct.stream()
                                .map(product -> calculateProductRevenue(product, finalDateStart, finalDateEnd))
                                .collect(Collectors.toList());

                Integer totalProducts = allProducts.size();

                Page_TK_Product pageTKProduct = Page_TK_Product.builder()
                                .tongSP(totalProducts)
                                .tongLike(totalLikes)
                                .tongBill(totalBills)
                                .tongEvalue(totalEvalues)
                                .thongke(new PageImpl<>(list, pageable, pageProduct.getTotalElements()))
                                .build();

                return pageTKProduct;
        }

        private Response_TK_Product calculateProductRevenue(Product product, Date startDate, Date endDate) {
                Response_TK_Product response = productMapper
                                .toResponse_TK_Product(product);
                Integer tongBill = billDetailRepository.calculateByFinishAtAndProduct(
                                startDate, endDate, product);
                double avgStar = evalueRepository
                                .calculateAverageStarByProduct(product.getId());
                Integer tongEvalue = evalueRepository.findAllByProduct(product).size();
                Integer tongLike = likeRepository.countByProduct(product);

                response.setSumBill(tongBill);
                response.setAvgStar(avgStar);
                response.setSumEvalue(tongEvalue);
                response.setSumLike(tongLike);
                return response;
        }

        public Date getDateStart(Date dateStart) {
                Calendar calStart = Calendar.getInstance();
                calStart.set(Calendar.DAY_OF_MONTH, 1);
                Date finalDateStart = (dateStart == null) ? calStart.getTime() : dateStart;
                return finalDateStart;
        }

        public Date getDateEnd(Date dateEnd) {
                Calendar calEnd = Calendar.getInstance();
                calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date finalDateEnd = (dateEnd == null) ? calEnd.getTime() : dateEnd;
                return finalDateEnd;
        }
}
