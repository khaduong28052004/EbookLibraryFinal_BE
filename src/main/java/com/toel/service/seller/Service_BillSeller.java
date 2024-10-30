package com.toel.service.seller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.seller.request.Request_Bill;
import com.toel.dto.seller.response.Response_Bill;
import com.toel.mapper.seller.Seller_BillMapper;
import com.toel.model.Bill;
import com.toel.model.OrderStatus;
import com.toel.repository.BillRepository;
import com.toel.repository.OrderStatusRepository;

@Service
public class Service_BillSeller {
    @Autowired
    Seller_BillMapper billMapper;
    @Autowired
    BillRepository billRepository;
    @Autowired
    OrderStatusRepository orderStatusRepository;

    public PageImpl<Response_Bill> getAll(Integer page, Integer size, boolean sortBy, String sortColumn,
            Integer account_id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC));
        Page<Bill> pageBill = billRepository.findAllByShopId(account_id, pageable);
        List<Response_Bill> list = pageBill.stream()
                .map(bill -> billMapper.response_Bill(bill))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageBill.getTotalElements());
    }

    public Response_Bill updateOrderStatus(Request_Bill request_Bill) {
        Bill bill = billMapper.bill(request_Bill);
        Optional<OrderStatus> orderStatus = orderStatusRepository.findById(bill.getOrderStatus().getId() + 1);
        bill.setOrderStatus(orderStatus.get());
        return billMapper.response_Bill(billRepository.saveAndFlush(bill));
    }

    public Response_Bill huy(Request_Bill request_Bill) {
        Bill bill = billMapper.bill(request_Bill);
        Optional<OrderStatus> orderStatus = orderStatusRepository.findById(7);
        bill.setOrderStatus(orderStatus.get());
        return billMapper.response_Bill(billRepository.saveAndFlush(bill));
    }
}
