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
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.response.Response_InforSeller;
import com.toel.dto.seller.response.Response_Product;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.ProductMapper;
import com.toel.model.Account;
import com.toel.model.Address;
import com.toel.model.Bill;
import com.toel.model.BillDetail;
import com.toel.model.Evalue;
import com.toel.model.ImageProduct;
import com.toel.model.OrderStatus;
import com.toel.model.Product;
import com.toel.model.TypeVoucher;
import com.toel.model.Voucher;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.ImageProductRepository;
import com.toel.repository.OrderStatusRepository;
import com.toel.repository.ProductRepository;
import com.toel.repository.TypeVoucherRepository;
import com.toel.repository.VoucherRepository;
import com.toel.service.user.FollowerService;

import lombok.Data;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Autowired
    private ImageProductRepository imageProductRepository;

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
    @GetMapping("/api/v1/user/informationSeller")
    public ApiResponse<?> informationSellerPublic(@RequestBody Map<String, String> body) {
        String sellerID = body.get("sellerID");
        String userID = body.get("userID");

        Integer lo = 1;
        try {
            Account account = accountRepository.findById(Integer.parseInt(sellerID))
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
                            "ID:[" + sellerID + "] không tìm thấy người bán"));
            Response_InforSeller inforSeller = new Response_InforSeller();
            inforSeller.setIdSeller(account.getId());
            inforSeller.setNumberOfProducts(account.getProducts().size());
            inforSeller.setNumberOfFollowers(account.getFollowers().size());
            inforSeller.setTrackingNumber(lo);
            // inforSeller.setAverageStarRating(averageStars(account));
            // inforSeller.setAverageStarRating();
            inforSeller.setShopCancellationRate(lo);
            inforSeller.setAvatar(account.getAvatar());
            inforSeller.setBackground(account.getBackground());
            if (isNumeric(userID)) {
                inforSeller
                        .setIsFollowed(
                                followerService.checkFollower(Integer.parseInt(userID), Integer.parseInt(sellerID)));
                // So sánh giá trị sellerID với một điều kiện
            } else {
                System.out.println("Invalid Seller ID: Not a number.");
            }
            inforSeller
                    .setIsFollowed(followerService.checkFollower(Integer.parseInt(userID), Integer.parseInt(sellerID)));
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

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str); // Thử chuyển đổi thành số
            return true;
        } catch (NumberFormatException e) {
            return false; // Bắt lỗi nếu không phải số
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

    @GetMapping("/api/v1/user/voucherAll/{idSeller}")
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
        try {
            List<TypeVoucher> typeVoucher = typeVoucherRepository.findAll();
            Date date = new Date();
            List<Voucher> listVoucherShop = voucherRepository.findAllByTypeVoucher(typeVoucher.get(0), date);
            List<Voucher> listVoucherSan = voucherRepository.findAllByTypeVoucher(typeVoucher.get(1), date);
            Map<String, Object> hash = new HashMap<>();
            hash.put("san", listVoucherSan);
            hash.put("shop", listVoucherShop);

            return ApiResponse.<Map>build().code(0).message("").result(hash);
        } catch (Exception e) {
            // TODO: handle exception
            return ApiResponse.<String>build().code(0).message("").result(null);
        }

    }

    @Autowired
    ProductMapper productMapper;

    @GetMapping("/api/v1/user/topProducts")
    public ApiResponse<?> topProducts() {
        try {
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
            // if(listBillDetails.isEmpty()){
            // return ApiResponse.<Map>build()
            // .code(0)
            // .message("không có hóa đơn detail nào!")
            // .result(null);
            // }
            // Thống kê số lượng sản phẩm đã bán
            // Map<Product, Long> productCountMap = listBillDetails.stream()
            // .collect(
            // Collectors.groupingBy(BillDetail::getProduct,
            // Collectors.summingLong(BillDetail::getQuantity)));
            // List<BillDetail> listBillDetails1 = billDetailRepository.
            Map<String, Object> hash = new HashMap<>();
            // List<Product> listProduct =
            // productRepository.findByBillDetails(listBillDetails);
            List<Product> listProductO = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                    .limit(10)
                    .collect(Collectors.toList());
            List<ImageProduct> imageProducts = new ArrayList<>();
            // imageProducts = imageProductRepository.findByProductIn(listProductO);
            List<Response_Product> listResponse_Products = productMapper.listResponse_Products(listProductO);

            hash.put("listProduct", listResponse_Products);
            // if (!listProduct.isEmpty()) {
            // hash.put("listProduct", listProduct.stream()
            // .sorted((e1, e2) -> Long.compare(e2.getId(), e1.getId()))
            // .limit(10)
            // .collect(Collectors.toList())); // sản
            // } else {
            // List<Product> listProductO =
            // productRepository.findAll(Sort.by(Sort.Direction.DESC,
            // "id")).stream().limit(10)
            // .collect(Collectors.toList());
            // hash.put("listProduct", listProductO);
            // }
            return ApiResponse.<Map>build()
                    .code(0)
                    .message("Top sold products fetched successfully")
                    .result(hash);
        } catch (Exception e) {
            return ApiResponse.<Map>build()
                    .code(0)
                    .message(e.getMessage())
                    .result(null);
        }

    }

    // @GetMapping("/api/v1/user/topProducts")
    // public ApiResponse<?> topProducts() {
    // try {
    // OrderStatus orderStatus = orderStatusRepository.findById(1).orElse(null);
    // if (orderStatus == null) {
    // return ApiResponse.<String>build().code(1).message("Đơn hoàn thành bằng
    // 0").result(null);
    // }
    // List<Bill> listBill = billRepository.findByOrderStatus(orderStatus);
    // if (listBill == null || listBill.isEmpty()) {
    // // ne
    // return ApiResponse.<String>build().code(1).message("không có hóa đơn
    // nào!").result(null);
    // }
    // List<BillDetail> listBillDetails =
    // billDetailRepository.findAllByBillIn(listBill);
    // // Thống kê số lượng sản phẩm đã bán
    // if (listBillDetails.isEmpty()) {
    // return ApiResponse.<String>build().code(1).message("không có hóa đơn
    // nào!").result(null);
    // }
    // Map<Product, Long> productCountMap = listBillDetails.stream()
    // .collect(
    // Collectors.groupingBy(BillDetail::getProduct,
    // Collectors.summingLong(BillDetail::getQuantity)));

    // // List<BillDetail> listBillDetails1 = billDetailRepository.

    // // // hash.put("listProduct", listProductO);

    // Map<String, Object> hash = new HashMap<>();
    // hash.put("list", listBillDetails);

    // return ApiResponse.<Map>build()
    // .code(0)
    // .message("Top sold products fetched successfully")
    // .result(hash);
    // } catch (Exception e) {
    // // TODO: handle exception
    // return ApiResponse.<Map>build()
    // .code(0)
    // .message(e.getMessage())
    // .result(null);
    // }

    // }

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
