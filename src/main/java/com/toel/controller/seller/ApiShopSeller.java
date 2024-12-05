package com.toel.controller.seller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.response.Response_Account;
import com.toel.service.admin.Service_Product;
import com.toel.service.seller.Service_ShopSeller;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/seller/shop")
public class ApiShopSeller {

        @Autowired
        Service_ShopSeller service_Shop;
        @Autowired
        Service_Product productService;

        @GetMapping("/followers/count")
        public ApiResponse<Integer> countFollowers(
                        @RequestParam(value = "shop_id", required = true) Integer shopId) {
                return ApiResponse.<Integer>build()
                                .result(service_Shop.countFollowersByShopId(shopId));
        }

        @GetMapping("/following/count")
        public ApiResponse<Integer> countFollowing(
                        @RequestParam(value = "account_id", required = true) Integer accountId) {
                return ApiResponse.<Integer>build()
                                .result(service_Shop.countFollowingByAccountId(accountId));
        }

        @GetMapping("/posts/count")
        public ApiResponse<Integer> countPosts(
                        @RequestParam(value = "account_id", required = true) Integer accountId) {
                return ApiResponse.<Integer>build()
                                .result(productService.getCountProductByAccountId(accountId));
        }

        @GetMapping("/get")
        public ApiResponse<Response_Account> get(
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id) {
                return ApiResponse.<Response_Account>build()
                                .result(service_Shop.get(account_id));
        }

        // @PostMapping("/saveImg")
        // public ApiResponse<Void> saveImg(
        //                 @RequestParam(value = "account_id", defaultValue = "0") Integer account_id,
        //                 @RequestPart(value = "avatar", required = false) MultipartFile avatar,
        //                 @RequestPart(value = "background", required = false) MultipartFile background) {

        //         try {
        //                 // Kiểm tra nếu không có ảnh nào được gửi lên
        //                 if ((avatar == null || avatar.isEmpty()) && (background == null || background.isEmpty())) {
        //                         return ApiResponse.<Void>build()
        //                                         .message("Không có ảnh nào được gửi lên để cập nhật")
        //                                         .code(400); // Mã lỗi 400 nếu không có ảnh
        //                 }

        //                 // Gọi service để lưu ảnh
        //                 boolean isUpdated = service_Shop.saveImage(account_id, avatar, background);

        //                 // Tạo thông báo dựa trên kết quả cập nhật
        //                 String message = isUpdated
        //                                 ? "Cập nhật ảnh thành công"
        //                                 : "Không có ảnh nào được cập nhật hoặc tài khoản không tồn tại";

        //                 return ApiResponse.<Void>build()
        //                                 .message(message)
        //                                 .code(isUpdated ? 200 : 400); // 200: Thành công, 400: Không có gì để cập nhật

        //         } catch (AppException e) {
        //                 return ApiResponse.<Void>build()
        //                                 .message(e.getMessage())
        //                                 .code(e.getErrorCode().getCode()); // Trả mã lỗi phù hợp với AppException
        //         } catch (Exception e) {
        //                 return ApiResponse.<Void>build()
        //                                 .message("Đã xảy ra lỗi khi cập nhật ảnh")
        //                                 .code(500); // Mã lỗi 500 (Internal Server Error)
        //         }
        // }

}
