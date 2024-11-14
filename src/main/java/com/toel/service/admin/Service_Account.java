package com.toel.service.admin;

import java.util.Calendar;
import java.util.Date;
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
import com.toel.dto.admin.response.ThongKe.Response_TK_Seller;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.AccountMapper;
import com.toel.model.Account;
import com.toel.model.Role;
import com.toel.repository.AccountReportRepository;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillRepository;
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
        @Autowired
        BillRepository billRepository;
        @Autowired
        AccountReportRepository accountReportRepository;

        public PageImpl<?> getAll(String rolename,
                        String search, Boolean gender, Integer page, Integer size, Boolean sortBy, String sortColumn) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
                Role role = roleRepository.findByNameIgnoreCase(rolename);
                Page<Account> pageAccount = null;
                if (search == null || search.isBlank()) {
                        pageAccount = (gender == null)
                                        ? accountRepository.findAllByRole(role, pageable)
                                        : accountRepository.findAllByRoleAndGender(role, gender,
                                                        pageable);
                } else {
                        pageAccount = accountRepository
                                        .findAllByGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        gender, role, search, search, search, search,
                                                        pageable);
                }
                Calendar calStart = Calendar.getInstance();
                calStart.set(Calendar.DAY_OF_MONTH, 1);
                Calendar calEnd = Calendar.getInstance();
                calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH));

                List<?> list = pageAccount.stream()
                                .map(account -> {
                                        if (rolename.equalsIgnoreCase("Seller")) {
                                                Double doanhSo = (billRepository.getTongDoanhSo(size,
                                                                calStart.getTime(), calEnd.getTime()) == null)
                                                                                ? 0
                                                                                : billRepository.getTongDoanhSo(size,
                                                                                                calStart.getTime(),
                                                                                                calEnd.getTime());
                                                Double doanhThu = (billRepository.getTongDoanhThu(size,
                                                                calStart.getTime(), calEnd.getTime()) == null)
                                                                                ? 0
                                                                                : billRepository.getTongDoanhThu(size,
                                                                                                calStart.getTime(),
                                                                                                calEnd.getTime());
                                                Response_TK_Seller accountnew = accountMapper
                                                                .to_TK_Seller(account);
                                                accountnew.setSumFollower(followerRepository
                                                                .findAllByShopId(account.getId()).size());
                                                accountnew.setSumProduct(
                                                                productRepository.findAllByAccount(account).size());
                                                accountnew.setSumReport(accountReportRepository
                                                                .countByCreateAtBetweenAndShop(
                                                                                calStart.getTime(), calEnd.getTime(),
                                                                                account));
                                                accountnew.setAvgStar(evalueRepository
                                                                .calculateAverageStarByAccountId(account.getId()));
                                                accountnew.setDoanhSo(doanhSo);
                                                accountnew.setDoanhThu(doanhThu);
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

        public Response_Account updateStatus(int id, Boolean status) {
                Account entity = accountRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account"));
                if (status != null && !status) {
                        entity.setStatus(false);
                } else {
                        entity.setStatus(!entity.isStatus());
                }
                return accountMapper.toAccount(accountRepository.saveAndFlush(entity));
        }

        public Response_Account updateActive(int id) {
                Account entity = accountRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account"));
                Role role = roleRepository.findByNameIgnoreCase("Seller");
                entity.setRole(role);
                entity.setCreateAtSeller(new Date());
                return accountMapper.toAccount(accountRepository.saveAndFlush(entity));
        }

        public Response_Account create(String rolename, Request_AccountCreate entity) {
                Account account = accountMapper.toAccountCreate(entity);
                account.setRole(roleRepository.findByNameIgnoreCase(rolename));
                account.setStatus(true);
                account.setCreateAt(new Date());
                return accountMapper.toAccount(accountRepository.saveAndFlush(account));
        }

}
