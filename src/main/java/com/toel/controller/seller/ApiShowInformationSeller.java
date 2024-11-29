package com.toel.controller.seller;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.response.Response_InforSeller;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.model.Account;
import com.toel.model.Address;
import com.toel.model.Bill;
import com.toel.model.BillDetail;
import com.toel.model.Evalue;
import com.toel.model.OrderStatus;
import com.toel.model.Product;
import com.toel.model.TypeVoucher;
import com.toel.model.Voucher;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.OrderStatusRepository;
import com.toel.repository.ProductRepository;
import com.toel.repository.TypeVoucherRepository;
import com.toel.repository.VoucherRepository;
import com.toel.service.user.FollowerService;

import lombok.Data;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
public class ApiShowInformationSeller {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private EvalueRepository evalueRepository;

    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private TypeVoucherRepository typeVoucherRepository;
    @Autowired
    private FollowerService followerService;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    /**
     * homeShowSeller
     * số lượng sản phẩm
     * số lượng theo giỏi
     * Tỉ lệ Shop hủy đơn:
     * số lượn người theo giõi (%)
     * thời gian tham gia
     * 
     * @param idSeller
     * @return
     */
    @GetMapping("/api/user/informationSeller/{idSeller}")
    public ApiResponse<?> informationSellerPublic(@PathVariable Integer idSeller) {
        try {
            Account account = accountRepository.findById(idSeller)
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
                            "ID:[" + idSeller + "] không tìm thấy người bán"));
            Response_InforSeller inforSeller = new Response_InforSeller();
            inforSeller.setIdSeller(account.getId());
            inforSeller.setNumberOfProducts(account.getProducts().size());
            inforSeller.setNumberOfFollowers(account.getFollowers().size());
            inforSeller.setTrackingNumber(idSeller);
            // inforSeller.setAverageStarRating(averageStars(account));
            // inforSeller.setAverageStarRating();
            inforSeller.setShopCancellationRate(idSeller);
            inforSeller.setAvatar(account.getAvatar());
            inforSeller.setBackground(account.getBackground());
            inforSeller.setIsFollowed(false);
            // Kiểm tra địa chỉ có `status` là true
            Optional<Address> defaultAddress = account.getAddresses().stream()
                    .filter(Address::isStatus) // Kiểm tra địa chỉ có `status` là true
                    .findFirst();
            if (defaultAddress.isPresent()) {
                Address address = defaultAddress.get();
                inforSeller.setDistrict(address.getFullNameAddress());
            } else {
                inforSeller.setDistrict("chưa cập nhật!");
            }
            inforSeller.setCreateAtSeller(account.getCreateAtSeller());
            inforSeller.setParticipationTime(inforSeller.calculateActiveDays());
            inforSeller.setShopName(account.getShopName());
            Map<String, Object> map = new HashMap<>();
            map.put("shopDataEX", inforSeller);
            map.put("rating", ValueAverageStars(account));
            // map.put("isf", ValueAverageStars(account));

