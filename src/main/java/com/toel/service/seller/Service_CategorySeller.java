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
import com.toel.repository.AccountRepository;
import com.toel.repository.CategoryRepository;
import com.toel.util.log.LogUtil;

@Service
public class Service_CategorySeller {

        @Autowired
        CategoryMapper categoryMapper;

        @Autowired
        CategoryRepository categoryRepository;

        @Autowired
        ProductMapper productMapper;

        @Autowired
        AccountRepository accountRepository;
        @Autowired
        LogUtil service_Log;

        public PageImpl<Response_Category> getAll(
                        Integer page, Integer size, boolean sortBy, String sortColumn, String search) {
                try {
                        Pageable pageable = PageRequest.of(page, size,
                                        Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
                        Page<Category> pageCategory = categoryRepository.findALlBySearch(search, pageable);
                        return new PageImpl<>(pageCategory.stream()
                                        .map(category -> categoryMapper.response_Category(category))
                                        .collect(Collectors.toList()), pageable, pageCategory.getTotalElements());
                } catch (Exception e) {
                        e.printStackTrace();
                        throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "System");
                }

        }

        public PageImpl<Response_CategorySeller> getAllSeller(
                        Integer page, Integer size, boolean sortBy, String sortColum, String search,
                        Integer account_id) {
                try {
                        Pageable pageable = PageRequest.of(page, size,
                                        Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColum));
                        Page<Object[]> pageCategory = categoryRepository.findCategoriesWithParentName(search,
                                        account_id,
                                        pageable);
                        return new PageImpl<>(pageCategory.stream()
                                        .map(objects -> {
                                                Integer id = (Integer) objects[0];
                                                String name = (String) objects[1];
                                                Integer idParent = (Integer) objects[2];
                                                String parentName = (String) objects[3];
                                                Boolean hasProducts = (Boolean) objects[4];
                                                return new Response_CategorySeller(id, name, idParent,
                                                                parentName != null ? parentName : "Không Có Danh Mục",
                                                                hasProducts);
                                        })
                                        .collect(Collectors.toList()), pageable, pageCategory.getTotalElements());
                } catch (Exception e) {
                        e.printStackTrace();
                        throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "System");
                }

        }

        public List<Response_Category> getAllList() {
                return categoryRepository.findALlByIdParentZero().stream()
                                .map(category -> categoryMapper.response_Category(category))
                                .collect(Collectors.toList());
        }

        public List<Response_Category> getIdParentAndAccount(
                        Integer idParent, Integer account_id) {
                return categoryRepository.findALlByIdParentAndAccount(idParent, account_id).stream()
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
                                .map(category -> {
                                        category.setAccount(accountRepository.findById(request_Category.getAccount())
                                                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
                                                                        "Account")));
                                        return category;
                                })
                                .filter(this::checkCategory)
                                .map(categoryRepository::saveAndFlush)
                                .map(category -> {
                                        service_Log.setLog(getClass(), request_Category.getAccount(), "INFO",
                                                        "Category",
                                                        categoryMapper.response_Category(category), null,
                                                        "Thêm thể loại");
                                        return category;
                                })
                                .map(categoryMapper::response_Category)
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_SETUP, "Tên danh mục đã tồn tại"));
        }

        public Response_Category update(
                        Request_CategoryUpdate request_Category, Integer accountID) {
                Category categoryOle = categoryRepository.findById(request_Category.getId())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
                                                "Account"));
                return Optional.of(request_Category)
                                .map(categoryMapper::categoryUpdate)
                                .map(category -> {
                                        category.setAccount(accountRepository.findById(request_Category.getAccount())
                                                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
                                                                        "Account")));
                                        return category;
                                })
                                .filter(this::checkCategory)
                                .map(categoryRepository::saveAndFlush)
                                .map(category -> {
                                        if (accountID != null) {
                                                service_Log.setLog(getClass(), accountID, "INFO",
                                                                "Category", categoryMapper.response_Category(categoryOle),
                                                                categoryMapper.response_Category(category),
                                                                "Cập nhật thể loại");
                                        }
                                        return category;
                                })
                                .map(categoryMapper::response_Category)
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_SETUP, "Tên danh mục đã tồn tại"));
        }

        public void delete(
                        Integer id_category, Integer accountID) {
                categoryRepository.findById(id_category)
                                .ifPresentOrElse(category -> {
                                        categoryRepository.delete(category);
                                        if (accountID != null) {
                                                service_Log.setLog(getClass(), accountID, "INFO",
                                                                "Category",
                                                                categoryMapper.response_Category(category), null,
                                                                "Xóa thể loại");
                                        }
                                }, () -> {
                                        throw new AppException(ErrorCode.OBJECT_NOT_FOUND, "Category");
                                });
        }

        public Response_Category edit(
                        Integer id) {
                return categoryMapper.response_Category(categoryRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Danh mục")));
        }

        public boolean checkCategory(
                        Category category) {
                return categoryRepository.findALlByIdAccount(category.getAccount().getId()).stream()
                                .noneMatch(categoryCheck -> category.getName()
                                                .equals(categoryCheck.getName())
                                                && (category.getId() == null
                                                                || !category.getId().equals(categoryCheck.getId())));
        }
}
