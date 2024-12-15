package com.toel.service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Locale.Category;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springdoc.core.converters.models.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.toel.mapper.user.ProductMaperUser;
import com.toel.model.Product;
import com.toel.model.UserProductActions;
import com.toel.repository.CategoryRepository;
import com.toel.repository.ProductRepository;
import com.toel.repository.UserProductActionsRepository;
import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.response.Response_ProductInfo;
import com.toel.exception.AppException;
import com.toel.mapper.ProductMapper;

@Service
public class UserProductActionsService {

    @Autowired
    private UserProductActionsRepository actionsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    ProductMapper productMapper;

    public List<UserProductActions> getAll() {
        List<UserProductActions> lisUserProductActions = actionsRepository.findAll();
        return lisUserProductActions;
    }

    /**
     * @param userId     // account do tac do
     * @param productId
     * @param actionType
     */
    public String handleUserAction(Integer userId, Integer productId, String actionType) {
        try {
            // Check for required fields
            if (productId == null || actionType == null) {
                return "Product ID or Action Type cannot be null";
            }
            if (userId == null) {
                userId = 0;
            }
            Optional<UserProductActions> existingAction = actionsRepository.findByUserIdAndProductId(userId, productId);
            UserProductActions action;
            if (existingAction.isPresent()) {
                action = existingAction.get();
            } else {
                action = new UserProductActions();
                action.setUserId(userId);
                action.setProductId(productId);
                action.setViewCount(0);
                action.setAddToCartCount(0);
                action.setPurchaseCount(0);
                action.setLastActionTime(LocalDateTime.now());
            }
            // Update counts based on actionType
            switch (actionType.toUpperCase()) {
                case "VIEW":
                    action.setViewCount(action.getViewCount() + 1);
                    break;
                case "ADD_TO_CART":
                    action.setAddToCartCount(action.getAddToCartCount() + 1);
                    break;
                case "PURCHASE":
                    action.setPurchaseCount(action.getPurchaseCount() + 1);
                    break;
                case "TIEUCUC": //
                    action.setViewCount(action.getViewCount() - 1);
                    break;
                case "TIEUCUC1": //
                    action.setAddToCartCount(action.getAddToCartCount() - 1);
                    break;
                case "TIEUCUC2": // report(danhgia) ,sp (tichC:tieuC),...............
                    action.setPurchaseCount(action.getPurchaseCount() - 1);
                    break;
                default:
                    throw new IllegalArgumentException("KHÔNG KHỚP HOẠT ĐỘNG NÀO!");
            }
            action.setLastActionTime(LocalDateTime.now());
            actionsRepository.save(action);
            return "Action processed successfully: " + actionType;
        } catch (Exception e) {
            return "Error processing action: " + e.getMessage();
        }
    }

    public String handleUserAction(Integer userId, List<Integer> productId, String actionType) {
        for (Integer integer : productId) {
            handleUserAction(userId, integer, actionType);
        }
        return "Action processed successfully";
    }

    /**
     * VIEW_WEIGHT = 1;
     * ADD_TO_CART_WEIGHT = 3;
     * PURCHASE_WEIGHT = 5;
     * 
     * @return response_productInfo
     */
    public List<Response_ProductInfo> recommendProducts() {// tinh do quan tam san pham
        List<UserProductActions> behaviors = actionsRepository.findAll();// get all UserProductActions
        Map<Integer, Integer> productScores = new HashMap<>(); // luu do // Map lưu trữ số điểm (trọng số) cho mỗi sp

        final int VIEW_WEIGHT = 1; // Trọng số cho từng hành động
        final int ADD_TO_CART_WEIGHT = 3;
        final int PURCHASE_WEIGHT = 5;

        for (UserProductActions behavior : behaviors) { // Duyệt qua tất cả các hành động của người dùng
            Integer productId = behavior.getProductId();
            int score = 0;
            if (behavior.getViewCount() > 0) {
                score += behavior.getViewCount() * VIEW_WEIGHT;
            } // Cộng điểm cho hành động VIEW
            if (behavior.getAddToCartCount() > 0) { // Cộng điểm cho hành động ADD_TO_CART
                score += behavior.getAddToCartCount() * ADD_TO_CART_WEIGHT;
            }

            if (behavior.getPurchaseCount() > 0) { // Cộng điểm cho hành động PURCHASE
                score += behavior.getPurchaseCount() * PURCHASE_WEIGHT;
            } // Cập nhật điểm của sản phẩm trong map
            productScores.put(productId, productScores.getOrDefault(productId, 0) + score);
        }
        productScores.entrySet().stream()
                .forEach(entry -> System.out.println("độ thích :" + entry.getKey() + " = " + entry.getValue()));
        // .limit(2)
        List<Product> listProduct = productScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .map(entry -> productRepository.findById(entry.getKey()).orElse(null))// .limit(1) // .limit(2)
                .collect(Collectors.toList());
        // List<Product> topProducts = productScores.entrySet().stream()
        // .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed()) // Sắp xếp
        // theo điểm số giảm dần
        // .limit(10) // Lấy tối đa 10 sản phẩm
        // .map(entry -> productRepository.findById(entry.getKey()).orElse(null)) // Lấy
        // sản phẩm từ repository
        // .filter(product -> product != null) // Lọc bỏ sản phẩm null (nếu không tìm
        // thấy trong DB)
        // .collect(Collectors.toList());
        System.out.println("size : " + listProduct.size());
        List<Product> listhoatdong = listProduct.stream() // loc san pham product.isDelete() == false // nguoc lai
                .filter(product -> product.isActive() && product.isDelete() == false && product.getAccount().isStatus())
                .collect(Collectors.toList());

        // categoryRepository.findALlByIdAccount(listhoatdong.getAccount);
        return productMapper.Response_ProductInfo(listhoatdong);
    }

