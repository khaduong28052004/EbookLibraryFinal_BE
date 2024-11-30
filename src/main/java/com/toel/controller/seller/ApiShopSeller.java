package com.toel.controller.seller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.request.Request_Account;
import com.toel.dto.seller.response.Response_Account;
import com.toel.exception.AppException;
import com.toel.service.admin.Service_Product;
import com.toel.service.seller.Service_ShopSeller;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/seller/shop")
public class ApiShopSeller {

        @Autowired
        Service_ShopSeller service_Shop;
        @Autowired
        Service_Product productService;

        // @PostMapping("/save")
        // public ApiResponse<Response_Account> save(
        //                 @RequestBody @Valid Request_Account request_Account) {
        //         return ApiResponse.<Response_Account>build()
        //                         .message("Cập nhật thông tin Shop thành công")
        //                         .result(service_Shop.save(request_Account));
        // }

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

        //                 // Kiểm tra nếu không có ảnh nào được cập nhật
        //                 if (!isUpdated) {
        //                         return ApiResponse.<Void>build()
        //                                         .message(message)
        //                                         .code(400); // Mã lỗi 400 nếu không có ảnh được cập nhật
        //                 }

        //                 // Trả về thông báo thành công
        //                 return ApiResponse.<Void>build().message(message);
        //         } catch (Exception e) {
        //                 // Trả về lỗi 500 nếu có lỗi xảy ra trong quá trình cập nhật
        //                 return ApiResponse.<Void>build()
        //                                 .message("Đã xảy ra lỗi khi cập nhật ảnh")
        //                                 .code(500); // Mã lỗi 500 (Internal Server Error)
        //         }
        // }
@PostMapping("/saveImg")
public ApiResponse<Void> saveImg(
        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id,
        @RequestPart(value = "avatar", required = false) MultipartFile avatar,
        @RequestPart(value = "background", required = false) MultipartFile background) {

    try {
        // Kiểm tra nếu không có ảnh nào được gửi lên
        if ((avatar == null || avatar.isEmpty()) && (background == null || background.isEmpty())) {
            return ApiResponse.<Void>build()
                    .message("Không có ảnh nào được gửi lên để cập nhật")
                    .code(400); // Mã lỗi 400 nếu không có ảnh
        }

        // Gọi service để lưu ảnh
        boolean isUpdated = service_Shop.saveImage(account_id, avatar, background);

        // Tạo thông báo dựa trên kết quả cập nhật
        String message = isUpdated
                ? "Cập nhật ảnh thành công"
                : "Không có ảnh nào được cập nhật hoặc tài khoản không tồn tại";

        return ApiResponse.<Void>build()
                .message(message)
                .code(isUpdated ? 200 : 400); // 200: Thành công, 400: Không có gì để cập nhật

    } catch (AppException e) {
        return ApiResponse.<Void>build()
                .message(e.getMessage())
                .code(e.getErrorCode().getCode()); // Trả mã lỗi phù hợp với AppException
    } catch (Exception e) {
        return ApiResponse.<Void>build()
                .message("Đã xảy ra lỗi khi cập nhật ảnh")
                .code(500); // Mã lỗi 500 (Internal Server Error)
    }
}

}
