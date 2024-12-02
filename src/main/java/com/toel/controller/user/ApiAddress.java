package com.toel.controller.user;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.model.Account;
import com.toel.model.Address;
import com.toel.repository.AccountRepository;
import com.toel.repository.AddressRepository;
import com.toel.service.Service_Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/user") // link này kkhoong ch
public class ApiAddress {
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    Service_Session sessionService;

    @Autowired
    AccountRepository accountRepositoty;

    @GetMapping("/rest/address/fill/{id}")
    public Map<String, Object> getFillAddress(@PathVariable Integer id) {
        Account account = accountRepositoty.findById(id).get();
        Map<String, Object> map = new HashMap<String, Object>();
        if (account == null) {
            map.put("message", "Account is null");
            map.put("status", "error");
            return map;
        }
        List<Address> listAddresses = addressRepository.findByAccount(account);
        map.put("data", listAddresses);
        map.put("message", "Address fill successfully");
        map.put("status", "success");
        return map;
    }

    @PostMapping("/rest/address/create/{id}")
public Map<String, Object> createAddress(@PathVariable("id") Integer id, @RequestBody Address address) {
    Map<String, Object> map = new HashMap<>();
    
    // Lấy tài khoản theo ID
    Optional<Account> optionalAccount = accountRepositoty.findById(id);
    if (optionalAccount.isEmpty()) {
        map.put("message", "Account not found");
        map.put("status", "error");
        return map;
    }
    
    Account account = optionalAccount.get();
    
    // Đặt tất cả các địa chỉ của tài khoản thành không mặc định
    List<Address> listAddresses = addressRepository.findByAccount(account);
    for (Address addressEnty : listAddresses) {
        if (addressEnty.isStatus()) {
            addressEnty.setStatus(false);
            addressRepository.save(addressEnty);
        }
    }

    // Thiết lập địa chỉ mới là mặc định
    address.setStatus(true);
    address.setAccount(account);
    addressRepository.save(address);
    
    map.put("data", address);
    map.put("message", "Address created successfully and set as default");
    map.put("status", "success");
    return map;
}


    @GetMapping("/rest/address/getOne/{id}")
    public Map<String, Object> getByIdAddress(@PathVariable("id") Integer id) {
        Address address = addressRepository.findById(id).get();
        Map<String, Object> map = new HashMap<String, Object>();
        if (id == null) {
            map.put("message", "Id is null");
            map.put("status", "error");
            return map;
        }
        map.put("data", address);
        map.put("message", "Address create successfully");
        map.put("status", "success");
        return map;
    }


    @PutMapping("/rest/address/update/{id}")
    public Map<String, Object> updateAddress(@PathVariable Integer id,@RequestBody Address address) {
        Map<String, Object> map = new HashMap<String, Object>();
        Account account = accountRepositoty.findById(id).get();
        if (address.isStatus()) {
            List<Address> listAddresses = addressRepository.findByAccount(account);
            for (Address addressEnty : listAddresses) {
                addressEnty.setStatus(false);
                addressRepository.save(addressEnty);
            }
        }

        addressRepository.saveAndFlush(address);
        map.put("data", address);
        map.put("message", "Address update successfully");
        map.put("status", "success");
        return map;
    }
    @PutMapping("/rest/address/updateStatus/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable("id") Integer id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address not found");
        }
    
        Address address = optionalAddress.get();
        Account account = address.getAccount();
    
        // Đặt tất cả các địa chỉ khác của tài khoản thành không mặc định
        List<Address> addresses = addressRepository.findByAccount(account);
        for (Address addr : addresses) {
            if (addr.isStatus()) {
                addr.setStatus(false);
                addressRepository.save(addr);
            }
        }
    
        // Đặt địa chỉ hiện tại làm mặc định
        address.setStatus(true);
        addressRepository.save(address);
    
        return ResponseEntity.ok("Default address updated successfully");
    }
    
    @DeleteMapping("/rest/address/delete/{id}")
    public Map<String, Object> deleteAddress(@PathVariable("id") Integer id) {
        Address address = addressRepository.findById(id).get();
        if (address.isStatus() == true) {
            List<Address> listAddresses = addressRepository.findAll();
            for (Address address2 : listAddresses) {
                if (address2.getId() != id) {
                    address2.setStatus(true);
                    addressRepository.save(address2);
                    break;
                }
            }
        }
        addressRepository.delete(address);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", "Address delete successfully");
        map.put("status", "success");
        return map;
    }
    @GetMapping("/rest/address/active/{id}") // Theo md
    public ResponseEntity<Map<String, Object>> getAddresses(@PathVariable Integer id) {
        Map<String, Object> map = new HashMap<>();

        // Tìm tài khoản theo ID
        Account account = accountRepositoty.findById(id).orElse(null);
        if (account == null) {
            map.put("message", "Account not found");
            map.put("status", "error");
            return ResponseEntity.status(404).body(map); // Trả về lỗi 404 nếu tài khoản không tồn tại
        }

        // Lấy danh sách địa chỉ có status = true
        List<Address> listAddresses = addressRepository.findByStatusAndAccount(true, account);
        
        // Kiểm tra nếu không có địa chỉ nào
        if (listAddresses.isEmpty()) {
            map.put("message", "No active addresses found");
            map.put("status", "success");
            map.put("data", listAddresses); // Trả về danh sách rỗng
            return ResponseEntity.ok(map);
        }

        map.put("data", listAddresses);
        map.put("message", "Addresses retrieved successfully");
        map.put("status", "success");
        return ResponseEntity.ok(map); // Trả về danh sách địa chỉ
    }
}