    public List<Response_ProductInfo> accListProduct(Integer size) {
        // Lấy tất cả UserProductActions và sắp xếp theo lastActionTime
        List<UserProductActions> recentBehaviors = actionsRepository.findAll().stream()
                .sorted(Comparator.comparing(UserProductActions::getLastActionTime).reversed()) // Sắp xếp giảm dần theo
                                                                                                // // lastActionTime
                .limit(size) // Lấy 3 hành động mới nhất
                .collect(Collectors.toList());
        List<Product> listhoatdong = recentBehaviors.stream()
                .map(entry -> productRepository.findById(entry.getProductId()).orElse(null))// .limit(2)
                .collect(Collectors.toList());
        return productMapper.Response_ProductInfo(listhoatdong);
    }

    public List<Response_ProductInfo> recommendProducts(Integer userId) {// tinh do quan tam san pham
        List<UserProductActions> behaviors = actionsRepository.findByUserId(userId);// get all UserProductActions
        Map<Integer, Integer> productScores = new HashMap<>(); // luu do // Map lưu trữ số điểm (trọng số) cho mỗi sp
        // Trọng số cho từng hành động
        final int VIEW_WEIGHT = 1;
        final int ADD_TO_CART_WEIGHT = 3;
        final int PURCHASE_WEIGHT = 5;
        // Duyệt qua tất cả các hành động của người dùng
        for (UserProductActions behavior : behaviors) {
            Integer productId = behavior.getProductId();
            int score = 0;
            // Cộng điểm cho hành động VIEW
            if (behavior.getViewCount() > 0) {
                score += behavior.getViewCount() * VIEW_WEIGHT;
            }
            // Cộng điểm cho hành động ADD_TO_CART
            if (behavior.getAddToCartCount() > 0) {
                score += behavior.getAddToCartCount() * ADD_TO_CART_WEIGHT;
            }
            // Cộng điểm cho hành động PURCHASE
            if (behavior.getPurchaseCount() > 0) {
                score += behavior.getPurchaseCount() * PURCHASE_WEIGHT;
            }
            // Cập nhật điểm của sản phẩm trong map
            productScores.put(productId, productScores.getOrDefault(productId, 0) + score);
        }
        List<Product> listProduct = productScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .map(entry -> productRepository.findById(entry.getKey()).orElse(null)) // .limit(2)
                .collect(Collectors.toList());

        List<Product> listhoatdong = listProduct.stream() // loc san pham product.isDelete() == false // nguoc lai
                .filter(product -> product.isActive() && product.isDelete() == false && product.getAccount().isStatus())
                .collect(Collectors.toList());
        return productMapper.Response_ProductInfo(listhoatdong);
    }

    public List<Response_ProductInfo> recommendProducts(LocalDateTime date) {// tinh do quan tam san pham
        // Xác định khoảng thời gian đầu ngày và cuối ngày
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay(); // 00:00:00 của ngày
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1); // 23:59:59 của ngày
        // Lọc ra các hành động xảy ra trong khoảng thời gian này
        List<UserProductActions> behaviors = actionsRepository.findByLastActionTimeBetween(startOfDay, endOfDay);
        Map<Integer, Integer> productScores = new HashMap<>(); // luu do // Map lưu trữ số điểm (trọng số) cho mỗi sp
        // Trọng số cho từng hành động

