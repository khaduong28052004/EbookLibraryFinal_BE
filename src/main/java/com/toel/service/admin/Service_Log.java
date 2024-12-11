package com.toel.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.response.Response_Log;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.AccountMapper;
import com.toel.mapper.CategoryMapper;
import com.toel.mapper.FlashSaleMapper;
import com.toel.mapper.LogMapper;
import com.toel.mapper.ProductMapper;
import com.toel.mapper.RoleMapper;
import com.toel.mapper.RolePermissionMapper;
import com.toel.model.Account;
import com.toel.model.Log;
import com.toel.repository.AccountRepository;
import com.toel.repository.CategoryRepository;
import com.toel.repository.FlashSaleDetailRepository;
import com.toel.repository.FlashSaleRepository;
import com.toel.repository.LogRepository;
import com.toel.repository.ProductRepository;
import com.toel.repository.RolePermissionRepository;
import com.toel.repository.RoleRepository;

@Service
public class Service_Log {
    @Autowired
    LogRepository logRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    FlashSaleRepository flashSaleRepository;
    @Autowired
    FlashSaleDetailRepository flashSaleDetailRepository;
    @Autowired
    RolePermissionRepository rolePermissionRepository;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RolePermissionMapper rolePermissionMapper;
    @Autowired
    FlashSaleMapper flashSaleMapper;
    @Autowired
    LogMapper logMapper;

    public PageImpl<Response_Log> getAll(Integer accountId, String key, int page, int size, Boolean sortBy,
            String column) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, column));
        Page<Log> pageLog;
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account không tồn tại"));

        if (key.isBlank()) {
            pageLog = (account.getId() == 1)
                    ? logRepository.findAll(pageable)
                    : logRepository.findByAccount(account, pageable);
        } else {
            pageLog = (account.getId() == 1)
                    ? logRepository.findByAccountFullname(key, pageable)
                    : logRepository.findByAccountFullnameAndAccount(key, account, pageable);
        }
        // List<Response_Log<Object>> responseLogs = pageLog;
        List<Response_Log> responseLogs = pageLog.stream()
                .map(this::mapToResponseLog)
                .toList();

        return new PageImpl<>(responseLogs, pageable, pageLog.getTotalElements());
    }

    private Response_Log mapToResponseLog(Log log) {
        Response_Log responseLog = logMapper.tResponse_Log(log);

        Map<String, JpaRepository<?, Integer>> repositoryMap = Map.of(
                "account", accountRepository,
                "product", productRepository,
                "category", categoryRepository,
                "role", roleRepository,
                "rolepermission", rolePermissionRepository,
                "flashsale", flashSaleRepository,
                "flashsaledetails", flashSaleDetailRepository);

        JpaRepository<?, Integer> repository = repositoryMap.get(log.getTableName().toLowerCase());
        if (repository == null) {
            throw new AppException(ErrorCode.OBJECT_SETUP,
                    "Không tìm thấy repository cho " + log.getTableName());
        }
        responseLog.setDoituongOld(log.getDataOld());
        responseLog.setDoituongNew(log.getDataNew());
        return responseLog;
    }

}
