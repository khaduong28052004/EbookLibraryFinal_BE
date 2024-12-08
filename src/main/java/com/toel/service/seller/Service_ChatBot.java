package com.toel.service.seller;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.google.api.Page;
import com.toel.dto.admin.response.Response_ProductListFlashSale;
import com.toel.mapper.ProductMapper;
import com.toel.model.Product;
import com.toel.repository.BillRepository;
import com.toel.repository.ProductRepository;

@Service
public class Service_ChatBot {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    BillRepository billRepository;
    @Autowired
    ProductMapper productMapper;

    public List<Response_ProductListFlashSale> searchChatBot(
            String key) {
        String search = normalizeString(key);
        List<Response_ProductListFlashSale> list = new ArrayList<>();
        String status;
        String keySearch = "moi";
        Date dateStart;
        Date dateEnd;
        String orderBy = "DESC";
        // tim
        // if (search.contains("san pham")) {
        status = "san pham";
        if (search.contains("moi")) {
            keySearch = "moi";
            orderBy = "DESC";
        }
        if (search.contains("luot ban")) {
            keySearch = "luot ban";
            if (search.contains("nhat")) {
                orderBy = "DESC";
            } else {
                orderBy = "ASC";
            }
        }
        if (search.contains("yeu thich")) {
            keySearch = "yeu thich";
            if (search.contains("nhat")) {
                orderBy = "DESC";
            } else {
                orderBy = "ASC";
            }
        }
        if (search.contains("yeu thich")) {
            keySearch = "danh gia";
            if (search.contains("nhat")) {
                orderBy = "DESC";
            } else {
                orderBy = "ASC";
            }
        }
        List<Product> products = productRepository.findChatBot(keySearch);
        list = products.stream()
                .map(product -> productMapper.tProductListFlashSale(product))
                .collect(Collectors.toList());
        // }
        return list;
    }

    private String normalizeString(String input) {
        if (input == null) {
            return "";
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase();
    }
}