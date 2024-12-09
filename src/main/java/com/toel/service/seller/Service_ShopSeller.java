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


// public boolean saveImage(Integer account_id, MultipartFile avatar, MultipartFile background) {
//     try {
//         // Tìm tài khoản theo ID
//         Account account = accountRepository.findById(account_id)
//                 .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Tài khoản không tồn tại"));

//         boolean isUpdated = false;

//         // Xử lý avatar
//         if (avatar != null && !avatar.isEmpty()) {
//             if (account.getAvatar() != null) {
//                 deleteImage.deleteFileByUrl(account.getAvatar()); // Xoá ảnh cũ
//             }
//             account.setAvatar(uploadImage.uploadFile("avatars/", avatar)); // Lưu ảnh mới
//             isUpdated = true;
//         }

//         // Xử lý background
//         if (background != null && !background.isEmpty()) {
//             if (account.getBackground() != null) {
//                 deleteImage.deleteFileByUrl(account.getBackground()); // Xoá ảnh cũ
//             }
//             account.setBackground(uploadImage.uploadFile("backgrounds/", background)); // Lưu ảnh mới
//             isUpdated = true;
//         }

//         // Nếu ít nhất một ảnh được cập nhật, lưu thay đổi
//         if (isUpdated) {
//             // Lưu cập nhật vào database
//             accountRepository.saveAndFlush(account);
//             return true;
//         }

//         // Nếu không có ảnh nào được gửi lên
//         throw new AppException(ErrorCode.INVALID_REQUEST, "Không có ảnh nào được gửi lên để cập nhật");

//     } catch (IOException e) {
//         e.printStackTrace();
//         return false; // Xử lý lỗi trong trường hợp gặp vấn đề
//     }
// }

public boolean saveImage(Integer account_id, MultipartFile avatar, MultipartFile background) {
    try {
        // Tìm tài khoản theo ID
        Account account = accountRepository.findById(account_id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Tài khoản không tồn tại"));

        boolean isUpdated = false;

        // Xử lý avatar
        if (avatar != null && !avatar.isEmpty()) {
            if (account.getAvatar() != null) {
                deleteImage.deleteFileByUrl(account.getAvatar()); // Xoá ảnh cũ
            }
            account.setAvatar(uploadImage.uploadFile("avatars/", avatar)); // Lưu ảnh mới
            isUpdated = true;
        }

        // Xử lý background
        if (background != null && !background.isEmpty()) {
            if (account.getBackground() != null) {
                deleteImage.deleteFileByUrl(account.getBackground()); // Xoá ảnh cũ
            }
            account.setBackground(uploadImage.uploadFile("backgrounds/", background)); // Lưu ảnh mới
            isUpdated = true;
        }

        // Lưu thay đổi chỉ khi có cập nhật
        if (isUpdated) {
            accountRepository.saveAndFlush(account);
        }

        return isUpdated;

    } catch (IOException e) {
        e.printStackTrace();
        throw new AppException(ErrorCode.OBJECT_SETUP, "Có lỗi xảy ra trong quá trình xử lý ảnh");
    }
}

}

