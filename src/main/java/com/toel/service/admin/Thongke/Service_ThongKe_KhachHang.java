package com.toel.service.admin.Thongke;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.response.ThongKe.Page_TK_KhachHang;
import com.toel.dto.admin.response.ThongKe.Response_TK_Account;
import com.toel.mapper.AccountMapper;
import com.toel.model.Account;
import com.toel.model.Role;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.RoleRepository;

@Service
public class Service_ThongKe_KhachHang {
        @Autowired
        AccountRepository accountRepository;
        @Autowired
        RoleRepository roleRepository;
        @Autowired
        EvalueRepository evalueRepository;
        @Autowired
        BillRepository billRepository;
        @Autowired
        AccountMapper accountMapper;

        public Page_TK_KhachHang get_TK_KhachHang(Date dateStart, Date dateEnd, String option,
                        String search, Boolean gender, int page, int size,
                        Boolean sortBy, String sortColumn) {

                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Sort.Direction.DESC : Sort.Direction.ASC, sortColumn));
                Role role = roleRepository.findByNameIgnoreCase("user");
                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);

                List<Account> allAccounts;
                if (option.equalsIgnoreCase("macdinh")) {
                        allAccounts = getAllAccounts(role, search, gender);
                } else if (option.equalsIgnoreCase("khachhangmoi")) {
                        allAccounts = getAllAccountsByCreateAt(role, finalDateStart, finalDateEnd, search, gender);
                } else if (option.equalsIgnoreCase("muanhieunhat")) {
                        allAccounts = getAllKhachHangByBillFinalAt(finalDateStart, finalDateEnd, search, gender);
                } else {
                        allAccounts = getAllAccountsByCreateAt(role, finalDateStart, finalDateEnd, search, gender);
                }
                AtomicInteger KhachHangDangHoatDong = new AtomicInteger(0);
                AtomicInteger KhachHangNgungHoatDong = new AtomicInteger(0);
                List<Response_TK_Account> list = allAccounts.stream()
                                .map(account -> {
                                        if (account.isStatus()) {
                                                KhachHangDangHoatDong.incrementAndGet();
                                        } else {
                                                KhachHangNgungHoatDong.incrementAndGet();
                                        }
                                        return calculateProductRevenue(account, finalDateStart, finalDateEnd);
                                })
                                .collect(Collectors.toList());

                Integer sumBill = list.stream().mapToInt(Response_TK_Account::getSumDonHang).sum();
                Page<Account> pageAccount;
                if (option.equalsIgnoreCase("macdinh")) {
                        pageAccount = getPage(role, search, gender, pageable);
                } else if (option.equalsIgnoreCase("khachhangmoi")) {
                        pageAccount = getPageByCreateAt(role, finalDateStart, finalDateEnd, search,
                                        gender, pageable);
                } else if (option.equalsIgnoreCase("muanhieunhat")) {
                        pageAccount = getPageKhachHangByBillFinalAt(finalDateStart, finalDateEnd,
                                        search, gender,
                                        pageable);
                } else {
                        pageAccount = getPageByCreateAt(role, finalDateStart, finalDateEnd, search,
                                        gender, pageable);
                }
                List<Response_TK_Account> listpage = pageAccount.stream()
                                .map(account -> calculateProductRevenue(account, finalDateStart, finalDateEnd))
                                .collect(Collectors.toList());

                Page_TK_KhachHang page_TK_KhachHang = Page_TK_KhachHang.builder()
                                .thongke(new PageImpl<>(listpage, pageable, pageAccount.getTotalElements()))
                                .tongDonHang(sumBill)
                                .tongKhachHang(KhachHangDangHoatDong.get() + KhachHangNgungHoatDong.get())
                                .KhachHangDangHoatDong(KhachHangDangHoatDong.get())
                                .KhachHangNgungHoatDong(KhachHangNgungHoatDong.get())
                                .build();

                return page_TK_KhachHang;
        }

        private Response_TK_Account calculateProductRevenue(Account account, Date startDate, Date endDate) {
                Response_TK_Account response = accountMapper.tResponse_TK_Account(account);
                int sumBill = billRepository.countByAccount(account) == null ? 0
                                : billRepository.countByAccount(account);
                response.setAvgdonhang(billRepository.calculateAGVTotalPriceByAccount(account));
                response.setAvgStar(evalueRepository.calculateAverageStarByKhachHang(account));
                response.setSumDonHang(sumBill);
                return response;
        }

        public List<Account> getAllAccounts(Role role, String search, Boolean gender) {
                if (search == null || search.isBlank()) {
                        return (gender == null) ? accountRepository.findAllByRole(role)
                                        : accountRepository.findAllByRoleAndGender(role, gender);
                } else {
                        return accountRepository
                                        .findAllByGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        gender, role, search, search, search, search);
                }
        }

        public List<Account> getAllAccountsByCreateAt(Role role, Date dateStart, Date dateEnd, String search,
                        Boolean gender) {
                if (search == null || search.isBlank()) {
                        return (gender == null)
                                        ? accountRepository.findAllByCreateAtBetweenAndRole(dateStart, dateEnd, role)
                                        : accountRepository.findAllByCreateAtBetweenAndRoleAndGender(dateStart, dateEnd,
                                                        role, gender);
                } else {
                        return accountRepository
                                        .findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        gender, role, search, search, search, search, dateStart,
                                                        dateEnd);
                }
        }

        public List<Account> getAllKhachHangByBillFinalAt(Date dateStart, Date dateEnd, String search, Boolean gender) {
                if (search == null || search.isBlank()) {
                        return (gender == null) ? billRepository.selectAllByAccountAndFinishAt(dateStart, dateEnd)
                                        : billRepository.selectAllByAccountAndGenderFinishAt(dateStart, dateEnd,
                                                        gender);
                } else {
                        return billRepository
                                        .findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        dateStart, dateEnd, gender, search, search, search, search);
                }
        }

        public Page<Account> getPage(Role role, String search, Boolean gender, Pageable pageable) {
                Page<Account> pageAccount = null;
                if (search == null || search.isBlank()) {
                        pageAccount = (gender == null)
                                        ? accountRepository.findAllByRole(role, pageable)
                                        : accountRepository.findAllByRoleAndGender(role, gender,
                                                        pageable);
                } else {
                        pageAccount = accountRepository
                                        .findAllByGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        gender, role, search, search, search, search, pageable);
                }
                return pageAccount;
        }

        public Page<Account> getPageByCreateAt(Role role, Date dateStart, Date dateEnd,
                        String search, Boolean gender, Pageable pageable) {
                Page<Account> pageAccount;

                if (search == null || search.isBlank()) {
                        pageAccount = (gender == null)
                                        ? accountRepository.findAllByCreateAtBetweenAndRole(dateStart,
                                                        dateEnd, role, pageable)
                                        : accountRepository.findAllByCreateAtBetweenAndRoleAndGender(dateStart,
                                                        dateEnd, role,
                                                        gender, pageable);
                } else {
                        pageAccount = accountRepository
                                        .findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        gender, role, search, search, search, search, dateStart,
                                                        dateEnd, pageable);
                }
                return pageAccount;
        }

        public Page<Account> getPageKhachHangByBillFinalAt(Date dateStart, Date dateEnd,
                        String search, Boolean gender, Pageable pageable) {
                Page<Account> pageAccount;

                if (search == null || search.isBlank()) {
                        pageAccount = (gender == null)
                                        ? billRepository.selectAllByAccountAndFinishAt(dateStart, dateEnd,
                                                        pageable)
                                        : billRepository.selectAllByAccountAndGenderFinishAt(dateStart,
                                                        dateEnd, gender, pageable);
                } else {
                        pageAccount = billRepository
                                        .findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        dateStart, dateEnd, gender, search, search, search,
                                                        search, pageable);
                }
                return pageAccount;
        }

        public Date getDateStart(Date dateStart) {
                Calendar calStart = Calendar.getInstance();
                calStart.set(Calendar.DAY_OF_MONTH, 1);
                Date finalDateStart = (dateStart == null) ? calStart.getTime() : dateStart;
                return finalDateStart;
        }

        public Date getDateEnd(Date dateEnd) {
                Calendar calEnd = Calendar.getInstance();
                calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date finalDateEnd = (dateEnd == null) ? calEnd.getTime() : dateEnd;
                return finalDateEnd;
        }
}
