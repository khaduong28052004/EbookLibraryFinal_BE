package com.toel.service.seller;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.seller.request.Request_Evalue;
import com.toel.dto.seller.response.Response_Evalue;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.EvalueMapper;
import com.toel.model.Evalue;
import com.toel.repository.AccountRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.ProductRepository;

@Service
public class Service_EvalueSeller {
        @Autowired
        EvalueMapper evalueMapper;
        @Autowired
        EvalueRepository evalueRepository;
        @Autowired
        AccountRepository accountRepository;
        @Autowired
        ProductRepository productRepository;

        public PageImpl<Response_Evalue> getAll(
                        Integer page, Integer size, boolean sortBy, String sortColum, Integer account_id,
                        String search) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColum));
                Page<Evalue> pageEvalue = evalueRepository.findByAccountId(account_id, search, pageable);
                return new PageImpl<>(pageEvalue.stream()
                                .map(evlue -> evalueMapper.response_Evalue(evlue))
                                .collect(Collectors.toList()), pageable, pageEvalue.getTotalElements());
        }

        public Response_Evalue phanHoi(Request_Evalue request_Evalue) {
                Evalue evalue = evalueMapper.evalue(request_Evalue);
                evalue.setAccount(accountRepository.findById(request_Evalue.getAccount())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account")));
                evalue.setProduct(productRepository.findById(request_Evalue.getProduct())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Product")));
                evalue.setCreateAt(new Date());
                return evalueMapper.response_Evalue(evalueRepository.saveAndFlush(evalue));
        }
}
