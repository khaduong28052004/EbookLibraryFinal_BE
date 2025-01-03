package com.toel.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.response.Response_Log;
import com.toel.service.admin.Service_Log;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/log")
public class ApiLog {
    @Autowired
    Service_Log service_Log;

    @GetMapping
    public ApiResponse<PageImpl<Response_Log>> getAllAccount(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "AccountID", required = false) Integer AccountID,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        try {
            return ApiResponse.<PageImpl<Response_Log>>build()
                    .result(service_Log.getAll(AccountID, search, page, size, sortBy, sortColumn));
        } catch (Exception e) {
            return ApiResponse.<PageImpl<Response_Log>>build()
                    .message(e.getMessage());
        }
    }

    @DeleteMapping
    public ApiResponse<Response_Log> delete(
            @RequestParam(value = "id", required = false) Integer id) {
        service_Log.delete(id);
        return ApiResponse.<Response_Log>build()
                .message("Xóa lịch sử thành công");
    }

    @DeleteMapping("/list")
    public ApiResponse<Response_Log> Listdelete(
            @RequestParam(value = "listId", required = false) List<Integer> listId) {
        service_Log.deleteList(listId);
        return ApiResponse.<Response_Log>build()
                .message("Xóa danh sách lịch sử thành công");
    }
}
