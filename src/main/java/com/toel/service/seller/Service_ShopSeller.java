package com.toel.service.seller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.seller.request.Request_Account;
import com.toel.dto.seller.response.Response_Account;
import com.toel.mapper.ShopMapper;
import com.toel.repository.AccountRepository;
import com.toel.repository.FollowerRepository;
import com.toel.repository.ProductRepository;

@Service
public class Service_ShopSeller {
    @Autowired
    ShopMapper accountMapper;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    FollowerRepository followerRepository ;

    @Autowired
    ProductRepository productRepository ;

    public Response_Account get(Integer idAccount) {
        return accountMapper.response_Account(accountRepository.findById(idAccount).get());
    }

    public Response_Account save(Request_Account request_Account) {
        return accountMapper.response_Account(accountRepository.saveAndFlush(accountMapper.account(request_Account)));
    }

    public Integer countFollowersByShopId(Integer shopId) {
        return followerRepository.countFollowersByShopId(shopId);
    }
    public Integer countFollowingByAccountId(Integer accountId) {
        return followerRepository.countFollowingByAccountId(accountId);
    }


    
}