package com.toel.service.admin;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.toel.dto.admin.request.Account.Request_AccountCreate;
import com.toel.dto.admin.response.Response_Account;
import com.toel.mapper.admin.AccountMapper;
import com.toel.model.Account;
import com.toel.model.Role;
import com.toel.repository.AccountRepository;
import com.toel.repository.RoleRepository;

@Service
public class Service_NhanVien {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    RoleRepository roleRepository;

    public PageImpl<Response_Account> getAll(
            String search, Boolean gender, Integer page, Integer size, Boolean sortBy, String sortColumn) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Role role = roleRepository.findByNameIgnoreCase("ADMINV1");
        Page<Account> pageAccount = null;
        if (search == null || search.isBlank()) {
            pageAccount = (gender == null)
                    ? accountRepository.findAllByRoleAndStatus(role, true, pageable)
                    : accountRepository.findAllByRoleAndStatusAndGender(role, true, gender, pageable);
        } else {
            pageAccount = (gender == null)
                    ? accountRepository
                            .findAllByUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContainingAndStatusAndRole(
                                    search, search, search, search, true, role, pageable)
                    : accountRepository
                            .findAllByGenderAndStatusAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                    gender, true, role, search, search, search, search, pageable);
        }
        List<Response_Account> list = pageAccount.stream()
                .map(accountMapper::toAccount)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageAccount.getTotalElements());
    }

    public Response_Account create(Request_AccountCreate entity) {
        Account account = accountMapper.toAccountCreate(entity);
        account.setRole(roleRepository.findByNameIgnoreCase("ADMINV1"));
        account.setStatus(true);
        return accountMapper.toAccount(accountRepository.saveAndFlush(account));
    }

    public void delete(Integer id) {
        Account entity = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy account"));
        entity.setStatus(false);
        accountRepository.save(entity);
    }
}