        final int VIEW_WEIGHT = 1;
        final int ADD_TO_CART_WEIGHT = 3;
        final int PURCHASE_WEIGHT = 5;
        // Duyệt qua tất cả các hành động của người dùng
        for (UserProductActions behavior : behaviors) {
            Integer productId = behavior.getProductId();
            int score = 0;
            // Cộng điểm cho hành động VIEW
            if (behavior.getViewCount() > 0) {
                score += behavior.getViewCount() * VIEW_WEIGHT;
            }
            // Cộng điểm cho hành động ADD_TO_CART
            if (behavior.getAddToCartCount() > 0) {
                score += behavior.getAddToCartCount() * ADD_TO_CART_WEIGHT;
            }
            // Cộng điểm cho hành động PURCHASE
            if (behavior.getPurchaseCount() > 0) {
                score += behavior.getPurchaseCount() * PURCHASE_WEIGHT;
            }
            // Cập nhật điểm của sản phẩm trong map
            productScores.put(productId, productScores.getOrDefault(productId, 0) + score);
        }
        List<Product> listProduct = productScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .map(entry -> productRepository.findById(entry.getKey()).orElse(null)).limit(5) // .limit(2)
                .collect(Collectors.toList());

        List<Product> listhoatdong = listProduct.stream() // loc san pham product.isDelete() == false // nguoc lai
                .filter(product -> product.isActive() && product.isDelete() == false)
                .collect(Collectors.toList());
        return productMapper.Response_ProductInfo(listhoatdong);
    }

    public List<Response_ProductInfo> recommendProducts(Integer userId, LocalDateTime date) {// tinh do quan tam sp
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay(); // 00:00:00 của ngày
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1); // 23:59:59 của ngày
        // Lọc ra các hành động xảy ra trong khoảng thời gian này
        List<UserProductActions> behaviors = actionsRepository.findByUserIdAndLastActionTimeBetween(userId, startOfDay,
                endOfDay);
        // List<UserProductActions> behaviors =
        // actionsRepository.findByUserIdAndLastActionTime(userId, date);// get all //
        // UserProductActions
        Map<Integer, Integer> productScores = new HashMap<>(); // luu do // Map lưu trữ số điểm (trọng số) cho mỗi sp
        // Trọng số cho từng hành động
        final int VIEW_WEIGHT = 1;
        final int ADD_TO_CART_WEIGHT = 3;
        final int PURCHASE_WEIGHT = 5;
        // Duyệt qua tất cả các hành động của người dùng
        for (UserProductActions behavior : behaviors) {
            Integer productId = behavior.getProductId();
            int score = 0;
            // Cộng điểm cho hành động VIEW
            if (behavior.getViewCount() > 0) {
                score += behavior.getViewCount() * VIEW_WEIGHT;
            }
            // Cộng điểm cho hành động ADD_TO_CART
            if (behavior.getAddToCartCount() > 0) {
                score += behavior.getAddToCartCount() * ADD_TO_CART_WEIGHT;
            }
            // Cộng điểm cho hành động PURCHASE
            if (behavior.getPurchaseCount() > 0) {
                score += behavior.getPurchaseCount() * PURCHASE_WEIGHT;
            }
            // Cập nhật điểm của sản phẩm trong map
            productScores.put(productId, productScores.getOrDefault(productId, 0) + score);
        }
        List<Product> listProduct = productScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .map(entry -> productRepository.findById(entry.getKey()).orElse(null)) // .limit(2)
                .collect(Collectors.toList());

        List<Product> listhoatdong = listProduct.stream() // loc san pham product.isDelete() == false // nguoc lai
                .filter(product -> product.isActive() && product.isDelete() == false)
                .collect(Collectors.toList());
        return productMapper.Response_ProductInfo(listhoatdong);
    }

    public List<Response_ProductInfo> recomendProductsAndCategory() { // sản phầm xu hươngs theo categorycategory
        // List<Response_ProductInfo> list =
        // recommendProducts().stream().limit(1).toList()
        List<Response_ProductInfo> list = recommendProducts();
        List<com.toel.model.Category> listCategory = new ArrayList<>(); // danh mục in sản phẩm đề xuất
        for (Response_ProductInfo response_ProductInfo : list) {
            // List<com.toel.model.Category> categories = categoryRepository
            // .findALlByIdAccount(response_ProductInfo.getAccount().getId());
            listCategory.add(response_ProductInfo.getCategory());
        }
        listCategory = listCategory.stream() // Loại bỏ trùng lặp danh mục
                .distinct()
                .collect(Collectors.toList());
        List<Product> products = productRepository.findByCategoryIn(listCategory).stream() // loc san pham //
                                                                                           // product.isDelete() = //
                                                                                           // false // nguoc lai
                .filter(product -> product.isActive() && product.isDelete() == false)
                .collect(Collectors.toList());
        ; // pr inin danh mục trong
        List<Product> listhoatdong = products.stream() // loc san pham product.isDelete() == false // nguoc lai
                .filter(product -> product.isActive() && product.isDelete() == false && product.getAccount().isStatus())
                .collect(Collectors.toList());
        return productMapper.Response_ProductInfo(products);
    }

    public List<Response_ProductInfo> recomendProductsAndCategorySize(Integer size) { // sản phầm xu hươngs theo
                                                                                      // categorycategory
        List<Response_ProductInfo> list = recommendProducts();
        List<com.toel.model.Category> listCategory = new ArrayList<>(); // danh mục in sản phẩm đề xuất
        for (Response_ProductInfo response_ProductInfo : list) {
            // List<com.toel.model.Category> categories = categoryRepository
            // .findALlByIdAccount(response_ProductInfo.getAccount().getId());
            listCategory.add(response_ProductInfo.getCategory());
        }
        listCategory = listCategory.stream() // Loại bỏ trùng lặp danh mục
                .distinct()
                .collect(Collectors.toList());
        List<Product> products = productRepository.findByCategoryIn(listCategory).stream()
                .filter(product -> product.isActive() && product.isDelete() == false && product.getAccount().isStatus())
                .limit(size)
                .collect(Collectors.toList());
        // pr inin danh mục trong
        // List<Product> listhoatdong = products.stream() // loc san pham
        // product.isDelete() == false // nguoc lai isStatus
        // // = true
        // .filter(product -> product.isActive() && product.isDelete() == false &&
        // product.getAccount().isStatus())
        // .collect(Collectors.toList());
        return productMapper.Response_ProductInfo(products);
    }

    public List<Response_ProductInfo> recomendProductsAndCategory(Integer userId) { // sản phầm xu hươngs theo //
                                                                                    // categorycategory
        List<Response_ProductInfo> list = recommendProducts(userId);
        List<com.toel.model.Category> listCategory = new ArrayList<>(); // danh mục in sản phẩm đề xuất
        for (Response_ProductInfo response_ProductInfo : list) {
            List<com.toel.model.Category> categories = categoryRepository
                    .findALlByIdAccount(response_ProductInfo.getAccount().getId());
            listCategory.addAll(categories);
        }
        listCategory = listCategory.stream() // Loại bỏ trùng lặp danh mục
                .distinct()
                .collect(Collectors.toList());
        List<Product> products = productRepository.findByCategoryIn(listCategory); // pr inin danh mục trong
        return productMapper.Response_ProductInfo(products);
    }

    public List<Response_ProductInfo> recomendProductsAndCategoryDate(Integer userId, LocalDateTime date) { // theo ngay
        List<Response_ProductInfo> list = recommendProducts(userId, date);
        List<com.toel.model.Category> listCategory = new ArrayList<>(); // danh mục in sản phẩm đề xuất
        for (Response_ProductInfo response_ProductInfo : list) {
            List<com.toel.model.Category> categories = categoryRepository
                    .findALlByIdAccount(response_ProductInfo.getAccount().getId());
            listCategory.addAll(categories);
        }
        listCategory = listCategory.stream() // Loại bỏ trùng lặp danh mục
                .distinct()
                .collect(Collectors.toList());
        List<Product> products = productRepository.findByCategoryIn(listCategory); // pr inin danh mục trong
        List<Product> listhoatdong = products.stream() // loc san pham product.isDelete() == false // nguoc lai
                .filter(product -> product.isActive() && product.isDelete() == false)
                .collect(Collectors.toList());
        return productMapper.Response_ProductInfo(listhoatdong);
    }

    public List<UserProductActions> UserProductActionsDate(List<UserProductActions> userProductActions,
            LocalDateTime date) {
        List<UserProductActions> behaviors = actionsRepository.findByLastActionTime(date);// get all UserProductActions
        return userProductActions;
    }



    // public List<Product> recommendProducts() {
    // List<UserProductActions> behaviors = actionsRepository.findAll();

    // Map<Long, Integer> productFrequency = new HashMap<>();
    // for (UserProductActions behavior : behaviors) {
    // // productFrequency.put(
    // // behavior.getProductId(),
    // // productFrequency.getOrDefault(behavior.getProductId(), 0) + 1);

    // }

    // return productFrequency.entrySet().stream()
    // .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
    // .map(entry -> productRepository.findById(entry.getKey()).orElse(null))
    // .collect(Collectors.toList());
    // }
}
