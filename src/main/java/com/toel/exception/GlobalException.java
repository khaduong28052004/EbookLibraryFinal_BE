package com.toel.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.toel.dto.Api.ApiResponse;

import jakarta.validation.ConstraintViolation;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        String formattedMessage = errorCode.getMessage();

        BindingResult bindingResult = ex.getBindingResult();
        var fieldError = bindingResult.getFieldError();
        if (fieldError != null) {
            String fieldString = fieldError.getField();
            String enumString = fieldError.getDefaultMessage();
            try {
                errorCode = ErrorCode.valueOf(enumString); // Sử dụng tên hằng số cho ErrorCode
            } catch (IllegalArgumentException e) {
                errorCode = ErrorCode.INVALID_REQUEST; // Gán mã lỗi mặc định nếu không tìm thấy
            }
            formattedMessage = errorCode.getMessage().replace("{key}", fieldString);
            // ConstraintViolation<?> cung cấp thông tin chi tiết về vi phạm ràng buộc (như
            // @NotNull, @NotBlank, @Future,...).
            ConstraintViolation<?> violation = fieldError.unwrap(ConstraintViolation.class);
            if (violation != null) {
                Map<String, Object> attributes = violation.getConstraintDescriptor().getAttributes();
                if (attributes.containsKey("value")) {
                    String value = attributes.get("value").toString();
                    if (enumString.equalsIgnoreCase("FIELD_MIN_VALUE")
                            || enumString.equalsIgnoreCase("FIELD_MIN_KEY")) {
                        formattedMessage = formattedMessage.replace("{min}", value);
                    } else if (enumString.equalsIgnoreCase("FIELD_MAX_VALUE")) {
                        formattedMessage = formattedMessage.replace("{max}", value);
                    }
                } else {
                    if (attributes.containsKey("min")) {
                        String minLength = attributes.get("min").toString();
                        formattedMessage = formattedMessage.replace("{min}", minLength);
                    }
                    if (attributes.containsKey("max")) {
                        String maxLength = attributes.get("max").toString();
                        formattedMessage = formattedMessage.replace("{max}", maxLength);
                    }
                }
            }
        }

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.build()
                        .code(errorCode.getCode())
                        .message(formattedMessage));
    }

    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode() != null ? ex.getErrorCode() : ErrorCode.UNCATEGORIZED_EXCEPTION;
        String formattedMessage = errorCode.getMessage();
        if (ex.getParams() != null && ex.getParams().length > 0) {
            formattedMessage = String.format(formattedMessage, ex.getParams());
        }
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.build()
                        .code(errorCode.getCode())
                        .message(formattedMessage));
    }
}
