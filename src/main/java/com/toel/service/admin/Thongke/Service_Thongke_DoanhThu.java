package com.toel.service.admin.Thongke;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.response.ThongKe.Response_TKDT_Seller;
import com.toel.mapper.AccountMapper;
import com.toel.model.Account;
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
                roleRepository.findByNameIgnoreCase("seller");
                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);
                List<Account> pageAccount = billRepository.findByFinishAtBetweenAndGenderAndSearch(finalDateStart,
                                finalDateEnd, gender, search);
                List<Response_TKDT_Seller> list = pageAccount.stream()
                                .map(account -> calculateSellerRevenue(account, finalDateStart, finalDateEnd))
                                .collect(Collectors.toList());
                Comparator<Response_TKDT_Seller> comparator = getComparator(sortColumn, sortBy);
                list.sort(comparator);
                Pageable pageable = PageRequest.of(page, size);
                int start = (int) pageable.getOffset();
                int end = Math.min(start + pageable.getPageSize(), list.size());
                List<Response_TKDT_Seller> paginatedList = list.subList(start, end);
                return new PageImpl<>(paginatedList, pageable, list.size());
        }

        private Comparator<Response_TKDT_Seller> getComparator(String sortColumn, Boolean sortBy) {
                Comparator<Response_TKDT_Seller> comparator;

                switch (sortColumn.toLowerCase()) {
                        case "dtshop":
                                comparator = Comparator.comparing(Response_TKDT_Seller::getDTshop);
                                break;
                        case "dtsan":
                                comparator = Comparator.comparing(Response_TKDT_Seller::getDTSan);
                                break;
                        case "phi":
                                comparator = Comparator.comparing(Response_TKDT_Seller::getPhi);
                                break;
                        case "loinhuan":
                                comparator = Comparator.comparing(Response_TKDT_Seller::getLoiNhuan);
                                break;
                        case "id":
                                comparator = Comparator.comparing(Response_TKDT_Seller::getId);
                                break;
                        case "fullname":
                                comparator = Comparator.comparing(Response_TKDT_Seller::getFullname);
                                break;
                        case "shopname":
                                comparator = Comparator.comparing(Response_TKDT_Seller::getShopName);
                                break;
                        default:
                                throw new IllegalArgumentException("Invalid sort column: " + sortColumn);
                }

                return sortBy ? comparator.reversed() : comparator;
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
