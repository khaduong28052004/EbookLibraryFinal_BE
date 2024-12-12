package com.toel.controller.auth;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.response.Response_ProductInfo;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.model.Product;
import com.toel.model.UserProductActions;
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
        return ApiResponse.<List<Response_ProductInfo>>build().code(0).message("oke").result(list);
    }

}