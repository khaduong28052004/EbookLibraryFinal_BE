package com.toel.service.admin.Thongke;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                List<Response_TK_Account> listAccounts = allAccounts.stream()
                                .map(account -> calculateProductRevenue(account, finalDateStart, finalDateEnd))
                                .collect(Collectors.toList());

                Comparator<Response_TK_Account> comparator = getComparator(sortColumn, sortBy);
                listAccounts.sort(comparator);
                Pageable pageable = PageRequest.of(page, size);
                int start = (int) pageable.getOffset();
                int end = Math.min(start + pageable.getPageSize(), listAccounts.size());
                List<Response_TK_Account> paginatedList = listAccounts.subList(start, end);
                Page_TK_KhachHang page_TK_KhachHang = Page_TK_KhachHang.builder()
                                .thongke(new PageImpl<>(paginatedList, pageable, allAccounts.size()))
                                .tongDonHang(sumBill)
                                .tongKhachHang(KhachHangDangHoatDong.get() + KhachHangNgungHoatDong.get())
                                .KhachHangDangHoatDong(KhachHangDangHoatDong.get())
                                .KhachHangNgungHoatDong(KhachHangNgungHoatDong.get())
                                .build();

                return page_TK_KhachHang;
        }

        private Comparator<Response_TK_Account> getComparator(String sortColumn, Boolean sortBy) {
                Comparator<Response_TK_Account> comparator;
                switch (sortColumn.toLowerCase()) {
                        case "id":
                                comparator = Comparator.comparing(Response_TK_Account::getId);
                                break;
                        case "fullname":
                                comparator = Comparator.comparing(Response_TK_Account::getFullname);
                                break;
                        case "username":
                                comparator = Comparator.comparing(Response_TK_Account::getUsername);
                                break;
                        case "avgdonhang":
                                comparator = Comparator.comparing(Response_TK_Account::getAvgdonhang);
                                break;
                        case "avgstar":
                                comparator = Comparator.comparing(Response_TK_Account::getAvgStar);
                                break;
                        case "sumdonhang":
                                comparator = Comparator.comparing(Response_TK_Account::getSumDonHang);
                                break;
                        default:
                                throw new IllegalArgumentException("Invalid sort column: " + sortColumn);
                }
                return sortBy ? comparator.reversed() : comparator;
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
