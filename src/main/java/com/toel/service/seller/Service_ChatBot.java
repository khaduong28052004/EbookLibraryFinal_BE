package com.toel.service.seller;

import java.sql.Date;
import java.text.Normalizer;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.MergedAnnotations.Search;
import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_Product;
import com.toel.mapper.BillMapper;
import com.toel.mapper.user.ProductMaperUser;
import com.toel.model.Bill;
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
    ProductMaperUser productMaperUser;
    @Autowired
    BillMapper billMapper;

    public List<?> searchChatBot(String key) {
        String search = normalizeString(key);
        List<Product> products = new ArrayList<>();
        List<Response_Product> responseList = new ArrayList<>();
        String keySearch = "moi";
        String orderBy = "DESC";

        LocalDate today = LocalDate.now();
        LocalDate dateStart = null, dateEnd = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        switch (getDateKeyword(search)) {
            case "hom nay":
                dateStart = dateEnd = today;
                break;
            case "hom truoc":
                dateStart = dateEnd = today.minusDays(1);
                break;
            case "thang nay":
                dateStart = today.withDayOfMonth(1);
                dateEnd = today;
                break;
            case "thang truoc":
                dateStart = today.minusMonths(1).withDayOfMonth(1);
                dateEnd = dateStart.withDayOfMonth(dateStart.lengthOfMonth());
                break;
            case "nam nay":
                dateStart = today.withDayOfYear(1);
                dateEnd = today;
                break;
            case "nam truoc":
                dateStart = today.minusYears(1).withDayOfYear(1);
                dateEnd = dateStart.withDayOfYear(dateStart.lengthOfYear());
                break;
            case "tuan nay":
                dateStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                dateEnd = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                break;
            case "tuan truoc":
                dateStart = today.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                dateEnd = today.minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                break;
            case "tu ngay":
                dateStart = parseDate(search, formatter, "tu ngay");
                dateEnd = parseDate(search, formatter, "den ngay");
                break;
            default:
                break;
        }

        Date sqlDateStart = (dateStart != null) ? Date.valueOf(dateStart) : null;
        Date sqlDateEnd = (dateEnd != null) ? Date.valueOf(dateEnd) : null;

        switch (getSearchKey(search)) {
            case "moi":
                keySearch = "moi";
                orderBy = "DESC";
                break;
            case "luot ban":
                keySearch = "luot ban";
                orderBy = search.contains("nhat") ? "DESC" : "ASC";
                break;
            case "yeu thich":
                keySearch = "yeu thich";
                orderBy = search.contains("nhat") ? "DESC" : "ASC";
                break;
            case "danh gia":
                keySearch = "danh gia";
                orderBy = search.contains("nhat") ? "DESC" : "ASC";
                break;
            default:
                break;
        }

        if (search.contains("san pham")) {
            if (sqlDateStart != null && sqlDateEnd != null) {
                products = orderBy.equals("DESC")
                        ? productRepository.findChatBotByDateDESC(keySearch, sqlDateStart, sqlDateEnd)
                        : productRepository.findChatBotByDateASC(keySearch, sqlDateStart, sqlDateEnd);
            } else {
                products = orderBy.equals("DESC")
                        ? productRepository.findChatBotDESC(keySearch)
                        : productRepository.findChatBotASC(keySearch);
            }

            responseList = products.stream()
                    .map(product -> productMaperUser.productToResponse_Product(product))
                    .collect(Collectors.toList());
        }

        return responseList;
    }

    public List<?> searchChatBotBill(String search, Integer account_id) {
        return billRepository.findByAccountId(account_id).stream().map(bill -> billMapper.response_Bill(bill))
                .collect(Collectors.toList());
    }

    // Hàm phụ để xác định tiêu chí tìm kiếm
    private String getSearchKey(String search) {
        if (search.contains("moi"))
            return "moi";
        if (search.contains("luot ban"))
            return "luot ban";
        if (search.contains("yeu thich"))
            return "yeu thich";
        if (search.contains("danh gia"))
            return "danh gia";
        return "moi";
    }

    // Hàm phụ để xác định keyword ngày
    private String getDateKeyword(String search) {
        if (search.contains("hom nay"))
            return "hom nay";
        if (search.contains("hom truoc"))
            return "hom truoc";
        if (search.contains("thang nay"))
            return "thang nay";
        if (search.contains("thang truoc"))
            return "thang truoc";
        if (search.contains("nam nay"))
            return "nam nay";
        if (search.contains("nam truoc"))
            return "nam truoc";
        if (search.contains("tuan nay"))
            return "tuan nay";
        if (search.contains("tuan truoc"))
            return "tuan truoc";
        if (search.contains("tu ngay") && search.contains("den ngay"))
            return "tu ngay";
        return "";
    }

    // Hàm phụ để parse ngày
    private LocalDate parseDate(String search, DateTimeFormatter formatter, String keyword) {
        Pattern pattern = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
        Matcher matcher = pattern.matcher(search);
        if (matcher.find(search.indexOf(keyword))) {
            try {
                return LocalDate.parse(matcher.group(), formatter);
            } catch (DateTimeParseException e) {
                throw new RuntimeException(
                        "Định dạng ngày không hợp lệ! Vui lòng nhập ngày theo định dạng dd/MM/yyyy.");
            }
        }
        return null;
    }

    // Hàm normalize chuỗi
    private String normalizeString(String input) {
        if (input == null)
            return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }

}