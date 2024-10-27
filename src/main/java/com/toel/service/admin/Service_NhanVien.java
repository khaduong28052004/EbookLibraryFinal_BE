package com.toel.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.repository.AccountRepository;

@Service
public class Service_NhanVien {
    @Autowired
    AccountRepository accountRepository;
}
