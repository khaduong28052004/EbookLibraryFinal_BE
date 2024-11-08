// package com.toel.service.admin.Thongke;

// import java.util.Calendar;
// import java.util.Date;
// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.data.domain.Sort.Direction;
// import org.springframework.stereotype.Service;

// import com.toel.dto.admin.response.ThongKe.Response_TKDT_Seller;
// import com.toel.dto.admin.response.ThongKe.Response_TK_Account;
// import com.toel.dto.admin.response.ThongKe.Response_TK_Seller;
// import com.toel.mapper.AccountMapper;
// import com.toel.model.Account;
// import com.toel.model.Role;
// import com.toel.repository.AccountReportRepository;
// import com.toel.repository.AccountRepository;
// import com.toel.repository.BillDetailRepository;
// import com.toel.repository.BillRepository;
// import com.toel.repository.EvalueRepository;
// import com.toel.repository.FollowerRepository;
// import com.toel.repository.LikeRepository;
// import com.toel.repository.ProductRepository;
// import com.toel.repository.RoleRepository;

// @Service
// public class Service_ThongKe_Account {
//         @Autowired
//         AccountRepository accountRepository;
//         @Autowired
//         RoleRepository roleRepository;
//         @Autowired
//         ProductRepository productRepository;
//         @Autowired
//         BillDetailRepository billDetailRepository;
//         @Autowired
//         BillRepository billRepository;
//         @Autowired
//         EvalueRepository evalueRepository;
//         @Autowired
//         LikeRepository likeRepository;
//         @Autowired
//         AccountReportRepository accountReportRepository;
//         @Autowired
//         FollowerRepository followerRepository;
//         @Autowired
//         AccountMapper accountMapper;

//         public PageImpl<Response_TKDT_Seller> get_TKDT_Seller(Date dateStart, Date dateEnd,
//                         String search, Boolean gender, int page, int size,
//                         Boolean sortBy, String sortColumn) {
//                 Pageable pageable = PageRequest.of(page, size,
//                                 Sort.by(sortBy ? Sort.Direction.DESC : Sort.Direction.ASC, sortColumn));
//                 Role role = roleRepository.findByNameIgnoreCase("seller");

//                 Date finalDateStart = getDateStart(dateStart);
//                 Date finalDateEnd = getDateEnd(dateEnd);

//                 Page<Account> pageAccount = getPage(role, search, gender, pageable);
//                 List<Response_TKDT_Seller> list = pageAccount.stream()
//                                 .map(account -> {
//                                         Response_TKDT_Seller accountnew = accountMapper.tResponse_TKDT_Seller(account);
//                                         accountnew.setDTshop(
//                                                         billDetailRepository.calculateAverageBillByShop(account.getId(),
//                                                                         finalDateStart, finalDateEnd));
//                                         System.out.println("Doanh thu shop: " + accountnew.getDTshop());
//                                         accountnew.setDTSan(
//                                                         billDetailRepository.calculateChietKhauByShop_San(
//                                                                         account.getId(),
//                                                                         finalDateStart, finalDateEnd));
//                                         System.out.println("Doanh thu sàn: " + accountnew.getDTSan());
//                                         accountnew.setPhi(billRepository.calculateVoucherByShop_San(account.getId(),
//                                                         finalDateStart, finalDateEnd));
//                                         System.out.println("Phí: " + accountnew.getPhi());
//                                         accountnew.setLoiNhuan(
//                                                         billDetailRepository.calculateChietKhauByShop_San(
//                                                                         account.getId(), finalDateStart, finalDateEnd)
//                                                                         - billRepository.calculateVoucherByShop_San(
//                                                                                         account.getId(), finalDateStart,
//                                                                                         finalDateEnd));
//                                         System.out.println("Lợi nhuận: " + accountnew.getLoiNhuan());
//                                         return accountnew;
//                                 })
//                                 .collect(Collectors.toList());

//                 return new PageImpl<>(list, pageable, pageAccount.getTotalElements());
//         }

//         public PageImpl<Response_TK_Account> get_TK_KhachHang(Date dateStart, Date dateEnd, String option,
//                         String search, Boolean gender, int page, int size,
//                         Boolean sortBy, String sortColumn) {
//                 Pageable pageable = PageRequest.of(page, size,
//                                 Sort.by(sortBy ? Sort.Direction.DESC : Sort.Direction.ASC, sortColumn));
//                 Role role = roleRepository.findByNameIgnoreCase("user");
//                 Page<Account> pageAccount;
//                 if (option.equalsIgnoreCase("macdinh")) {
//                         // account có thể mua hàng hông để chỉnh sửa
//                         pageAccount = getPage(role, search, gender, pageable);
//                 } else if (option.equalsIgnoreCase("khachhangmoi")) {
//                         pageAccount = getPageByCreateAt(role, dateStart, dateEnd, search, gender, pageable);
//                 } else if (option.equalsIgnoreCase("muanhieunhat")) {
//                         pageAccount = getPageKhachHangByBillFinalAt(dateStart, dateEnd, search, gender, pageable);
//                 } else {
//                         pageAccount = getPageByCreateAt(role, dateStart, dateEnd, search, gender, pageable);
//                 }

