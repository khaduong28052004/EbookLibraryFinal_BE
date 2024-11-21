package com.toel.service.admin.Thongke;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                Role role = roleRepository.findByNameIgnoreCase("seller");
                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);
                // Page<Account> pageAccount = null;
                List<Account> listAccount;
                if (option.equalsIgnoreCase("macdinh")) {
                        listAccount = getPage(role, search, gender);
                } else if (option.equalsIgnoreCase("shopmoi")) {
                        listAccount = getPageByCreateAtSeller(role, dateStart, dateEnd, search,
                                        gender);
                } else if (option.equalsIgnoreCase("donhang")) {
                        listAccount = billRepository.findByFinishAtBetweenAndGenderAndSearch(
                                        finalDateStart,
                                        finalDateEnd, gender, search);
                } else if (option.equalsIgnoreCase("sanpham")) {
                        listAccount = getPageShopByProductCreateAt(finalDateStart, finalDateEnd,
                                        search, gender);
                } else if (option.equalsIgnoreCase("baocao")) {
                        listAccount = getPageShopByReportCreateAt(finalDateStart, finalDateEnd,
                                        search, gender);
                } else {
                        listAccount = getPageByCreateAtSeller(role, dateStart, dateEnd, search,
                                        gender);
                }

                Integer totalShop = listAccount.size();
                Integer tongSP = 0;
                Integer tongTheoDoi = 0;
                Integer tongBaoCao = 0;

                List<Response_TK_Seller> list = listAccount.stream()
                                .map(account -> calculateTk_Seller(account, finalDateStart, finalDateEnd))
                                .collect(Collectors.toList());
                for (Response_TK_Seller account : list) {
                        tongSP += account.getSumProduct();
                        tongTheoDoi += account.getSumFollower();
                        tongBaoCao += account.getSumReport();
                }
                Comparator<Response_TK_Seller> comparator = getComparator(sortColumn, sortBy);
                list.sort(comparator);
                Pageable pageable = PageRequest.of(page, size);
                int start = (int) pageable.getOffset();
                int end = Math.min(start + pageable.getPageSize(), list.size());
                List<Response_TK_Seller> paginatedList = list.subList(start, end);
                Page_TK_Seller page_TK_Seller = new Page_TK_Seller();

                page_TK_Seller.setTongShop(totalShop);
                page_TK_Seller.setTongSP(tongSP);
                page_TK_Seller.setTongTheoDoi(tongTheoDoi);
                page_TK_Seller.setTongBaoCao(tongBaoCao);
                page_TK_Seller.setThongke(new PageImpl<>(paginatedList, pageable, listAccount.size()));

                return page_TK_Seller;

        }

        private Comparator<Response_TK_Seller> getComparator(String sortColumn, Boolean sortBy) {
                Comparator<Response_TK_Seller> comparator;
                switch (sortColumn.toLowerCase()) {
                        case "id":
                                comparator = Comparator.comparing(Response_TK_Seller::getId);
                                break;
                        case "shopname":
                                comparator = Comparator.comparing(Response_TK_Seller::getShopName);
                                break;
                        case "sumfollower":
                                comparator = Comparator.comparing(Response_TK_Seller::getSumFollower);
                                break;
                        case "sumproduct":
                                comparator = Comparator.comparing(Response_TK_Seller::getSumProduct);
                                break;
                        case "sumreport":
                                comparator = Comparator.comparing(Response_TK_Seller::getSumReport);
                                break;
                        case "avgstar":
                                comparator = Comparator.comparing(Response_TK_Seller::getAvgStar);
                                break;
                        default:
                                throw new IllegalArgumentException("Invalid sort column: " + sortColumn);
                }
                return sortBy ? comparator.reversed() : comparator;
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
