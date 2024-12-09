package com.toel.controller.seller;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.request.Account.Request_AccountCreateOTP;
import com.toel.dto.admin.response.Response_ProductListFlashSale;
import com.toel.dto.seller.response.Response_InforSeller;
import com.toel.dto.seller.response.Response_Like;
import com.toel.dto.seller.response.Response_Product;
import com.toel.dto.seller.response.Response_ProductInfo;
import com.toel.dto.user.resquest.Request_Evaluate_User;
import com.toel.dto.user.resquest.Request_ReportShop_DTO;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.ProductMapper;
import com.toel.mapper.user.LikeMapper;
import com.toel.model.Account;
import com.toel.model.Address;
import com.toel.model.Bill;
import com.toel.model.BillDetail;
import com.toel.model.Evalue;
import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;
import com.toel.model.ImageProduct;
import com.toel.model.Like;
import com.toel.model.OrderStatus;
import com.toel.model.Product;
import com.toel.model.TypeVoucher;
import com.toel.model.Voucher;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.FlashSaleDetailRepository;
import com.toel.repository.FlashSaleRepository;
import com.toel.repository.FollowerRepository;
import com.toel.repository.ImageProductRepository;
import com.toel.repository.LikeRepository;
import com.toel.repository.OrderStatusRepository;
import com.toel.repository.ProductRepository;
import com.toel.repository.TypeVoucherRepository;
import com.toel.repository.VoucherRepository;
import com.toel.service.user.FollowerService;
import com.toel.service.user.Service_SelectAllProductHome;
import com.toel.service.user.Service_ShowInfoSeller;

