package com.toel.exception;

import org.springframework.http.HttpStatus;

import lombok.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1001, "Không được xác thực", HttpStatus.UNAUTHORIZED),
    INVALID_REQUEST(1002, "Yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST),
    PAGE_NUMBER_INVALID(1003, "Số trang không hợp lệ", HttpStatus.BAD_REQUEST),

    OBJECT_NOT_FOUND(2001, "%s không tồn tại", HttpStatus.NOT_FOUND),
    OBJECT_ALREADY_EXISTS(2002, "%s đã tồn tại", HttpStatus.CONFLICT),
    OBJECT_ACTIVE(2003, "%s đang hoạt động", HttpStatus.BAD_REQUEST),
    OBJECT_SETUP(2004, "%s", HttpStatus.BAD_REQUEST),

    FIELD_REQUIRED(3001, "{key} không được bỏ trống!", HttpStatus.BAD_REQUEST),
    FIELD_INVALID_FUTURE_DATE(3002, "{key} phải là thời gian trong tương lai!", HttpStatus.BAD_REQUEST),
    FIELD_MIN_VALUE(3003, "{key} phải lớn hơn hoặc bằng {min}!", HttpStatus.BAD_REQUEST),
    FIELD_MAX_VALUE(3004, "{key} phải bé hơn hoặc bằng {max}!", HttpStatus.BAD_REQUEST),
    FIELD_INVALID_ID(3005, "Id {key} không hợp lệ!", HttpStatus.BAD_REQUEST),
    FIELD_MIN_KEY(3006, "{key} phải lớn hơn hoặc bằng {min} kí tự!", HttpStatus.BAD_REQUEST),

    TIME_RANGE_ERROR(4001, "Thời gian bắt đầu phải trước thời gian kết thúc", HttpStatus.BAD_REQUEST),
    EMAIL_FORMAT_ERROR(4002, "Email không hợp lệ!", HttpStatus.BAD_REQUEST),

    TIME_ERROR(5001, "Khoảng cách giữa ngày bắt đầu và kết thúc không được vượt quá 24 giờ", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    public String formatMessage(Object... params) {
        return String.format(message, params);
    }
}
