package com.toel.controller.auth;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.response.Response_Product;
import com.toel.dto.seller.response.Response_ProductInfo;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.ProductMapper;
import com.toel.mapper.user.ProductMaperUser;
import com.toel.model.BillDetail;
import com.toel.model.FlashSaleDetail;
import com.toel.model.Product;
import com.toel.model.UserProductActions;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.ProductRepository;
import com.toel.service.UserProductActionsService;

@CrossOrigin("*")
@RestController
// /api/actions
public class UserProductActionsController {
    @Autowired
    private UserProductActionsService actionsService;

    /**
     * @param userId     = 0 account mac dinh
     * @param productId  // san pham tac dong
     * @param actionType // loai tac dong
     * @return apireponse ....message(*....);
     */
    @PostMapping("/api/v1/user/actions")
    public ApiResponse<?> handleAction(@RequestParam(defaultValue = "0") Integer userId,
            @RequestParam(defaultValue = "0") Integer productId,
            @RequestParam(defaultValue = "0") String actionType) {
        if (productId == 0 || actionType.equals("0")) {
            throw new AppException(ErrorCode.OBJECT_SETUP, "ID sản phẩm và loại hoạt động không được bỏ trống");
        }
        actionsService.handleUserAction(userId, productId, actionType);
        return ApiResponse.build().code(0).message("Hành động đã được ghi nhận!");
    }

    /**
     * @return list taat car cac hoat dong;
     */
    @GetMapping("/api/v1/user/actions")
    public ApiResponse<?> getAll() {
        // List<UserProductActions> listUserProductActions = re
        List<UserProductActions> list = actionsService.getAll();
        return ApiResponse.<List<UserProductActions>>build().code(0).message("oke").result(list);
    }

    /**
     * @return list taat car cac hoat dong;
     */
    @GetMapping("/api/v1/user/actions_product")
    public ApiResponse<?> getAllProduct() {
        // List<UserProductActions> listUserProductActions = re
        List<Response_ProductInfo> list = actionsService.recommendProducts();
        return ApiResponse.<List<Response_ProductInfo>>build().code(0).message("actions_product").result(list);
    }

    @GetMapping("/api/v1/user/actions_product_user")
    public ApiResponse<?> getAllProductAndUser(@RequestParam(defaultValue = "0") Integer id) {
        // List<UserProductActions> listUserProductActions = re
        List<Response_ProductInfo> list = actionsService.recommendProducts(id);
        return ApiResponse.<List<Response_ProductInfo>>build().code(0).message("actions_product_user").result(list);
    }

    // @GetMapping("/api/v1/user/actions_product_category")
    // public ApiResponse<?> getAllProductAndcategory() {
    // // List<UserProductActions> listUserProductActions = re
    // List<Response_ProductInfo> list =
    // actionsService.recomendProductsAndCategory();
    // return
    // ApiResponse.<List<Response_ProductInfo>>build().code(0).message("actions_product_category").result(list);
    // }

    @GetMapping("/api/v1/user/actions_product_date")
    public ApiResponse<?> getProductsAndCategoriesByDate(@RequestParam LocalDateTime date) {
        List<Response_ProductInfo> recommendedProducts = actionsService.recommendProducts(date);
        return ApiResponse.<List<Response_ProductInfo>>build().code(0).message("actions_product_date")
                .result(recommendedProducts);
    }

    @GetMapping("/api/v1/user/actions_product_date_user")
    public ApiResponse<?> getProductsAndByDateUser(@RequestParam LocalDateTime date,
            @RequestParam(defaultValue = "0") Integer user) {
        List<Response_ProductInfo> recommendedProducts = actionsService.recommendProducts(user, date);
        return ApiResponse.<List<Response_ProductInfo>>build().code(0).message("actions_product_date_user")
                .result(recommendedProducts);
    }

    @GetMapping("/api/v1/user/actions_product_category_date_user")
    public ApiResponse<?> getProductsAndCategoriesByDateUser(@RequestParam LocalDateTime date,
            @RequestParam(defaultValue = "0") Integer user) {
        List<Response_ProductInfo> recommendedProducts = actionsService.recomendProductsAndCategoryDate(user, date);
        return ApiResponse.<List<Response_ProductInfo>>build().code(0).message("actions_product_category_date_user")
                .result(recommendedProducts);
    }

    @GetMapping("/api/v1/user/actions_product_category")
    public ApiResponse<?> getAllProductAndCategory(
            @RequestParam(defaultValue = "0") int page, // Page number
            @RequestParam(defaultValue = "10") int size // Items per page
    ) {
        // Fetch all products from the service
        List<Response_ProductInfo> allProducts = actionsService.recomendProductsAndCategory();

        // Calculate pagination indices
        int start = (int) Math.min((long) page * size, allProducts.size());
        int end = (int) Math.min(start + size, allProducts.size());

        // Extract the current page's content
        List<Response_ProductInfo> pagedContent = allProducts.subList(start, end);

        // Wrap the paginated content with PageImpl
        Page<Response_ProductInfo> pagedList = new PageImpl<>(pagedContent, PageRequest.of(page, size),
                allProducts.size());

        // Return the response
        return ApiResponse.<Page<Response_ProductInfo>>build()
                .code(0)
                .message("actions_product_category")
                .result(pagedList);
    }

