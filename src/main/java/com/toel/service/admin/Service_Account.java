package com.toel.service.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.request.Account.Request_AccountCreate;
import com.toel.dto.admin.response.Response_Account;
import com.toel.dto.admin.response.Response_TK_Seller;
import com.toel.mapper.AccountMapper;
import com.toel.model.Account;
import com.toel.model.Role;
import com.toel.repository.AccountRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.FollowerRepository;
import com.toel.repository.ProductRepository;
import com.toel.repository.RoleRepository;

@Service
public class Service_Account {
        @Autowired
        AccountRepository accountRepository;
        @Autowired
        AccountMapper accountMapper;
        @Autowired
        RoleRepository roleRepository;
        @Autowired
        FollowerRepository followerRepository;
        @Autowired
        ProductRepository productRepository;
        @Autowired
        EvalueRepository evalueRepository;

        public PageImpl<?> getAll(String rolename,
                        String search, Boolean gender, Integer page, Integer size, Boolean sortBy, String sortColumn) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
                Role role = roleRepository.findByNameIgnoreCase(rolename);
                Page<Account> pageAccount = null;
                if (search == null || search.isBlank()) {
                        pageAccount = (gender == null)
                                        ? accountRepository.findAllByRoleAndStatus(role, true, pageable)
                                        : accountRepository.findAllByRoleAndStatusAndGender(role, true, gender,
                                                        pageable);
                } else {
                        pageAccount = (gender == null)
                                        ? accountRepository
                                                        .findAllByUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContainingAndStatusAndRole(
                                                                        search, search, search, search, true,
                                                                        role,
                                                                        pageable)
                                        : accountRepository
                                                        .findAllByGenderAndStatusAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                                        gender, true, role, search, search,
                                                                        search,
                                                                        search, pageable);
                }
                List<?> list = pageAccount.stream()
                                .map(account -> {
                                        if (rolename.equalsIgnoreCase("Seller")) {
                                                Response_TK_Seller accountnew = accountMapper
                                                                .to_TK_Seller(account);
                                                accountnew.setSumFollow(followerRepository
                                                                .findAllByShopId(account.getId()).size());
                                                accountnew.setSumProduct(
                                                                productRepository.findAllByAccount(account).size());
                                                accountnew.setAgvEvalue(evalueRepository
                                                                .calculateAverageStarByAccountId(account.getId()));
                                                return accountnew;
                                        } else {
                                                return accountMapper.toAccount(account);
                                        }
                                })
                                .collect(Collectors.toList());
                return new PageImpl<>(list, pageable, pageAccount.getTotalElements());
        }

        public PageImpl<Response_Account> getAllSellerNotBorwse(String search, Boolean gender, Integer page,
                        Integer size,
                        Boolean sortBy, String sortColumn) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
                Role role = roleRepository.findByNameIgnoreCase("USER");
                Page<Account> pageAccount = null;
                if (search == null || search.isBlank()) {
                        pageAccount = (gender == null)
                                        ? accountRepository.findAllByRoleAndStatusAndNumberIdIsNotNull(role, true,
                                                        pageable)
                                        : accountRepository.findAllByRoleAndStatusAndGenderAndNumberIdIsNotNull(role,
                                                        true, gender,
                                                        pageable);
                } else {
                        pageAccount = (gender == null)
                                        ? accountRepository
                                                        .findAllByUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContainingAndStatusAndRoleAndNumberIdIsNotNull(
                                                                        search, search, search, search, true,
                                                                        role,
                                                                        pageable)
                                        : accountRepository
                                                        .findAllByGenderAndStatusAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContainingAndNumberIdIsNotNull(
                                                                        gender, true, role, search, search,
                                                                        search,
                                                                        search, pageable);
                }
                List<Response_Account> list = pageAccount.stream()
                                .map(accountMapper::toAccount)
                                .collect(Collectors.toList());
                return new PageImpl<>(list, pageable, pageAccount.getTotalElements());
        }

        public Response_Account updateStatus(int id) {
                Account entity = accountRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy account"));
                entity.setStatus(!entity.isStatus());
                return accountMapper.toAccount(accountRepository.saveAndFlush(entity));
        }

        public Response_Account updateActive(int id) {
                Account entity = accountRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy account"));
                Role role = roleRepository.findByNameIgnoreCase("Seller");
                entity.setRole(role);
                return accountMapper.toAccount(accountRepository.saveAndFlush(entity));
        }

        public Response_Account create(String rolename, Request_AccountCreate entity) {
                Account account = accountMapper.toAccountCreate(entity);
                account.setRole(roleRepository.findByNameIgnoreCase(rolename));
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
