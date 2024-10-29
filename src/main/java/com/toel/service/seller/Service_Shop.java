package com.toel.service.seller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.seller.request.Request_Account;
import com.toel.dto.seller.response.Response_Account;
import com.toel.mapper.seller.AccountMapper;
import com.toel.model.Account;
import com.toel.repository.AccountRepository;

@Service
public class Service_Shop {
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    AccountRepository accountRepository;

    public Response_Account get(Integer idAccount) {
        return accountMapper.response_Account(accountRepository.findById(idAccount).get());
    }

    public Response_Account save(Request_Account request_Account) {
        Account account = accountMapper.account(request_Account);
        return accountMapper.response_Account(accountRepository.saveAndFlush(account));
    }
}
