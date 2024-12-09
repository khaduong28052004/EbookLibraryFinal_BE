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

import com.toel.dto.admin.response.ThongKe.Page_TK_Bill;
import com.toel.dto.admin.response.ThongKe.Response_TK_Bill;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.BillMapper;
import com.toel.model.Bill;
import com.toel.model.OrderStatus;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.OrderStatusRepository;

@Service
public class Service_ThongKe_DonHang {
        @Autowired
        BillRepository billRepository;
        @Autowired
        BillDetailRepository billDetailRepository;
        @Autowired
        BillMapper billMapper;
        @Autowired
        OrderStatusRepository orderStatusRepository;

        public Page_TK_Bill get_TKDT_DonHang(Date dateStart, Date dateEnd, Integer orderStatusId,
                        Integer page, Integer size, Boolean sortBy, String sortColumn) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Sort.Direction.DESC : Sort.Direction.ASC, sortColumn));
                Date finalDateStart = getDateStart(dateStart);
                Date finalDateEnd = getDateEnd(dateEnd);
                Page<Bill> pageBill;
                OrderStatus orderStatus = (orderStatusId != null) ? orderStatusRepository.findById(orderStatusId)
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Trạng thái")) : null;
                if (orderStatus == null) {
                        pageBill = billRepository.selectAllByCreateAtBetweenAndOrderStatus(finalDateStart, finalDateEnd,
                                        pageable);
                } else if (orderStatus.getId() == 5) {
                        pageBill = billRepository.selectAllByFinishAtBetweenAndOrderStatus(
                                        finalDateStart, finalDateEnd, orderStatus.getId(), pageable);
                        System.out.println("hoàn thành");
                } else if (orderStatus.getId() == 1) {
                        pageBill = billRepository.selectAllByCreateAtBetweenAndOrderStatus(
                                        finalDateStart, finalDateEnd, orderStatus.getId(), pageable);
                        System.out.println("chờ duyệt");
                } else {
                        pageBill = billRepository.selectAllByUpdateAtBetweenAndOrderStatus(
                                        finalDateStart, finalDateEnd, orderStatus.getId(), pageable);
                        System.out.println("Trạng thái: " + orderStatus.getName());
                }
                List<Response_TK_Bill> list = pageBill.stream()
                                .map(billMapper::toResponse_TK_Bill)
                                .collect(Collectors.toList());

                Integer tongDangChoXuLy = billRepository.findAllByUpdateAtBetweenAndOrderStatus(finalDateStart,
                                finalDateEnd, orderStatusRepository.findById(2).get()).size();
                Integer tongDangGiao = billRepository.findAllByUpdateAtBetweenAndOrderStatus(finalDateStart,
                                finalDateEnd, orderStatusRepository.findById(3).get()).size();
                Integer tongHoanThanh = billRepository.findAllByFinishAtBetweenAndOrderStatus(finalDateStart,
                                finalDateEnd, orderStatusRepository.findById(5).get().getId()).size();
                Integer tongHuy = billRepository.findAllByUpdateAtBetweenAndOrderStatus(finalDateStart,
                                finalDateEnd, orderStatusRepository.findById(6).get()).size();

                Page_TK_Bill page_TK_Bill = Page_TK_Bill.builder()
                                .thongke(new PageImpl<>(list, pageable, pageBill.getTotalElements()))
                                .tongDangChoXuLy(tongDangChoXuLy)
                                .tongDangGiao(tongDangGiao)
                                .tongHoanThanh(tongHoanThanh)
                                .tongHuy(tongHuy)
                                .build();
                return page_TK_Bill;
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
