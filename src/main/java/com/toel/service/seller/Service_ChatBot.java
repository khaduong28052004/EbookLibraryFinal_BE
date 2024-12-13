package com.toel.service.seller;

import java.sql.Date;
import java.text.Normalizer;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.seller.response.Response_Bill;
import com.toel.dto.seller.response.Response_Category;
import com.toel.dto.seller.response.Response_ChatBotCategory;
import com.toel.dto.user.response.Response_Product;
import com.toel.mapper.BillMapper;
import com.toel.mapper.user.ProductMaperUser;
import com.toel.model.Bill;
import com.toel.model.Product;
import com.toel.repository.BillRepository;
import com.toel.repository.CategoryRepository;
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
    @Autowired
    CategoryRepository categoryRepository;

    public List<Response_Product> searchChatBotProduct(String key) {
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

    public List<Response_Bill> searchChatBotBill(String key, Integer account_id) {

        String search = normalizeString(key);
        List<Bill> listBill = new ArrayList<>();
        List<Response_Bill> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate dateStart = null, dateEnd = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        switch (getDateKeyword(search)) {
            case "hom nay":
                dateStart = dateEnd = today;
                break;
            case "hom qua":
                dateStart = dateEnd = today.minusDays(1);
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

        if (sqlDateStart != null & sqlDateEnd != null) {
            listBill = billRepository.getBillChatBot(account_id, sqlDateStart, sqlDateEnd);
        } else {
            listBill = billRepository.findByAccountId(account_id);
        }
        list = listBill.stream().map(bill -> billMapper.response_Bill(bill))
                .collect(Collectors.toList());
        return list;
    }

    public Response_ChatBotCategory searchChatBotCategory(String key) {
        String search = normalizeString(key);
        String emotion = getDateKeywordCategory(search);

        List<String> genres = suggestGenres(emotion);
        List<Product> result = new ArrayList<>();
        for (String genre : genres) {
            if (!genre.equals("default")) {
                result.addAll(categoryRepository.findTop1ByCategoryName(genre));
            }
        }

        List<Response_Product> list = result.stream()
                .map(product -> productMaperUser.productToResponse_Product(product))
                .collect(Collectors.toList());
        return Response_ChatBotCategory.builder().emotion(emotion).products(list).build();
    }

    private static String getDateKeywordCategory(String search) {
        // Cảm xúc vui vẻ
        if (search.contains("vui") || search.contains("hanh phuc") || search.contains("thich qua")
                || search.contains("mau sac"))
            return "vui";

        // Cảm xúc buồn
        if (search.contains("buon") || search.contains("muon khoc") || search.contains("tam trang")
                || search.contains("dau long") || search.contains("bun") || search.contains("tuyet vong"))
            return "buon";

        // Cảm xúc giận dữ
        if (search.contains("gian") || search.contains("buc") || search.contains("tuc") || search.contains("kho chiu")
                || search.contains("bat binh") || search.contains("tuc gian"))
            return "gian";

        // Cảm xúc chán nản
        if (search.contains("chan") || search.contains("te nhat") || search.contains("mat hung")
                || search.contains("chang co gi vui") || search.contains("khong co y nghia"))
            return "chan";

        // Cảm xúc mệt mỏi
        if (search.contains("met") || search.contains("kiet suc") || search.contains("met moi")
                || search.contains("duoi qua") || search.contains("xuoi nhuong") || search.contains("thieu nang luong"))
            return "met";

        // Cảm xúc yêu thương
        if (search.contains("yeu") || search.contains("dang yeu") || search.contains("lang mam")
                || search.contains("tinh cam") || search.contains("trai tim"))
            return "yeu";

        // Cảm xúc thất tình
        if (search.contains("that tinh") || search.contains("tan vo") || search.contains("dau kho")
                || search.contains("co don") || search.contains("chia tay"))
            return "that tinh";

        // Trường hợp mặc định khi không tìm thấy cảm xúc
        return "default";
    }

    private static List<String> suggestGenres(String emotion) {
        switch (emotion) {
            case "vui":
                return Arrays.asList("Hài Hước", "Tiểu Thuyết", "Light Novel", "Truyện Ngắn - Tản Văn", "Truyện Tranh",
                        "Truyện Thiếu Nhi", "12 Cung Hoàng Đạo", "Nghệ Thuật - Giải Trí", "Du Ký");
            case "buon":
                return Arrays.asList("Chicken Soup - Hạt Giống Tâm Hồn", "Rèn Luyện Nhân Cách", "Câu Chuyện Cuộc Đời",
                        "Ngôn Tình", "Tác Phẩm Kinh Điển", "Thơ Ca, Tục Ngữ, Ca Dao, Thành Ngữ", "Tâm Lý",
                        "Kiến Thức Bách Khoa", "Phóng Sự - Ký Sự - Phê Bình Văn Học");
            case "gian":
                return Arrays.asList("Tâm Lý", "Kỹ Năng Sống", "Chicken Soup - Hạt Giống Tâm Hồn",
                        "Rèn Luyện Nhân Cách", "Marketing - Bán Hàng", "Nhân Sự - Làm Việc",
                        "Nhân Vật - Bài Học Kinh Doanh", "Khởi Nghiệp - Làm Giàu", "Phân Tích Thiết Kế",
                        "Quản Trị - Lãnh Đạo");
            case "chan":
                return Arrays.asList("Hài Hước - Truyện Cười", "Light Novel", "Huyền Bí - Giả Tưởng - Kinh Dị",
                        "Truyện Trinh Thám - Kiếm Hiệp", "Tiểu Thuyết", "Du Ký", "Nghệ Thuật - Giải Trí",
                        "Truyện Tranh", "Tác Giả - Tác Phẩm");
            case "met":
                return Arrays.asList("Chicken Soup - Hạt Giống Tâm Hồn", "Rèn Luyện Nhân Cách",
                        "Dinh Dưỡng - Sức Khỏe Cho Trẻ",
                        "Nuôi Dạy Con", "Cẩm Nang Làm Cha Mẹ", "Phát Triển Kỹ Năng - Trí Tạo Cho Trẻ",
                        "Phương Pháp Giáo Dục Trẻ Các Nước",
                        "Tô Màu, Luyện Chữ", "Kiến Thức - Kỹ Năng Sống Cho Trẻ", "Flashcard - Thẻ Học Thông Minh",
                        "Sách Ảnh");
            case "yeu":
                return Arrays.asList("Ngôn Tình", "Tiểu Thuyết", "Thơ Ca, Tục Ngữ, Ca Dao, Thành Ngữ",
                        "Tác Phẩm Kinh Điển");
            case "that tinh":
                return Arrays.asList("Hài Hước - Truyện Cười", "Huyền Bí - Giả Tưởng - Kinh Dị",
                        "Truyện Trinh Thám - Kiếm Hiệp", "Light Novel");
            default:
                return Arrays.asList("default");
        }
    }

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