            return ApiResponse.<Map>build().code(100).message("null").result(map);
        } catch (Exception e) {
            // TODO: handle exception
            return ApiResponse.<Response_InforSeller>build().code(100).message(e.getMessage()).result(null);
        }

    }

    // @GetMapping("/api/user/voucherall/{idSeller}")
    // public ApiResponse<?> voucherAll(@PathVariable Integer idSeller) {
    // Account account = accountRepository.findById(idSeller)
    // .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
    // "ID:[" + idSeller + "] không tìm thấy người bán"));
    // List<Voucher> listVoucher = voucherRepository.findAllByAccount(account);
    // List<Voucher> listVoucher1 = voucherRepository.findAll();
    // Map<String, Object> hash = new HashMap<>();
    // hash.put("Voucher", listVoucher);
    // return ApiResponse.<Map>build().code(0).message(null).result(hash);
    // }

    @GetMapping("/api/user/voucherall/{idSeller}")
    public ApiResponse<?> voucherAll(@PathVariable Integer idSeller) {
        Account account = accountRepository.findById(idSeller)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
                        "ID:[" + idSeller + "] không tìm thấy người bán"));
        List<Voucher> listVoucher = voucherRepository.findAllByAccount(account);

        // Sắp xếp bằng Collections.sort
        Collections.sort(listVoucher, (v1, v2) -> {
            if (v1.getId() == null && v2.getId() == null)
                return 0;
            if (v1.getId() == null)
                return 1;
            if (v2.getId() == null)
                return -1;
            return v2.getId().compareTo(v1.getId()); // Giảm dần
        });

        Map<String, Object> hash = new HashMap<>();
        hash.put("Voucher", listVoucher);
        return ApiResponse.<Map>build().code(0).message(null).result(hash);
    }

    public Map<String, Object> ValueAverageStars(Account account) {
        List<Product> products = account.getProducts();
        List<BillDetail> listBillDetail = billDetailRepository.findByProductIn(products);
        List<Evalue> listEvalue = evalueRepository.findByBillDetailIn(listBillDetail);
        int totalStars = listEvalue.stream()
                .mapToInt(Evalue::getStar) // Lấy số sao từ mỗi Evalue
                .sum();
        double averageStars = listEvalue.isEmpty() ? 0 : (double) totalStars / listEvalue.size();
        Map<String, Object> result = new HashMap<>();
        result.put("averageStars", averageStars); // Trung bình sao
        result.put("totalReviews", listEvalue.size()); // Tổng số đánh giá
        result.put("totalStars", totalStars); // Tổng số sao (không bắt buộc)
        return result;
    }


    @GetMapping("/api/v1/user/voucherAll")
    public ApiResponse<?> getVoucherAll() {
        List<TypeVoucher> typeVoucher = typeVoucherRepository.findAll();
        Date date = new Date();
        List<Voucher> listVoucherShop = voucherRepository.findAllByTypeVoucher(typeVoucher.get(0), date);
        List<Voucher> listVoucherSan = voucherRepository.findAllByTypeVoucher(typeVoucher.get(1), date);
        Map<String, Object> hash = new HashMap<>();
        hash.put("san", listVoucherSan);
        hash.put("shop", listVoucherShop);

        return ApiResponse.<Map>build().code(0).message("").result(hash);
    }

    @GetMapping("/api/v1/user/topProducts")
    public ApiResponse<?> topProducts() {
        OrderStatus orderStatus = orderStatusRepository.findById(1).orElse(null);
        if (orderStatus == null) {
            return ApiResponse.<String>build().code(1).message("Đơn hoàn thành bằng 0").result(null);
        }
        List<Bill> listBill = billRepository.findByOrderStatus(orderStatus);
        if (listBill == null || listBill.isEmpty()) {
            // ne
            return ApiResponse.<String>build().code(1).message("không có hóa đơn nào!").result(null);
        }
        List<BillDetail> listBillDetails = billDetailRepository.findAllByBillIn(listBill);
        // Thống kê số lượng sản phẩm đã bán
        Map<Product, Long> productCountMap = listBillDetails.stream()
                .collect(
                        Collectors.groupingBy(BillDetail::getProduct, Collectors.summingLong(BillDetail::getQuantity)));

        // List<BillDetail> listBillDetails1 = billDetailRepository.
        List<Product> listProduct = productRepository.findByBillDetails(listBillDetails);
        Map<String, Object> hash = new HashMap<>();
        hash.put("list", listBillDetails);
        // Sắp xếp danh sách sản phẩm theo số lượng bán giảm dần và giới hạn top 10 sản
        // phẩm
        List<Map.Entry<Product, Long>> topProducts = productCountMap.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(10)
                .collect(Collectors.toList());

        // Chuẩn bị kết quả trả về
        List<Map<String, Object>> result = topProducts.stream()
                .map(entry -> {
                    Map<String, Object> productData = new HashMap<>();
                    productData.put("product", entry.getKey());
                    productData.put("quantity", entry.getValue());
                    return productData;
                })
                .collect(Collectors.toList());

        return ApiResponse.<List<Map<String, Object>>>build()
                .code(0)
                .message("Top sold products fetched successfully")
                .result(result);
    }

    // public double averageStars(Account account) {
    // // Lấy danh sách các sản phẩm của người bán
    // List<Product> products = account.getProducts();
    // List<BillDetail> listBillDetail =
    // billDetailRepository.findByProductIn(products);// all billdetail relate to
    // // seller
    // List<Evalue> listEvalue =
    // evalueRepository.findByBillDetailIn(listBillDetail);// all evalue relate to
    // seller
    // int totalStars = listEvalue.stream()
    // .mapToInt(Evalue::getStar) // total star from evalue
    // .sum();
    // double averageStars = listEvalue.isEmpty() ? 0 : (double) totalStars /
    // listEvalue.size();
    // return averageStars; // average star precaution
    // }

    // Integer idSeller; x
    // String avatar; x
    // String phone;
    // String background; X
    // String shopName; X
    // String district; // huyện X
    // Integer averageStarRating; // trung bình sao
    // Integer numberOfFollowers; // số luong followers X
    // Integer numberOfProducts; // số lượng sản phẩm X
    // Data participationTime; // tới gian bán
    // Integer trackingNumber; // số lượng theo dõi
    // Integer shopCancellationRate; // Tỷ lệ Shop hủy đơn(%)
}