    @GetMapping("/api/v1/user/actions_product_category1")
    public ApiResponse<?> getAllProductAndCategory1(
            @RequestParam(defaultValue = "0") int page, // Page number
            @RequestParam(defaultValue = "4") int size, // Items per page
            @RequestParam(defaultValue = "0") Integer account_id // Items per page
    ) {
        // Fetch all products from the service
        List<Response_ProductInfo> allProducts = actionsService.recomendProductsAndCategory(account_id);
        List<Response_ProductInfo> allProducts1 = actionsService.recommendProducts(LocalDateTime.now());
        // Calculate pagination indices

        // Combine the lists
        List<Response_ProductInfo> combinedProducts = new ArrayList<>();
        combinedProducts.addAll(allProducts1);
        combinedProducts.addAll(allProducts);

        int start = (int) Math.min((long) page * size, combinedProducts.size());
        int end = (int) Math.min(start + size, combinedProducts.size());
        // Extract the current page's content
        List<Response_ProductInfo> pagedContent = combinedProducts.subList(start, end);
        // Wrap the paginated content with PageImpl
        Page<Response_ProductInfo> pagedList = new PageImpl<>(pagedContent, PageRequest.of(page, size),
                allProducts.size());

        // Return the response
        return ApiResponse.<Page<Response_ProductInfo>>build()
                .code(0)
                .message("actions_product_category")
                .result(pagedList);
    }

    @GetMapping("/api/v1/user/actions_product_category1_data")
    public ApiResponse<?> getAllProductAndCategorydata(
            @RequestParam(defaultValue = "0") int page, // Page number
            @RequestParam(defaultValue = "4") int size, // Items per page
            @RequestParam(defaultValue = "0") Integer account_id // Items per page
    ) {
        // Fetch all products from the service
        // List<Response_ProductInfo> allProducts =
        // actionsService.recomendProductsAndCategory(account_id);
        List<Response_ProductInfo> allProducts = actionsService.recommendProducts(account_id, LocalDateTime.now());
        List<Response_ProductInfo> allProducts1 = actionsService.recommendProducts();
        // Calculate pagination indices

        // Combine the lists
        List<Response_ProductInfo> combinedProducts = new ArrayList<>();
        combinedProducts.addAll(allProducts);
        combinedProducts.addAll(allProducts1);

        int start = (int) Math.min((long) page * size, combinedProducts.size());
        int end = (int) Math.min(start + size, combinedProducts.size());

        // Extract the current page's content
        List<Response_ProductInfo> pagedContent = combinedProducts.subList(start, end);

        // Wrap the paginated content with PageImpl
        Page<Response_ProductInfo> pagedList = new PageImpl<>(pagedContent, PageRequest.of(page, size),
                combinedProducts.size());

        // Return the response
        return ApiResponse.<Page<Response_ProductInfo>>build()
                .code(0)
                .message("actions_product_category")
                .result(pagedList);
    }

    ProductRepository productRepo;

    ProductMaperUser productMaper;

    @Autowired
    BillDetailRepository billDetailRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    @GetMapping("/api/v1/user/actions_product_category1_Bestseller")
    public ApiResponse<?> getAllProductAndCategorydataBestseller(
            @RequestParam(defaultValue = "0") int page, // Page number
            @RequestParam(defaultValue = "4") int size, // Items per page
            @RequestParam(defaultValue = "0") Integer account_id // Items per page
    ) {
        List<BillDetail> listBD = billDetailRepository.findAll().stream()
                .filter(m -> m.getBill().getOrderStatus().getId() == 5).collect(Collectors.toList());
        List<Product> listP = listBD.stream().map(m -> m.getProduct()).collect(Collectors.toSet()).stream()
                .filter(product -> product.isActive() && product.isDelete() == false)
                .collect(Collectors.toList()); // Convert it back to a list if needed;

        List<Response_ProductInfo> allProducts = productMapper.Response_ProductInfo(listP);
        int start = (int) Math.min((long) page * size, allProducts.size());
        int end = (int) Math.min(start + size, allProducts.size());
        List<Response_ProductInfo> pagedContent = allProducts.subList(start, end);
        Page<Response_ProductInfo> pagedList = new PageImpl<>(pagedContent, PageRequest.of(page, size),
                allProducts.size()); // Return the response
        return ApiResponse.<Page<Response_ProductInfo>>build()
                .code(0)
                .message("actions_product_category")
                .result(pagedList);
    }

    // @PostMapping("/api/v1/user/topLikeProducts") // đang dùng
    // public ApiResponse<?> thichNhieu1(@RequestBody Map<String, String> body) {
    // // Lấy danh sách sản phẩm theo lượt like
    // List<Map<String, Object>> topLikedProducts =
    // likeRepository.findTopLikedProducts();
    // // Lấy danh sách id sản phẩm từ kết quả truy vấn
    // List<Integer> productIds = topLikedProducts.stream()
    // .map(item -> (Integer) item.get("productId"))
    // .collect(Collectors.toList());
    // // Lấy chi tiết sản phẩm từ danh sách id
    // List<Product> products = productRepository.findAllById(productIds);
    // List<Response_ProductInfo> listResponse_Products =
    // productMapper.Response_ProductInfo(products);
    // // Chuẩn bị dữ liệu phản hồi
    // List<Map<String, Object>> responseList = topLikedProducts.stream()
    // .map(item -> {
    // Integer productId = (Integer) item.get("productId");
    // Long likeCount = (Long) item.get("likeCount");
    // // Tìm chi tiết sản phẩm tương ứng
    // Response_ProductInfo product = listResponse_Products.stream()
    // .filter(p -> p.getId().equals(productId))
    // .findFirst()
    // .orElse(null);
    // // Trả về dữ liệu sản phẩm với số lượt like
    // Map<String, Object> response = new HashMap<>();
    // response.put("product", product);
    // response.put("likeCount", likeCount);
    // return response;
    // })
    // .collect(Collectors.toList());

    // // Đóng gói phản hồi API
    // Map<String, Object> response = new HashMap<>();
    // response.put("topLikedProducts", responseList);
    // return ApiResponse.<Map>build().message("Top liked products retrieved
    // successfully").result(response);
    // }
}
