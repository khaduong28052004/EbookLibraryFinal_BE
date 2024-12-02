package com.toel.service.admin.Thongke;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.response.Response_SearchAudio;
import com.toel.dto.admin.response.ThongKe.Page_TK_Product;
import com.toel.dto.admin.response.ThongKe.Response_TK_Product;
import com.toel.dto.user.response.Response_Product;
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
                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);

                List<Product> allProducts;

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

                List<Response_TK_Product> list = allProducts.stream()
                                .map(product -> calculateProductRevenue(product, finalDateStart, finalDateEnd))
                                .collect(Collectors.toList());

                Integer totalProducts = allProducts.size();
                Comparator<Response_TK_Product> comparator = getComparator(sortColumn, sortBy);
                list.sort(comparator);
                Pageable pageable = PageRequest.of(page, size);
                int start = (int) pageable.getOffset();
                int end = Math.min(start + pageable.getPageSize(), allProducts.size());
                List<Response_TK_Product> paginatedList = list.subList(start, end);
                Page_TK_Product pageTKProduct = Page_TK_Product.builder()
                                .tongSP(totalProducts)
                                .tongLike(totalLikes)
                                .tongBill(totalBills)
                                .tongEvalue(totalEvalues)
                                .thongke(new PageImpl<>(paginatedList, pageable, list.size()))
                                .build();

                return pageTKProduct;
        }

        public PageImpl<Response_SearchAudio> get_Search_Product(Date dateStart, Date dateEnd, String option,
                        String search, Integer page, Integer size, Boolean sortBy, String sortColumn) {
                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);
                List<Product> allProducts;
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

                List<Response_SearchAudio> list = allProducts.stream()
                                .map(product -> calculateProductRevenueSearch(option, product, finalDateStart,
                                                finalDateEnd))
                                .collect(Collectors.toList());

                Comparator<Response_SearchAudio> comparator = getComparatorSearch(sortColumn, sortBy);
                list.sort(comparator);
                Pageable pageable = PageRequest.of(page, size);
                int start = (int) pageable.getOffset();
                int end = Math.min(start + pageable.getPageSize(), allProducts.size());
                List<Response_SearchAudio> paginatedList = list.subList(start, end);
                return new PageImpl<>(paginatedList, pageable, list.size());
        }

        private Comparator<Response_TK_Product> getComparator(String sortColumn, Boolean sortBy) {
                Comparator<Response_TK_Product> comparator;
                switch (sortColumn.toLowerCase()) {
                        case "id":
                                comparator = Comparator.comparing(Response_TK_Product::getId);
                                break;
                        case "name":
                                comparator = Comparator.comparing(Response_TK_Product::getName);
                                break;
                        case "price":
                                comparator = Comparator.comparing(Response_TK_Product::getPrice);
                                break;
                        case "sumbill":
                                comparator = Comparator.comparing(Response_TK_Product::getSumBill);
                                break;
                        case "sumevalue":
                                comparator = Comparator.comparing(Response_TK_Product::getSumEvalue);
                                break;
                        case "sumlike":
                                comparator = Comparator.comparing(Response_TK_Product::getSumLike);
                                break;
                        case "avgstar":
                                comparator = Comparator.comparing(Response_TK_Product::getAvgStar);
                                break;
                        case "createat":
                                comparator = Comparator.comparing(Response_TK_Product::getCreateAt);
                                break;
                        default:
                                throw new IllegalArgumentException("Invalid sort column: " + sortColumn);
                }
                return sortBy ? comparator.reversed() : comparator;
        }

        private Comparator<Response_SearchAudio> getComparatorSearch(String sortColumn, Boolean sortBy) {
                Comparator<Response_SearchAudio> comparator;
                switch (sortColumn.toLowerCase()) {
                        case "id":
                                comparator = Comparator.comparing(Response_SearchAudio::getId);
                                break;
                        case "name":
                                comparator = Comparator.comparing(Response_SearchAudio::getName);
                                break;
                        case "price":
                                comparator = Comparator.comparing(Response_SearchAudio::getPrice);
                                break;
                        case "sumbill":
                                comparator = Comparator.comparing(Response_SearchAudio::getSumBill);
                                break;
                        case "sumevalue":
                                comparator = Comparator.comparing(Response_SearchAudio::getSumEvalue);
                                break;
                        case "sumlike":
                                comparator = Comparator.comparing(Response_SearchAudio::getSumLike);
                                break;
                        case "avgstar":
                                comparator = Comparator.comparing(Response_SearchAudio::getAvgStar);
                                break;
                        case "createat":
                                comparator = Comparator.comparing(Response_SearchAudio::getCreateAt);
                                break;
                        default:
                                throw new IllegalArgumentException("Invalid sort column: " + sortColumn);
                }
                return sortBy ? comparator.reversed() : comparator;
        }

        private Response_TK_Product calculateProductRevenue(Product product, Date startDate, Date endDate) {
                Response_TK_Product response = productMapper
                                .toResponse_TK_Product(product);
                Integer tongBill = billDetailRepository.calculateByFinishAtAndProduct(
                                startDate, endDate, product);
                double avgStar = evalueRepository
                                .calculateAverageStarByProduct(product.getId(), startDate, endDate);
                Integer tongEvalue = evalueRepository.findAllByProductAndCreateAt(product, startDate, endDate).size();
                Integer tongLike = likeRepository.countByProductAndCreateAt(product, startDate, endDate);

                response.setSumBill(tongBill);
                response.setAvgStar(avgStar);
                response.setSumEvalue(tongEvalue);
                response.setSumLike(tongLike);
                return response;
        }

        private Response_SearchAudio calculateProductRevenueSearch(String option, Product product, Date startDate,
                        Date endDate) {
                Response_SearchAudio response = productMapper
                                .toResponse_TK_ProductSearchAudio(product);
                Integer tongBill = 0;
                double avgStar = 0.0;
                Integer tongEvalue = 0;
                Integer tongLike = 0;

                if (option.equalsIgnoreCase("bill")) {
                        tongBill = billDetailRepository.calculateByFinishAtAndProduct(
                                        startDate, endDate, product);
                } else {
                        tongBill = billDetailRepository.findByProduct(product).size();
                }
                if (option.equalsIgnoreCase("danhgia")) {
                        avgStar = evalueRepository
                                        .calculateAverageStarByProduct(product.getId(), startDate, endDate);
                } else {
                        avgStar = evalueRepository.calculateAverageStarByProduct(product.getId());
                }
                if (option.equalsIgnoreCase("danhgia")) {
                        tongEvalue = evalueRepository.findAllByProductAndCreateAt(product, startDate, endDate).size();
                } else {
                        tongEvalue = evalueRepository.findAllByProduct(product).size();
                }
                if (option.equalsIgnoreCase("yeuthich")) {
                        tongLike = likeRepository.countByProductAndCreateAt(product, startDate, endDate);
                } else {
                        tongLike = likeRepository.findAllByProduct(product).size();
                }
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
