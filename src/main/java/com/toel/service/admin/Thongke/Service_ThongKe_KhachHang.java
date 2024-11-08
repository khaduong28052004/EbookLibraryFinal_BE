package com.toel.service.admin.Thongke;

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
import org.springframework.stereotype.Service;

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

        public PageImpl<Response_TK_Account> get_TK_KhachHang(Date dateStart, Date dateEnd, String option,
                        String search, Boolean gender, int page, int size,
                        Boolean sortBy, String sortColumn) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Sort.Direction.DESC : Sort.Direction.ASC, sortColumn));
                Role role = roleRepository.findByNameIgnoreCase("user");
                Page<Account> pageAccount;
                if (option.equalsIgnoreCase("macdinh")) {
                        // account có thể mua hàng hông để chỉnh sửa
                        pageAccount = getPage(role, search, gender, pageable);
                } else if (option.equalsIgnoreCase("khachhangmoi")) {
                        pageAccount = getPageByCreateAt(role, dateStart, dateEnd, search, gender, pageable);
                } else if (option.equalsIgnoreCase("muanhieunhat")) {
                        pageAccount = getPageKhachHangByBillFinalAt(dateStart, dateEnd, search, gender, pageable);
                } else {
                        pageAccount = getPageByCreateAt(role, dateStart, dateEnd, search, gender, pageable);
                }

                List<Response_TK_Account> list = pageAccount.stream()
                                .map(account -> {
                                        Response_TK_Account accountnew = accountMapper.tResponse_TK_Account(account);
                                        int sumBill = billRepository.countByAccount(account) == null ? 0
                                                        : billRepository.countByAccount(account);
                                        accountnew.setAvgdonhang(
                                                        billRepository.calculateAGVTotalPriceByAccount(account));
                                        System.out.println("Đơn hàng: " + accountnew.getAvgdonhang());
                                        accountnew.setAvgStar(
                                                        evalueRepository.calculateAverageStarByKhachHang(account));
                                        System.out.println("trung bình sao: " + accountnew.getAvgStar());
                                        accountnew.setSumDonHang(sumBill);
                                        System.out.println("Tổng đơn hàng: " + accountnew.getSumDonHang());
                                        return accountnew;
                                })
                                .collect(Collectors.toList());

                return new PageImpl<>(list, pageable, pageAccount.getTotalElements());
        }

        public Page<Account> getPage(Role role, String search, Boolean gender, Pageable pageable) {
                Page<Account> pageAccount = null;
                if (search == null || search.isBlank()) {
                        pageAccount = (gender == null)
                                        ? accountRepository.findAllByRoleAndStatus(role, true, pageable)
                                        : accountRepository.findAllByRoleAndStatusAndGender(role, true, gender,
                                                        pageable);
                } else {
                        pageAccount = accountRepository
                                        .findAllByGenderAndStatusAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        gender, true, role, search, search, search, search, pageable);
                }
                return pageAccount;
        }

        public Page<Account> getPageByCreateAt(Role role, Date dateStart, Date dateEnd,
                        String search, Boolean gender, Pageable pageable) {
                Page<Account> pageAccount;

                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);

                if (search == null || search.isBlank()) {
                        pageAccount = (gender == null)
                                        ? accountRepository.findAllByCreateAtBetweenAndRole(finalDateStart,
                                                        finalDateEnd, role, pageable)
                                        : accountRepository.findAllByCreateAtBetweenAndRoleAndGender(finalDateStart,
                                                        finalDateEnd, role,
                                                        gender, pageable);
                } else {
                        pageAccount = accountRepository
                                        .findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        gender, role, search, search, search, search, finalDateStart,
                                                        finalDateEnd, pageable);
                }
                return pageAccount;
        }

        public Page<Account> getPageKhachHangByBillFinalAt(Date dateStart, Date dateEnd,
                        String search, Boolean gender, Pageable pageable) {
                Page<Account> pageAccount;

                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);

                if (search == null || search.isBlank()) {
                        pageAccount = (gender == null)
                                        ? billRepository.selectAllByAccountAndFinishAt(finalDateStart, finalDateEnd,
                                                        pageable)
                                        : billRepository.selectAllByAccountAndGenderFinishAt(finalDateStart,
                                                        finalDateEnd, gender, pageable);
                } else {
                        pageAccount = billRepository
                                        .findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        finalDateStart, finalDateEnd, gender, search, search, search,
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
