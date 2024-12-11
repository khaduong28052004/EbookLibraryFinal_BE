//package com.toel.config;
//
//import java.util.Date;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import com.toel.model.Account;
//import com.toel.model.Category;
//import com.toel.model.ImageProduct;
//import com.toel.model.Product;
//import com.toel.repository.AccountRepository;
//import com.toel.repository.CategoryRepository;
//import com.toel.repository.ImageProductRepository;
//import com.toel.repository.ProductRepository;
//
//@Component
//public class main implements CommandLineRunner {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private ImageProductRepository imageProductRepository;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Tạo 100 sản phẩm và liên kết với ảnh sản phẩm
//        for (int i = 0; i < 100; i++) {
//            Product product = new Product();
//            Account account = accountRepository.findById(3).get();
//            Category category = categoryRepository.findById(1).get();
//            product.setAccount(account);
//            product.setName("'Viết cho con trong thời chiến tranh'");
//            product.setCreateAt(new Date());
//            product.setPublishingCompany("'Nguyễn Hoàng Việt'");
//            product.setActive(true);
//            product.setSale(10);
//            product.setDelete(false);
//            product.setPrice(100000);
//            product.setQuantity(1000);
//            product.setWriterName("Dương Văn Kha");
//            product.setWeight(1000);
//            product.setIntroduce("Cuốn sách dành cho những ai muốn khám phá bản thân và giá trị cuộc sống.");
//            product.setCategory(category);
//            product = productRepository.save(product);
//
//            // Tạo và lưu ảnh sản phẩm
//            ImageProduct imageProduct = new ImageProduct();
//            imageProduct.setName(
//                    "https://firebasestorage.googleapis.com/v0/b/ebookstore-4fbb3.appspot.com/o/evalue%2F88842fc2-1acc-4283-98f8-b18e114db35a-su_im_lang_cua_bay_cuu__thomas_harris_600x972.jpg?alt=media");
//            imageProduct.setProduct(product);
//            imageProductRepository.save(imageProduct);
//
//            System.err.println("Số " + i);
//        }
//    }
//}
