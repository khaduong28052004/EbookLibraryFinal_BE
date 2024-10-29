package com.toel.service.seller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.seller.response.Response_Bill;
import com.toel.mapper.seller.BillMapper;
import com.toel.model.Bill;
import com.toel.repository.BillRepository;

@Service
public class Service_Bill {
    @Autowired
    BillMapper billMapper;
    @Autowired
    BillRepository billRepository;

    public PageImpl<Response_Bill> getAll(Integer page, Integer size, boolean sortBy, String sortColumn,
            Integer account_id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC));
        Page<Bill> pageBill = billRepository.findAllByShopId(account_id, pageable);
        List<Response_Bill> list = pageBill.stream()
                .map(bill -> billMapper.response_Bill(bill))
                .collect(Collectors.toList());

        return new PageImpl<>(list, pageable, pageBill.getTotalElements());
    }
}
