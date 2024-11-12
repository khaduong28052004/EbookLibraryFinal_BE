package com.toel.service.seller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.toel.dto.seller.request.ImageProduct.Request_ImageProduct;
import com.toel.mapper.ProductMapper;
import com.toel.model.ImageProduct;
import com.toel.model.Product;
import com.toel.repository.ImageProductRepository;
import com.toel.service.firebase.DeleteImage;
import com.toel.service.firebase.UploadImage;

@Service
public class Service_ImageProductSeller {
    @Autowired
    ImageProductRepository imageProductRepository;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    UploadImage uploadImage;
    @Autowired
    DeleteImage deleteImage;

    public void createProductImages(Product product, List<Request_ImageProduct> images) throws IOException {
        List<ImageProduct> imageProducts = images.stream()
                .map(requestImage -> {
                    try {
                        MultipartFile imageFile = requestImage.getImageFile();
                        String name = uploadImage.uploadFile("product", imageFile); // Tải lên ảnh

                        ImageProduct imageProduct = new ImageProduct();
                        imageProduct.setName(name);
                        imageProduct.setProduct(product);
                        return imageProduct;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull) // Lọc bỏ những ảnh bị lỗi
                .collect(Collectors.toList());

        // Lưu tất cả ImageProduct vào cơ sở dữ liệu trong một lần
        imageProductRepository.saveAll(imageProducts);
    }

    public void updateProductImages(Product product, List<Request_ImageProduct> images) {

        product.getImageProducts().forEach(image -> {
            deleteImage.deleteFileByUrl(image.getName());
            imageProductRepository.delete(image);
        });
        List<ImageProduct> imageProducts = images.stream()
                .map(requestImage -> {
                    try {
                        MultipartFile imageFile = requestImage.getImageFile();
                        String name = uploadImage.uploadFile("product", imageFile);

                        ImageProduct imageProduct = new ImageProduct();
                        imageProduct.setName(name);
                        imageProduct.setProduct(product);
                        return imageProduct;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull) 
                .collect(Collectors.toList());

        imageProductRepository.saveAll(imageProducts);
    }
}
