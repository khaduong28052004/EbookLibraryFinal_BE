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

import com.toel.dto.admin.response.ThongKe.Response_TKDT_Seller;
import com.toel.mapper.AccountMapper;
import com.toel.model.Account;
import com.toel.model.Role;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.LikeRepository;
import com.toel.repository.ProductRepository;
import com.toel.repository.RoleRepository;

@Service
public class Service_Thongke_DoanhThu {
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
        AccountMapper accountMapper;

        public PageImpl<Response_TKDT_Seller> get_TKDT_Seller(Date dateStart, Date dateEnd,
                        String search, Boolean gender, Integer page, Integer size, Boolean sortBy, String sortColumn) {

                Role role = roleRepository.findByNameIgnoreCase("Seller");
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Sort.Direction.DESC : Sort.Direction.ASC, sortColumn));
                Page<Account> pageAccount = null;
                Calendar calStart = Calendar.getInstance();
                calStart.set(Calendar.DAY_OF_MONTH, 1);
                Date finalDateStart = (dateStart == null) ? calStart.getTime() : dateStart;
                Calendar calEnd = Calendar.getInstance();
                calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date finalDateEnd = (dateEnd == null) ? calEnd.getTime() : dateEnd;
                if (search == null || search.isBlank()) {
                        pageAccount = (gender == null)
                                        ? accountRepository.findAllByRole(role, pageable)
                                        : accountRepository.findAllByRoleAndGender(role, gender, pageable);
                } else {
                        pageAccount = (gender == null)
                                        ? accountRepository
                                                        .findAllByUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContainingAndRole(
                                                                        search, search, search, search, role, pageable)
                                        : accountRepository
                                                        .findAllByGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                                                                        gender, role, search, search, search, search,
                                                                        pageable);
                }

                List<Response_TKDT_Seller> list = pageAccount.stream()
                                .map(account -> {
                                        Response_TKDT_Seller accountnew = accountMapper.tResponse_TKDT_Seller(account);
                                        accountnew.setDTshop(
                                                        billDetailRepository.calculateAverageBillByShop(account.getId(),
                                                                        finalDateStart, finalDateEnd));
                                        System.out.println("Doanh thu shop: " + accountnew.getDTshop());
                                        accountnew.setDTSan(
                                                        billDetailRepository.calculateChietKhauByShop_San(
                                                                        account.getId(),
                                                                        finalDateStart, finalDateEnd));
                                        System.out.println("Doanh thu sàn: " + accountnew.getDTSan());
                                        accountnew.setPhi(billRepository.calculateVoucherByShop_San(account.getId(),
                                                        finalDateStart, finalDateEnd));
                                        System.out.println("Phí: " + accountnew.getPhi());
                                        accountnew.setLoiNhuan(
                                                        billDetailRepository.calculateChietKhauByShop_San(
                                                                        account.getId(), finalDateStart, finalDateEnd)
                                                                        - billRepository.calculateVoucherByShop_San(
                                                                                        account.getId(), finalDateStart,
                                                                                        finalDateEnd));
                                        System.out.println("Lợi nhuận: " + accountnew.getLoiNhuan());
                                        return accountnew;
                                })
                                .collect(Collectors.toList());

                return new PageImpl<>(list, pageable, pageAccount.getTotalElements());
        }

        // public PageImpl<Response_TK_Bill> getAll_TK_DonHang(LocalDate dateStart,
        // LocalDate dateEnd, Integer page,
        // Integer size, Boolean sortBy, String sortColumn) {
        // Pageable pageable = PageRequest.of(page, size,
        // Sort.by(sortBy ? Sort.Direction.DESC : Sort.Direction.ASC, sortColumn));
        // Page<Bill> pageBill;

        // }

}
