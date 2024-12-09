package com.toel.dto.user.response;

import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.toel.dto.seller.response.Response_Category;
import com.toel.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Search<T> {
    PageImpl<T> product;
    List<Category> categories;
}