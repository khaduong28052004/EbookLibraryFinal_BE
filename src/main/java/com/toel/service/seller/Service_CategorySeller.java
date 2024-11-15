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

import com.toel.dto.seller.request.Category.Request_CategoryCreate;
import com.toel.dto.seller.request.Category.Request_CategoryUpdate;
import com.toel.dto.seller.response.Response_Category;
import com.toel.dto.seller.response.Response_CategorySeller;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.CategoryMapper;
import com.toel.mapper.ProductMapper;
import com.toel.model.Category;
import com.toel.repository.CategoryRepository;

@Service
public class Service_CategorySeller {

        @Autowired
        CategoryMapper categoryMapper;

        @Autowired
        CategoryRepository categoryRepository;

        @Autowired
        ProductMapper productMapper;

        public PageImpl<Response_Category> getAll(
                        Integer page, Integer size, boolean sortBy, String sortColumn, String search) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
                Page<Category> pageCategory = categoryRepository.findALlBySearch(search, pageable);
                List<Response_Category> list = pageCategory.stream()
                                .map(category -> categoryMapper.response_Category(category))
                                .collect(Collectors.toList());
                return new PageImpl<>(list, pageable, pageCategory.getTotalElements());
        }

        public PageImpl<Response_CategorySeller> getAllSeller(Integer page, Integer size, boolean sortBy,
                        String sortColum,
                        String search) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColum));
                Page<Object[]> pageCategory = categoryRepository.findCategoriesWithParentName(search, pageable);

                List<Response_CategorySeller> list = pageCategory.stream()
                                .map(objects -> {
                                        Integer id = (Integer) objects[0];
                                        String name = (String) objects[1];
                                        Integer idParent = (Integer) objects[2];
                                        String parentName = (String) objects[3];
                                        Boolean hasProducts = (Boolean) objects[4]; // hasProducts
                                        return new Response_CategorySeller(id, name, idParent,
                                                        parentName != null ? parentName : "No Parent", hasProducts);
                                })
                                .collect(Collectors.toList());

                return new PageImpl<>(list, pageable, pageCategory.getTotalElements());
        }

        public List<Response_Category> getAllList() {
                return categoryRepository.findALlByIdParentZero().stream()
                                .map(category -> categoryMapper.response_Category(category))
                                .collect(Collectors.toList());
        }

        public List<Response_Category> getIdParent(
                        Integer idParent) {
                return categoryRepository.findALlByIdParent(idParent).stream()
                                .map(category -> categoryMapper.response_Category(category))
                                .collect(Collectors.toList());
        }

        public Response_Category create(
                        Request_CategoryCreate request_Category) {
                return Optional.of(request_Category)
                                .map(categoryMapper::categoryCreate)
                                .filter(this::checkCategory)
                                .map(categoryRepository::saveAndFlush)
                                .map(categoryMapper::response_Category)
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_SETUP, "Tên sản phẩm đã tồn tại"));
        }

        public Response_Category update(
                        Request_CategoryUpdate request_Category) {
                return Optional.of(request_Category)
                                .map(categoryMapper::categoryUpdate)
                                .filter(this::checkCategory)
                                .map(categoryRepository::saveAndFlush)
                                .map(categoryMapper::response_Category)
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_SETUP, "Tên sản phẩm đã tồn tại"));
        }

        public void delete(
                        Integer id_category) {
                categoryRepository.findById(id_category)
                                .ifPresent(
                                                category -> {
                                                        categoryRepository.delete(category);
                                                });
        }

        public Response_Category edit(
                        Integer id) {
                return categoryMapper.response_Category(categoryRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Thể Loại")));
        }

        public boolean checkCategory(Category category) {
                return categoryRepository.findAll().stream().noneMatch(categoryCheck -> category.getName()
                                .equals(categoryCheck.getName())
                                && (category.getId() == null || !category.getId().equals(categoryCheck.getId())));
        }
}