//                 List<Response_TK_Account> list = pageAccount.stream()
//                                 .map(account -> {
//                                         Response_TK_Account accountnew = accountMapper.tResponse_TK_Account(account);
//                                         int sumBill = billRepository.countByAccount(account) == null ? 0
//                                                         : billRepository.countByAccount(account);
//                                         accountnew.setAvgdonhang(
//                                                         billRepository.calculateAGVTotalPriceByAccount(account));
//                                         System.out.println("Đơn hàng: " + accountnew.getAvgdonhang());
//                                         accountnew.setAvgStar(
//                                                         evalueRepository.calculateAverageStarByKhachHang(account));
//                                         System.out.println("trung bình sao: " + accountnew.getAvgStar());
//                                         accountnew.setSumDonHang(sumBill);
//                                         System.out.println("Tổng đơn hàng: " + accountnew.getSumDonHang());
//                                         return accountnew;
//                                 })
//                                 .collect(Collectors.toList());

//                 return new PageImpl<>(list, pageable, pageAccount.getTotalElements());
//         }

//         public PageImpl<Response_TK_Seller> get_TK_Seller(Date dateStart, Date dateEnd, String option,
//                         String search, Boolean gender, int page, int size,
//                         Boolean sortBy, String sortColumn) {
//                 Pageable pageable = PageRequest.of(page, size,
//                                 Sort.by(sortBy ? Sort.Direction.DESC : Sort.Direction.ASC, sortColumn));
//                 Role role = roleRepository.findByNameIgnoreCase("seller");
//                 Date finalDateStart = getDateStart(dateStart);
//                 Date finalDateEnd = getDateEnd(dateEnd);
//                 Page<Account> pageAccount;
//                 if (option.equalsIgnoreCase("shopmoi")) {
//                         pageAccount = getPageByCreateAtSeller(role, dateStart, dateEnd, search, gender,
//                                         pageable);
//                 } else if (option.equalsIgnoreCase("doanhso")) {
//                         pageAccount = getPageShopByBillFinalAt(finalDateStart, finalDateEnd, search, gender, pageable);
//                 }else if (option.equalsIgnoreCase("sanpham")) {
//                         pageAccount = getPageShopByBillFinalAt(finalDateStart, finalDateEnd, search, gender, pageable);
//                 }
//                 List<Response_TK_Seller> list = pageAccount.stream()
//                                 .map(account -> {
//                                         Double doanhSo = (billRepository.getTongDoanhSo(size,
//                                                         finalDateStart, finalDateEnd) == null)
//                                                                         ? 0
//                                                                         : billRepository.getTongDoanhSo(size,
//                                                                                         finalDateStart, finalDateEnd);
//                                         Double doanhThu = (billRepository.getTongDoanhThu(size,
//                                                         finalDateStart, finalDateEnd) == null)
//                                                                         ? 0
//                                                                         : billRepository.getTongDoanhThu(size,
//                                                                                         finalDateStart, finalDateEnd);
//                                         Response_TK_Seller accountnew = accountMapper
//                                                         .to_TK_Seller(account);
//                                         accountnew.setSumFollower(followerRepository
//                                                         .findAllByShopId(account.getId()).size());
//                                         accountnew.setSumProduct(
//                                                         productRepository.findAllByAccount(account).size());
//                                         accountnew.setSumReport(accountReportRepository.countByCreateAtBetween(
//                                                         finalDateStart, finalDateEnd));
//                                         accountnew.setAvgStar(evalueRepository
//                                                         .calculateAverageStarByAccountId(account.getId()));
//                                         accountnew.setDoanhSo(doanhSo);
//                                         accountnew.setDoanhThu(doanhThu);
//                                         return accountnew;
//                                 })
//                                 .collect(Collectors.toList());
//                 return new PageImpl<>(list, pageable, pageAccount.getTotalElements());

//         }

//         public Page<Account> getPage(Role role, String search, Boolean gender, Pageable pageable) {
//                 Page<Account> pageAccount = null;
//                 if (search == null || search.isBlank()) {
//                         pageAccount = (gender == null)
//                                         ? accountRepository.findAllByRoleAndStatus(role, true, pageable)
//                                         : accountRepository.findAllByRoleAndStatusAndGender(role, true, gender,
//                                                         pageable);
//                 } else {
//                         pageAccount = accountRepository
//                                         .findAllByGenderAndStatusAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
//                                                         gender, true, role, search, search, search, search, pageable);
//                 }
//                 return pageAccount;
//         }

