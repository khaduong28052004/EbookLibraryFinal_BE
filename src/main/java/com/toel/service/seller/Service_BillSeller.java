package com.toel.service.seller;

import java.util.Date;
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
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.BillMapper;
import com.toel.model.Account;
import com.toel.model.Bill;
import com.toel.model.OrderStatus;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.OrderStatusRepository;

@Service
public class Service_BillSeller {
    @Autowired
    BillMapper billMapper;
    @Autowired
    BillRepository billRepository;
    @Autowired
    OrderStatusRepository orderStatusRepository;
    @Autowired
    AccountRepository accountRepository;

    public PageImpl<Response_Bill> getAll(Integer page, Integer size, boolean sortBy, String sortColumn,
            Integer account_id, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<Bill> pageBill = billRepository.findAllByShopId(account_id, search, pageable);
        List<Response_Bill> list = pageBill.stream()
                .map(bill -> billMapper.response_Bill(bill))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageBill.getTotalElements());
    }

    public Response_Bill updateOrderStatus(Request_Bill request_Bill) {
        Bill bill = billRepository.findById(request_Bill.getId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Bill"));
        bill.setOrderStatus(orderStatusRepository.findById(request_Bill.getOrderStatus() + 1)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "OrderStatus")));
        bill.setUpdateAt(new Date());
        return billMapper.response_Bill(billRepository.saveAndFlush(bill));
    }

    public Response_Bill huy(Request_Bill request_Bill) {
        Bill bill = billRepository.findById(request_Bill.getId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Bill"));
        bill.setOrderStatus(orderStatusRepository.findById(6)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "OrderStatus")));
        bill.setUpdateAt(new Date());
        return billMapper.response_Bill(billRepository.saveAndFlush(bill));
    }

    public String QuantityBillStatus(Integer idOrder, Integer idAccount) {
        try {
            Optional<OrderStatus> orderOptional = orderStatusRepository.findById(idOrder);
            if (!orderOptional.isPresent()) {   
                return "0";      // return "Order status not found";
            }
            OrderStatus order = orderOptional.get();          // Check if Account exists
            Optional<Account> accountOptional = accountRepository.findById(idAccount);
            if (!accountOptional.isPresent()) {
                return "0";         // return "Account not found";
            }
            Account account = accountOptional.get();
            List<Bill> bills = billRepository.findByOrderStatusAndAccount(order, account);
            if (bills.isEmpty()) {
                return "0";        // return "NULL"; // No bills found for the given criteria
            } 
            return Integer.toString(bills.size());// Return the quantity as a string
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "0";
        }
    }

    public String QuantityBillStatus(Integer idOder) {
        try {
            OrderStatus order = orderStatusRepository.findById(idOder).get();
            List<Bill> bill = billRepository.findByOrderStatus(order);
            if (bill.isEmpty()) {
                return "CHUA CO";
            }
            Integer quantity = bill.size();
            return Integer.toString(quantity);
        } catch (Exception e) {
            System.out.println("lor " + e);
            return "ERROR";
        }
    }

    // public String quantityBillStatus(Integer idOrder) {
    // OrderStatus order = orderStatusRepository.findById(idOrder).orElse(null);

    // if (order == null) {
    // return "0"; // Return 0 if the order is not found
    // }

    // // Filter the bills based on a condition (example condition: check if the
    // bill is active)
    // long count = billRepository.findByOrderStatus(order).stream()
    // .filter(bill -> bill.) // Replace `isActive` with your actual condition
    // .count();

    // return Long.toString(count);
    // }

}
