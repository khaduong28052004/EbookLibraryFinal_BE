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

import com.toel.dto.seller.request.Request_Evalue;
import com.toel.dto.seller.response.Response_Evalue;
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
            Integer page, Integer size, boolean sortBy, String sortColum, Integer account_id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColum));
        Page<Evalue> pageEvalue = evalueRepository.findByAccountId(account_id, pageable);
        List<Response_Evalue> list = pageEvalue.stream()
                .map(evlue -> evalueMapper.response_Evalue(evlue))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageEvalue.getTotalElements());
    }

    public Response_Evalue phanHoi(Request_Evalue request_Evalue) {
        Evalue evalue = evalueMapper.evalue(request_Evalue);
        evalue.setAccount(accountRepository.findById(request_Evalue.getAccount()).get());
        evalue.setProduct(productRepository.findById(request_Evalue.getProduct()).get());
        return evalueMapper.response_Evalue(evalueRepository.saveAndFlush(evalue));
    }
}
