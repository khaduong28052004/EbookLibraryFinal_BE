package com.toel.controller.seller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.toel.service.seller.Service_BillSeller;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/user/bill")
public class Apiuserbill {
        @Autowired
        Service_BillSeller service_Bill;
        public static Map<String, String> map = new HashMap<>();


        @GetMapping("/countOder/{idOrder}/{idAccount}")
        public ResponseEntity<?> getCountOders1(@PathVariable Integer idOrder, @PathVariable Integer idAccount) {
                try {
                        map.put("quantity", service_Bill.QuantityBillStatus(idOrder, idAccount));
                        return ResponseEntity.ok().body(map);
                } catch (Exception e) {
                        map.put("quantity", service_Bill.QuantityBillStatus(idOrder, idAccount));
                        return ResponseEntity.badRequest().body(map);// TODO: handle exception
                }
        }

}
