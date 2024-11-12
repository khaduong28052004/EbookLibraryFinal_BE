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

import com.toel.dto.admin.response.ThongKe.Page_TK_Seller;
import com.toel.dto.admin.response.ThongKe.Response_TK_Seller;
import com.toel.mapper.AccountMapper;
import com.toel.model.Account;
import com.toel.model.Role;
import com.toel.repository.AccountReportRepository;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.FollowerRepository;
import com.toel.repository.LikeRepository;
import com.toel.repository.ProductRepository;
import com.toel.repository.RoleRepository;

@Service
public class Service_ThongKe_Seller {
        @Autowired
        AccountRepository accountRepository;
        @Autowired
        RoleRepository roleRepository;
        @Autowired
        ProductRepository productRepository;
        @Autowired
        BillDetailRepository billDetailRepository;
        @Autowired
        BillRepository billRepository;
        @Autowired
        EvalueRepository evalueRepository;
        @Autowired
        LikeRepository likeRepository;
        @Autowired
        AccountReportRepository accountReportRepository;
        @Autowired
        FollowerRepository followerRepository;

        @Autowired
        AccountMapper accountMapper;

        public Page_TK_Seller get_TK_Seller(Date dateStart, Date dateEnd, String option,
                        String search, Boolean gender, int page, int size,
                        Boolean sortBy, String sortColumn) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Sort.Direction.DESC : Sort.Direction.ASC, sortColumn));
                Role role = roleRepository.findByNameIgnoreCase("seller");
                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);
                Page<Account> pageAccount = null;
                // List<Account> listAccount;
                // if (option.equalsIgnoreCase("macdinh")) {
                // listAccount = getPage(role, search, gender);
                // } else if (option.equalsIgnoreCase("shopmoi")) {
                // listAccount = getPageByCreateAtSeller(role, dateStart, dateEnd, search,
                // gender);
                // } else if (option.equalsIgnoreCase("donhang")) {
                // listAccount = billRepository.findByFinishAtBetweenAndGenderAndSearch(
                // finalDateStart,
                // finalDateEnd, gender, search);
                // } else if (option.equalsIgnoreCase("sanpham")) {
                // listAccount = getPageShopByProductCreateAt(finalDateStart, finalDateEnd,
                // search, gender);
                // } else if (option.equalsIgnoreCase("baocao")) {
                // listAccount = getPageShopByReportCreateAt(finalDateStart, finalDateEnd,
                // search, gender);
                // } else {
                // listAccount = getPageByCreateAtSeller(role, dateStart, dateEnd, search,
                // gender);
                // }

                // Integer totalShop = listAccount.size();

                if (option.equalsIgnoreCase("macdinh")) {
                        pageAccount = getPage(role, search, gender, pageable);
                } else if (option.equalsIgnoreCase("shopmoi")) {
                        pageAccount = getPageByCreateAtSeller(role, dateStart, dateEnd, search, gender,
                                        pageable);
                } else if (option.equalsIgnoreCase("donhang")) {
                        pageAccount = billRepository.findByFinishAtBetweenAndGenderAndSearch(
                                        finalDateStart,
                                        finalDateEnd, gender, search, pageable);
                } else if (option.equalsIgnoreCase("sanpham")) {
                        pageAccount = getPageShopByProductCreateAt(finalDateStart, finalDateEnd, search, gender,
                                        pageable);
                } else if (option.equalsIgnoreCase("baocao")) {
                        pageAccount = getPageShopByReportCreateAt(finalDateStart, finalDateEnd, search, gender,
                                        pageable);
                } else {
                        pageAccount = getPageByCreateAtSeller(role, dateStart, dateEnd, search, gender,
                                        pageable);
                }
                List<Response_TK_Seller> list = pageAccount.stream()
                                .map(account -> calculateTk_Seller(account, finalDateStart, finalDateEnd))
                                .collect(Collectors.toList());
                Integer totalShop = (int) pageAccount.getTotalElements();
                // Page_TK_Seller page_TK_Seller = Page_TK_Seller.builder()
                // .thongke(new PageImpl<>(list, pageable, pageAccount.getTotalElements()))
                // .build();
                Page_TK_Seller page_TK_Seller = new Page_TK_Seller();
                page_TK_Seller.setTongShop(totalShop);
                page_TK_Seller.setThongke(new PageImpl<>(list, pageable, pageAccount.getTotalElements()));

                return page_TK_Seller;

        }

        public Response_TK_Seller calculateTk_Seller(Account account, Date finalDateStart, Date finalDateEnd) {
                Double doanhSo = billRepository.getTongDoanhSo(account.getId(),
                                finalDateStart, finalDateEnd);
                System.out.println("doanh thu: " + billRepository.getTongDoanhThu(account.getId(),
                                finalDateStart, finalDateEnd));

                Double doanhThu = billRepository.getTongDoanhThu(account.getId(),
                                finalDateStart, finalDateEnd);
                Integer sumFollower = followerRepository
                                .findAllByShopId(account.getId()).size();
                Integer sumProduct = productRepository.findAllByAccount(account).size();
                Integer sumReport = accountReportRepository.countByCreateAtBetweenAndShop(
                                finalDateStart, finalDateEnd, account);
                double agvStar = evalueRepository
                                .calculateAverageStarByAccountId(account.getId());
                Response_TK_Seller entity = accountMapper
                                .to_TK_Seller(account);
                entity.setSumFollower(sumFollower);
                entity.setSumProduct(sumProduct);
                entity.setSumReport(sumReport);
                entity.setAvgStar(agvStar);
                entity.setDoanhSo(doanhSo == null ? 0 : doanhSo);
                entity.setDoanhThu(doanhThu == null ? 0 : doanhThu);
                return entity;
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

        public List<Account> getPage(Role role, String search, Boolean gender) {
                List<Account> listAccounts = null;
                if (search == null || search.isBlank()) {
                        listAccounts = (gender == null)
                                        ? accountRepository.findAllByRole(role)
                                        : accountRepository.findAllByRoleAndGender(role, gender);

                } else {
                        listAccounts = accountRepository
                                        .findAllByGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        gender, role, search, search, search, search);
                }
                return listAccounts;
        }

        public Page<Account> getPageByCreateAtSeller(Role role, Date dateStart, Date dateEnd,
                        String search, Boolean gender, Pageable pageable) {
                Page<Account> pageAccount;

                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);

                if (search == null || search.isBlank()) {
                        pageAccount = (gender == null)
                                        ? accountRepository.findAllByCreateAtBetweenAndRole(finalDateStart,
                                                        finalDateEnd, role, pageable)
                                        : accountRepository.findAllByCreateAtBetweenAndRoleAndGender(
                                                        finalDateStart,
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

        public List<Account> getPageByCreateAtSeller(Role role, Date dateStart, Date dateEnd,
                        String search, Boolean gender) {
                List<Account> pageAccount;

                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);

                if (search == null || search.isBlank()) {
                        pageAccount = (gender == null)
                                        ? accountRepository.findAllByCreateAtBetweenAndRole(finalDateStart,
                                                        finalDateEnd, role)
                                        : accountRepository.findAllByCreateAtBetweenAndRoleAndGender(
                                                        finalDateStart,
                                                        finalDateEnd, role,
                                                        gender);
                } else {
                        pageAccount = accountRepository
                                        .findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        gender, role, search, search, search, search, finalDateStart,
                                                        finalDateEnd);
                }
                return pageAccount;
        }

        // public Page<Account> getPageShopByBillFinalAt(Date dateStart, Date dateEnd,
        // String search, Boolean gender, Pageable pageable) {
        // Page<Account> pageAccount;

        // Date finalDateStart = getDateStart(dateStart);
        // Date finalDateEnd = getDateEnd(dateEnd);

        // if (search == null || search.isBlank()) {
        // pageAccount = (gender == null)
        // ? billRepository.selectAllByShopAndFinishAt(finalDateStart, finalDateEnd,
        // pageable)
        // : billRepository.selectAllByShopAndGenderFinishAt(finalDateStart,
        // finalDateEnd, gender, pageable);
        // } else {
        // pageAccount = billRepository
        // .findAllShopByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
        // finalDateStart, finalDateEnd, gender, search, search, search,
        // search, pageable);
        // }
        // return pageAccount;
        // }

        public Page<Account> getPageShopByProductCreateAt(Date dateStart, Date dateEnd,
                        String search, Boolean gender, Pageable pageable) {
                Page<Account> pageAccount;

                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);

                if (search == null || search.isBlank()) {
                        pageAccount = (gender == null)
                                        ? productRepository.selectAllByProductAndCreateAt(finalDateStart, finalDateEnd,
                                                        pageable)
                                        : productRepository.selectAllByProductAndGenderFinishAt(finalDateStart,
                                                        finalDateEnd, gender, pageable);
                } else {
                        pageAccount = productRepository
                                        .findAllByProductCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        finalDateStart, finalDateEnd, gender, search, search, search,
                                                        search, pageable);
                }
                return pageAccount;
        }

        public List<Account> getPageShopByProductCreateAt(Date dateStart, Date dateEnd,
                        String search, Boolean gender) {
                List<Account> list;

                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);

                if (search == null || search.isBlank()) {
                        list = (gender == null)
                                        ? productRepository.selectAllByProductAndCreateAt(finalDateStart, finalDateEnd)
                                        : productRepository.selectAllByProductAndGenderFinishAt(finalDateStart,
                                                        finalDateEnd, gender);
                } else {
                        list = productRepository
                                        .findAllByProductCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        finalDateStart, finalDateEnd, gender, search, search, search,
                                                        search);
                }
                return list;
        }

        public Page<Account> getPageShopByReportCreateAt(Date dateStart, Date dateEnd,
                        String search, Boolean gender, Pageable pageable) {
                Page<Account> pageAccount;

                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);

                if (search == null || search.isBlank()) {
                        pageAccount = (gender == null)
                                        ? accountReportRepository.selectAllByProductAndCreateAt(finalDateStart,
                                                        finalDateEnd,
                                                        pageable)
                                        : accountReportRepository.selectAllByProductAndGenderFinishAt(finalDateStart,
                                                        finalDateEnd, gender, pageable);
                } else {
                        pageAccount = accountReportRepository
                                        .findAllByProductCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        finalDateStart, finalDateEnd, gender, search, search, search,
                                                        search, pageable);
                }
                return pageAccount;
        }

        public List<Account> getPageShopByReportCreateAt(Date dateStart, Date dateEnd,
                        String search, Boolean gender) {
                List<Account> list;

                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);

                if (search == null || search.isBlank()) {
                        list = (gender == null)
                                        ? accountReportRepository.selectAllByProductAndCreateAt(finalDateStart,
                                                        finalDateEnd)
                                        : accountReportRepository.selectAllByProductAndGenderFinishAt(finalDateStart,
                                                        finalDateEnd, gender);
                } else {
                        list = accountReportRepository
                                        .findAllByProductCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                        finalDateStart, finalDateEnd, gender, search, search, search,
                                                        search);
                }
                return list;
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
