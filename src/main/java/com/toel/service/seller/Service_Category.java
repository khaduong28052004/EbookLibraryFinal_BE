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
import com.toel.mapper.seller.CategoryMapper;
import com.toel.model.Category;
import com.toel.repository.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class Service_Category {
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    CategoryRepository categoryRepository;

    public PageImpl<Response_Category> getAll(int page, int size, boolean sortBy, String sortColumn) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<Category> pagetCategory = categoryRepository.findAll(pageable);
        List<Response_Category> list = pagetCategory.stream()
                .map(category -> categoryMapper.response_Category(category))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pagetCategory.getTotalElements());
    }

    public Response_Category save(Request_Category request_Category) {
        Category entity = categoryMapper.category(request_Category);
        return categoryMapper.response_Category(categoryRepository.saveAndFlush(entity));
    }

    public void delete(Integer id_category) {
        categoryRepository.findById(id_category)
                .ifPresentOrElse(
                        category -> categoryRepository.delete(category),
                        () -> {
                            throw new EntityNotFoundException("Category with id " + id_category + " not found.");
                        });
    }
}