import jakarta.validation.Valid;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @Autowired
    private FollowerRepository followRepository;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    private Service_ShowInfoSeller serviceShowInfoSeller;
    @Autowired
    FlashSaleRepository flashSaleRepo;
    @Autowired
    Service_SelectAllProductHome serviceSellectAll;
    @Autowired
    FlashSaleDetailRepository flashSaleDetailRepo;

    /**
     * homeShowSeller
     * Thông tin người bán:
     * - Số lượng sản phẩm
     * - Số lượng theo dõi
     * - Tỉ lệ Shop hủy đơn
     * - Thời gian tham gia
     * 
     * @param body Dữ liệu chứa sellerID và userID
     * @return Thông tin người bán
     */
    @PostMapping("/api/v1/user/informationSeller")
    public ApiResponse<?> informationSellerPublic(@RequestBody Map<String, String> body) {
        String sellerID = body.get("sellerID");
        String userID = body.get("userID");
        Integer defaultValue = 1;
        if (sellerID.equals(userID)) {
            return ApiResponse.<String>build()
                    .code(400)
                    .message("Invalid Seller ID: Not a number")
                    .result(null);
        }
        if (!isNumeric(sellerID)) {
            return ApiResponse.<String>build()
                    .code(400)
                    .message("Invalid Seller ID: Not a number")
                    .result(null);
        }
        try {
            Account account = accountRepository.findById(Integer.parseInt(sellerID))
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
                            "ID:[" + sellerID + "] không tìm thấy người bán"));

            Response_InforSeller inforSeller = new Response_InforSeller();
            inforSeller.setIdSeller(account.getId());
            inforSeller.setNumberOfProducts(account.getProducts().size());
            inforSeller.setTrackingNumber(defaultValue); // nguoi ban da dax theo gioi casi gi
            inforSeller.setShopCancellationRate(defaultValue);
            inforSeller.setAvatar(account.getAvatar());
            inforSeller.setBackground(account.getBackground());

            Integer follower = followRepository.countFollowersByShopId(account.getId());
            inforSeller.setNumberOfFollowers(follower);
            // Kiểm tra userID và set trạng thái theo dõi
            if (isNumeric(userID) || !sellerID.equals(userID)) {
                inforSeller.setIsFollowed(
                        followerService.checkFollower(Integer.parseInt(userID), Integer.parseInt(sellerID)));
            } else {
                inforSeller.setIsFollowed(null);
            }
            // Lấy địa chỉ mặc định
            String district = account.getAddresses().stream()
                    .filter(Address::isStatus)
                    .map(Address::getFullNameAddress)
                    .findFirst()
                    .orElse("Chưa cập nhật!");
            inforSeller.setDistrict(district);
            inforSeller.setCreateAtSeller(account.getCreateAtSeller());
            inforSeller.setParticipationTime(inforSeller.calculateActiveDays());
            inforSeller.setShopName(account.getShopName());
            // Kết quả trả về
            Map<String, Object> map = new HashMap<>();
            map.put("shopDataEX", inforSeller);
            map.put("rating", ValueAverageStars(account));

            return ApiResponse.<Map>build().code(100).message("Success").result(map);
        } catch (Exception e) {
            return ApiResponse.<String>build().code(500).message(e.getMessage()).result(null);
        }
    }

    @GetMapping("path")
    public String getMethodName() {

        return new String();
    }

    /**
     * Kiểm tra chuỗi có phải là số không
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

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
            return ApiResponse.<String>build().code(0).message("").result(null);
        }

    }

    // @PostMapping("/api/v1/user/topProducts")
    // public ApiResponse<?> topProducts(@RequestBody Map<String, String> body) {
    // String sellerID = body.get("sellerID");
    // // String userID = body.get("userID");
    // System.out.println("ly lor" + sellerID);
    // try {
    // OrderStatus orderStatus = orderStatusRepository.findById(1).orElse(null);
    // if (orderStatus == null) {
    // return ApiResponse.<String>build().code(1).message("Đơn hoàn thành bằng
    // 0").result(null);
    // }
    // List<Bill> listBill = billRepository.findByOrderStatus(orderStatus);
    // if (listBill == null || listBill.isEmpty()) {
    // return ApiResponse.<String>build().code(1).message("không có hóa đơn
    // nào!").result(null);
    // }
    // List<BillDetail> listBillDetails =
    // billDetailRepository.findAllByBillIn(listBill);
    // List<Product> listProduct =
    // productRepository.findByBillDetails(listBillDetails);
    // // List<Response_ProductInfo> listResponse_Products = productMapper
    // // .Response_ProductInfo(listProductO);
    // Map<String, Object> hash = new HashMap<>();
    // hash.put("listProduct", listBillDetails);
    // return ApiResponse.<Map>build()
    // .code(0)
    // .message("Top sản moi phẩm bán chạy ")
    // .result(hash);
    // // List<Product> listProduct =
    // // productRepository.findByBillDetails(listBillDetails);

    // //
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // // Map<String, Object> hash = new HashMap<>();
    // // // List<Product> listProductO = new ArrayList<>();
    // // List<Product> listProductO = new ArrayList<>();
    // // if (isNumeric(sellerID)) {
    // // listProductO = productRepository.findAll(Sort.by(Sort.Direction.DESC,
    // // "id")).stream()
    // // .filter(product ->
    // // !product.getAccount().getId().equals(Integer.parseInt(sellerID))) // Lọc
    // bỏ
    // // .limit(10) // Giới hạn kết quả
    // // .collect(Collectors.toList());
    // // } else {
    // // listProductO = productRepository.findAll(Sort.by(Sort.Direction.DESC,
    // // "id")).stream()
    // // // .filter(product ->
    // // // !product.getAccount().getId().equals(Integer.parseInt(sellerID))) //
    // Lọc
    // // bỏ
    // // // sellerID
    // // .limit(10) // Giới hạn kết quả
    // // .collect(Collectors.toList());
    // // }
    // // List<Response_ProductInfo> listResponse_Products = productMapper
    // // .Response_ProductInfo(listProductO);
    // // hash.put("listProduct", listResponse_Products);
    // // return ApiResponse.<Map>build()
    // // .code(0)
    // // .message("Top sản moi phẩm bán chạy ")
    // // .result(hash);
    // //
    // ............>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..
    // } catch (Exception e) {
    // return ApiResponse.<Map>build()
    // .code(0)
    // .message(e.getMessage())
    // .result(null);
    // }

    // }
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    LikeMapper likeMapper;

    @PostMapping("/api/v1/user/topLikeProducts1")
    public ApiResponse<?> thichNhieu(@RequestBody Map<String, String> body) {
        // TODO: process POST request
        List<Like> listLike = likeRepository.findAll();
        List<Response_Like> responeLike = likeMapper.mapToResponseLikeList(listLike);
        List<Integer> idproduct = new ArrayList<>();
        // idproduct = listLike.
        for (Response_Like response_Like : responeLike) {
            idproduct.add(response_Like.getProduct());
        }

        List<Product> lisProducts = productRepository.findAllById(idproduct);
        Map<String, Object> response = new HashMap<>();
        response.put("response", responeLike);
        return ApiResponse.<Map>build().message("getMethodName()").result(response);
    }

    @PostMapping("/api/v1/user/topLikeProducts") // đang dùng
    public ApiResponse<?> thichNhieu1(@RequestBody Map<String, String> body) {
        // Lấy danh sách sản phẩm theo lượt like
        List<Map<String, Object>> topLikedProducts = likeRepository.findTopLikedProducts();
        // Lấy danh sách id sản phẩm từ kết quả truy vấn
        List<Integer> productIds = topLikedProducts.stream()
                .map(item -> (Integer) item.get("productId"))
                .collect(Collectors.toList());
        // Lấy chi tiết sản phẩm từ danh sách id
        List<Product> products = productRepository.findAllById(productIds);
        List<Response_ProductInfo> listResponse_Products = productMapper.Response_ProductInfo(products);
        // Chuẩn bị dữ liệu phản hồi
        List<Map<String, Object>> responseList = topLikedProducts.stream()
                .map(item -> {
                    Integer productId = (Integer) item.get("productId");
                    Long likeCount = (Long) item.get("likeCount");
                    // Tìm chi tiết sản phẩm tương ứng
                    Response_ProductInfo product = listResponse_Products.stream()
                            .filter(p -> p.getId().equals(productId))
                            .findFirst()
                            .orElse(null);
                    // Trả về dữ liệu sản phẩm với số lượt like
                    Map<String, Object> response = new HashMap<>();
                    response.put("product", product);
                    response.put("likeCount", likeCount);
                    return response;
                })
                .collect(Collectors.toList());

        // Đóng gói phản hồi API
        Map<String, Object> response = new HashMap<>();
        response.put("topLikedProducts", responseList);
        return ApiResponse.<Map>build().message("Top liked products retrieved successfully").result(response);
    }

    @PostMapping("/api/v1/user/topProducts") // đang dùng
    public ApiResponse<?> topProducts(@RequestBody Map<String, String> body) {
        String sellerID = body.get("sellerID");
        System.out.println("Seller ID: " + sellerID);
        try {
            OrderStatus orderStatus = orderStatusRepository.findById(1).orElse(null);
            if (orderStatus == null) {
                return ApiResponse.<String>build().code(1).message("Order status not found").result(null);
            }

            List<Bill> listBill = billRepository.findByOrderStatus(orderStatus);
            if (listBill == null || listBill.isEmpty()) {
                return ApiResponse.<String>build().code(1).message("No bills found").result(null);
            }

            // Fetch top 10 products using custom query
            List<Product> topProducts = productRepository.findTop10ByBillDetails(listBill);
            List<Response_ProductInfo> listResponse_Products = productMapper.Response_ProductInfo(topProducts);

            Map<String, Object> response = new HashMap<>();
            response.put("listProduct", listResponse_Products);

            return ApiResponse.<Map<String, Object>>build()
                    .code(0)
                    .message("Top 10 best-selling products fetched successfully")
                    .result(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.<String>build().code(1).message("Error fetching top products").result(null);
        }
    }

    @GetMapping("/api/v1/user/topProducts1")
    public ApiResponse<?> topProducts1(@RequestBody Map<String, String> body) {
        String sellerID = body.get("sellerID");
        // if(isNumeric(sellerID)) return
        // ApiResponse.<String>build().message("sai"+sellerID);
        Boolean isaccount = accountRepository.existsById(Integer.parseInt(sellerID));
        return ApiResponse.<Boolean>build()
                .code(0)
                .message("e.getMessage()")
                .result(isaccount);

    }

    @RequestMapping("api/v1/user/shop/selectall")
    public ApiResponse<Map<String, Object>> selectAll(
            @RequestParam(name = "id_Shop", defaultValue = "0") Integer id_Shop,
            @RequestParam(name = "size", defaultValue = "8") Integer size,
            @RequestParam(name = "sort", defaultValue = "price") String sort) {
        List<FlashSaleDetail> flashSaleDetails = new ArrayList<FlashSaleDetail>();
        LocalDateTime localDateTime = LocalDateTime.now();
        FlashSale flashSale = flashSaleRepo.findFlashSaleNow(localDateTime);
        try {

            flashSaleDetails = flashSaleDetailRepo.findAllByFlashSale(flashSale);
        } catch (Exception e) {
        }

        Map<String, Object> response = serviceSellectAll.selectAllHomeShop(flashSaleDetails, id_Shop, 0, size, sort);
        response.put("flashSale", flashSale);
        if (response.get("error") != null) {
            return ApiResponse.<Map<String, Object>>build().message("not fault").code(1002);
        }

        return ApiResponse.<Map<String, Object>>build().message("success").result(response);
    }

    @PostMapping("api/v1/user/shop/createReport")
    public ResponseEntity<Map<String, Object>> createReportShop(@Valid @RequestBody Request_ReportShop_DTO reportDTO,
            BindingResult bindingResult) {
        Map<String, Object> response = serviceShowInfoSeller.createReportShop(reportDTO);
        return ResponseEntity.ok(response);

    }

    // @PostMapping("/api/v1/user/send-otpe")
    // public ApiResponse<?> sendOtp(@RequestBody @Valid Request_AccountCreateOTP
    // body) {

    // // String otp = otpService.generateOtp(identifier); // Tạo OTP
    // // otpService.saveOtp(identifier, otp); // Lưu OTP và thời gian hết hạn
    // if (accountRepository.existsByUsername(body.getUsername())) {
    // throw new AppException(ErrorCode.OBJECT_ALREADY_EXISTS, "Tên tài khoản");
    // }

    // // Check if email already exists
    // if (accountRepository.existsByEmail(body.getEmail())) {
    // throw new AppException(ErrorCode.OBJECT_ALREADY_EXISTS, "Email ");
    // }

    // if ("email".equalsIgnoreCase(body.getMethod())) {
    // // emailService.sendOtpEmail(identifier, otp);
    // return ApiResponse.build().message("OTP đã được gửi qua email.");
    // } else if ("phone".equalsIgnoreCase(body.getMethod())) {
    // // smsService.sendOtpSms(identifier, otp);
    // return ApiResponse.build().message("OTP đã được gửi qua phone.");
    // } else {
    // return ApiResponse.build().message("OTP đã được gửi qua g.");
    // }
    // }
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
