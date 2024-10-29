package com.toel.dto.Api;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    int code = 1000;
    String message;
    T result;


    public ApiResponse<T> code(int code) {
        this.code = code;
        return this;
    }

    public ApiResponse<T> message(String message) {
        this.message = message;
        return this;
    }

    public ApiResponse<T> result(T result) {
        this.result = result;
        return this;
    }

    public static <T> ApiResponse<T> build() {
        return new ApiResponse<>();
    }
}
