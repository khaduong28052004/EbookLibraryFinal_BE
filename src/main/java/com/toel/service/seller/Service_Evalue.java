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
import com.toel.mapper.seller.EvalueMapper;
import com.toel.model.Evalue;
import com.toel.repository.EvalueRepository;

@Service
public class Service_Evalue {
    @Autowired
    EvalueMapper evalueMapper;
    @Autowired
    EvalueRepository evalueRepository;

    public PageImpl<Response_Evalue> getAll(
            Integer page, Integer size, boolean sortBy, String sortColum, Integer account_id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC));
        Page<Evalue> pageEvalue = evalueRepository.findByAccountId(account_id, pageable);
        List<Response_Evalue> list = pageEvalue.stream()
                .map(evlue -> evalueMapper.response_Evalue(evlue))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageEvalue.getTotalElements());
    }

    public Response_Evalue phanHoi(Request_Evalue request_Evalue) {
        return evalueMapper.response_Evalue(evalueRepository.saveAndFlush(evalueMapper.evalue(request_Evalue)));
    }
}