//         public Page<Account> getPageByCreateAt(Role role, Date dateStart, Date dateEnd,
//                         String search, Boolean gender, Pageable pageable) {
//                 Page<Account> pageAccount;

//                 Date finalDateStart = getDateStart(dateStart);
//                 Date finalDateEnd = getDateEnd(dateEnd);

//                 if (search == null || search.isBlank()) {
//                         pageAccount = (gender == null)
//                                         ? accountRepository.findAllByCreateAtBetweenAndRole(finalDateStart,
//                                                         finalDateEnd, role, pageable)
//                                         : accountRepository.findAllByCreateAtBetweenAndRoleAndGender(finalDateStart,
//                                                         finalDateEnd, role,
//                                                         gender, pageable);
//                 } else {
//                         pageAccount = accountRepository
//                                         .findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
//                                                         gender, role, search, search, search, search, finalDateStart,
//                                                         finalDateEnd, pageable);
//                 }
//                 return pageAccount;
//         }

//         public Page<Account> getPageKhachHangByBillFinalAt(Date dateStart, Date dateEnd,
//                         String search, Boolean gender, Pageable pageable) {
//                 Page<Account> pageAccount;

//                 Date finalDateStart = getDateStart(dateStart);
//                 Date finalDateEnd = getDateEnd(dateEnd);

//                 if (search == null || search.isBlank()) {
//                         pageAccount = (gender == null)
//                                         ? billRepository.selectAllByAccountAndFinishAt(finalDateStart, finalDateEnd,
//                                                         pageable)
//                                         : billRepository.selectAllByAccountAndGenderFinishAt(finalDateStart,
//                                                         finalDateEnd, gender, pageable);
//                 } else {
//                         pageAccount = billRepository
//                                         .findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
//                                                         finalDateStart, finalDateEnd, gender, search, search, search,
//                                                         search, pageable);
//                 }
//                 return pageAccount;
//         }

//         public Page<Account> getPageByCreateAtSeller(Role role, Date dateStart, Date dateEnd,
//                         String search, Boolean gender, Pageable pageable) {
//                 Page<Account> pageAccount;

//                 Date finalDateStart = getDateStart(dateStart);
//                 Date finalDateEnd = getDateEnd(dateEnd);

//                 if (search == null || search.isBlank()) {
//                         pageAccount = (gender == null)
//                                         ? accountRepository.findAllByCreateAtSellerBetweenAndRole(finalDateStart,
//                                                         finalDateEnd, role, pageable)
//                                         : accountRepository.findAllByCreateAtSellerBetweenAndRoleAndGender(
//                                                         finalDateStart,
//                                                         finalDateEnd, role,
//                                                         gender, pageable);
//                 } else {
//                         pageAccount = accountRepository
//                                         .findAllByCreateAtSellerBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
//                                                         gender, role, search, search, search, search, finalDateStart,
//                                                         finalDateEnd, pageable);
//                 }
//                 return pageAccount;
//         }

//         public Page<Account> getPageShopByBillFinalAt(Date dateStart, Date dateEnd,
//                         String search, Boolean gender, Pageable pageable) {
//                 Page<Account> pageAccount;

//                 Date finalDateStart = getDateStart(dateStart);
//                 Date finalDateEnd = getDateEnd(dateEnd);

//                 if (search == null || search.isBlank()) {
//                         pageAccount = (gender == null)
//                                         ? billRepository.selectAllByShopAndFinishAt(finalDateStart, finalDateEnd,
//                                                         pageable)
//                                         : billRepository.selectAllByShopAndGenderFinishAt(finalDateStart,
//                                                         finalDateEnd, gender, pageable);
//                 } else {
//                         pageAccount = billRepository
//                                         .findAllShopByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
//                                                         finalDateStart, finalDateEnd, gender, search, search, search,
//                                                         search, pageable);
//                 }
//                 return pageAccount;
//         }

//         public Date getDateStart(Date dateStart) {
//                 Calendar calStart = Calendar.getInstance();
//                 calStart.set(Calendar.DAY_OF_MONTH, 1);
//                 Date finalDateStart = (dateStart == null) ? calStart.getTime() : dateStart;
//                 return finalDateStart;
//         }

//         public Date getDateEnd(Date dateEnd) {
//                 Calendar calEnd = Calendar.getInstance();
//                 calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
//                 Date finalDateEnd = (dateEnd == null) ? calEnd.getTime() : dateEnd;
//                 return finalDateEnd;
//         }
// }
