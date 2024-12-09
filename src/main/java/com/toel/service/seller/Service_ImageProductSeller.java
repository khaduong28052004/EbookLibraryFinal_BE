package com.toel.service.seller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public boolean createProductImages(Product product, List<MultipartFile> images) {
        List<ImageProduct> imageProducts = new ArrayList<>();
        try {
            for (MultipartFile requestImage : images) {
                try {
                    String name = uploadImage.uploadFile("product", requestImage);
                    ImageProduct imageProduct = new ImageProduct();
                    imageProduct.setName(name);
                    imageProduct.setProduct(product);
                    imageProducts.add(imageProduct);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            imageProductRepository.saveAll(imageProducts);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProductImages(Product product, List<MultipartFile> images) {
        try {
            for (ImageProduct image : product.getImageProducts()) {
                try {
                    deleteImage.deleteFileByUrl(image.getName());
                    imageProductRepository.delete(image);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            List<ImageProduct> imageProducts = new ArrayList<>();
            for (MultipartFile requestImage : images) {
                try {
                    String name = uploadImage.uploadFile("product", requestImage);
                    ImageProduct imageProduct = new ImageProduct();
                    imageProduct.setName(name);
                    imageProduct.setProduct(product);
                    imageProducts.add(imageProduct);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            imageProductRepository.saveAll(imageProducts);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
