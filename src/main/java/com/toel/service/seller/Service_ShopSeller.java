package com.toel.service.seller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.toel.dto.seller.request.Request_Account;
import com.toel.dto.seller.response.Response_Account;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.ShopMapper;
import com.toel.model.Account;
import com.toel.repository.AccountRepository;

import com.toel.repository.FollowerRepository;
import com.toel.repository.ProductRepository;

import com.toel.service.firebase.DeleteImage;
import com.toel.service.firebase.UploadImage;


@Service
public class Service_ShopSeller {
    @Autowired
    ShopMapper accountMapper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    DeleteImage deleteImage;
    @Autowired
    UploadImage uploadImage;

    @Autowired
    FollowerRepository followerRepository ;

    @Autowired
    ProductRepository productRepository ;

    public Response_Account get(Integer idAccount) {
        return accountMapper.response_Account(accountRepository.findById(idAccount)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account")));
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


    


    public boolean saveImage(Integer account_id, MultipartFile avatar, MultipartFile backgroud) {
        try {
            Account account = accountRepository.findById(account_id)
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account"));
            deleteImage.deleteFileByUrl(account.getAvatar());
            deleteImage.deleteFileByUrl(account.getBackground());
            account.setAvatar(uploadImage.uploadFile("avatar", avatar));
            account.setBackground(uploadImage.uploadFile("background", backgroud));
            accountRepository.saveAndFlush(account);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

