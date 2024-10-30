package com.toel.service.seller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.seller.request.Request_Category;
import com.toel.dto.seller.response.Response_Category;
import com.toel.mapper.seller.Seller_CategoryMapper;
import com.toel.model.Category;
import com.toel.repository.CategoryRepository;

@Service
public class Service_CategorySeller {

    @Autowired
    Seller_CategoryMapper categoryMapper;

    @Autowired
    CategoryRepository categoryRepository;

    public PageImpl<Response_Category> getAll(
            Integer page, Integer size, boolean sortBy, String sortColumn) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<Category> pageCategory = categoryRepository.findAll(pageable);
        List<Response_Category> list = pageCategory.stream()
                .map(category -> categoryMapper.response_Category(category))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageCategory.getTotalElements());
    }

    public Response_Category save(
            Request_Category request_Category) {
        return categoryMapper
                .response_Category(categoryRepository.saveAndFlush(categoryMapper.category(request_Category)));
    }

    public void delete(
            Integer id_category) {
        categoryRepository.findById(id_category)
                .ifPresent(
                        category -> {
                            categoryRepository.delete(category);
                        });
    }
}
