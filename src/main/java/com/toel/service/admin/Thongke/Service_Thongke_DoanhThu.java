package com.toel.service.admin.Thongke;

import java.text.DecimalFormat;
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
                        String search, Boolean gender, int page, int size,
                        Boolean sortBy, String sortColumn) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Sort.Direction.DESC : Sort.Direction.ASC, sortColumn));
                Role role = roleRepository.findByNameIgnoreCase("seller");

                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);
                Page<Account> pageAccount = billRepository.findByFinishAtBetweenAndGenderAndSearch(finalDateStart,
                                finalDateEnd, gender, search, pageable);
                List<Response_TKDT_Seller> list = pageAccount.stream()
                                .map(account -> calculateSellerRevenue(account, finalDateStart, finalDateEnd))
                                .collect(Collectors.toList());

                return new PageImpl<>(list, pageable, pageAccount.getTotalElements());
        }

        private Response_TKDT_Seller calculateSellerRevenue(Account account, Date startDate, Date endDate) {
                Response_TKDT_Seller response = accountMapper.tResponse_TKDT_Seller(account);
                // DecimalFormat decimalFormat = new DecimalFormat("#.##");

                double doanhThuShop = billDetailRepository.calculateAverageBillByShop(account.getId(), startDate,
                                endDate);
                double doanhThuSan = billDetailRepository.calculateChietKhauByShop_San(account.getId(), startDate,
                                endDate);
                double phi = billRepository.calculateVoucherByShop_San(account.getId(),
                                startDate, endDate);
                double loiNhuan = doanhThuSan - phi;

                response.setDTshop(doanhThuShop);
                response.setDTSan(doanhThuSan);
                response.setPhi(phi);
                response.setLoiNhuan(loiNhuan);
                return response;
        }

        public double[] calculateMonthlyRevenue(Date dateStart, Date dateEnd, String search, Boolean gender) {
                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);
                double tongShop = 0, doanhThuSan = 0, phi = 0, loiNhuan = 0;
                tongShop = billRepository
                                .findByFinishAtBetweenAndGenderAndSearch(finalDateStart, finalDateEnd, gender, search)
                                .size();
                for (Account account : billRepository.findByFinishAtBetweenAndGenderAndSearch(finalDateStart,
                                finalDateEnd, gender, search)) {
                        doanhThuSan += billDetailRepository.calculateChietKhauByShop_San(account.getId(),
                                        finalDateStart,
                                        finalDateEnd);
                        phi += billRepository.calculateVoucherByShop_San(account.getId(), finalDateStart, finalDateEnd);
                }
                loiNhuan = doanhThuSan - phi;

                return new double[] { tongShop, doanhThuSan, phi, loiNhuan };
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
