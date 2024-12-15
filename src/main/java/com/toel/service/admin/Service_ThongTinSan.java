package com.toel.service.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.toel.dto.admin.request.Platform.Request_PlatformUpdate;
import com.toel.dto.admin.response.Response_ImagePlaform;
import com.toel.dto.admin.response.Response_Platform;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.ImagePlaformMapper;
import com.toel.mapper.PlatformMapper;
import com.toel.model.CategoryImage;
import com.toel.model.ImagePlaform;
import com.toel.model.Platform;
import com.toel.repository.CategoryImagesRepository;
import com.toel.repository.ImagePlatformsRepository;
import com.toel.repository.PlatformRepository;
import com.toel.service.firebase.DeleteImage;
import com.toel.service.firebase.UploadImage;

@Service
public class Service_ThongTinSan {
    @Autowired
    PlatformRepository platformRepository;
    @Autowired
    CategoryImagesRepository categoryImageRepository;
    @Autowired
    ImagePlatformsRepository imagePlaformRepository;
    @Autowired
    PlatformMapper platformMapper;
    @Autowired
    ImagePlaformMapper imagePlaformMapper;
    @Autowired
    DeleteImage deleteImage;
    @Autowired
    UploadImage uploadImage;

    public Response_Platform getIdPlatform(Integer id) {
        return platformMapper.tResponse_Platform(platformRepository.findById(id).get());
    }

    public Response_Platform update(Request_PlatformUpdate platformUpdate) {
        Platform platform = platformRepository.findById(platformUpdate.getId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Sàn"));
        if (platformUpdate.getPolicies() == null) {
            platformMapper.Request_PlatformUpdateNotPolicies(platform, platformUpdate);
        } else {
            platform.setPolicies(platformUpdate.getPolicies());
        }
        return platformMapper.tResponse_Platform(platformRepository.save(platform));
    }

    public List<Response_ImagePlaform> getAllByCategoryImage(Integer category) {
        CategoryImage categoryImage = categoryImageRepository.findById(category)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Thể loại ảnh"));
        return imagePlaformRepository.findAllByCategoryImage(categoryImage).stream()
                .map(image -> {
                    Response_ImagePlaform response_ImagePlaform = new Response_ImagePlaform();
                    response_ImagePlaform.setCategoryImage(categoryImage);
                    response_ImagePlaform.setUrl(image.getUrl());
                    response_ImagePlaform.setId(image.getId());
                    return response_ImagePlaform;
                })
                .toList();
    }

    // public Response_ImagePlaform createImagePlaform(Request_ImagePlaformCreate
    // image) {
    // ImagePlaform entity = new ImagePlaform();
    // entity.setCategoryImage(categoryImageRepository.findById(image.getCategoryImage()).get());
    // entity.setUrl(image.getUrl());
    // return
    // imagePlaformMapper.toImagePlaform(imagePlaformRepository.save(entity));
    // }

    // public void delete(List<Integer> list) {
    // list.forEach(item -> {
    // ImagePlaform imagePlaform = imagePlaformRepository.findById(item)
    // .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Ảnh"));
    // imagePlaformRepository.delete(imagePlaform);
    // });
    // }

    // public boolean saveImagePlaform(Integer categoryId, List<MultipartFile>
    // images) {
    // return updateProductImages(categoryImageRepository.findById(categoryId)
    // .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Loại Ảnh")),
    // images);
    // }

    public boolean updateProductImages(CategoryImage categoryImage, List<MultipartFile> images) {
        try {
            System.out.println("===== categoryImage" + categoryImage.getName());

            imagePlaformRepository.deleteAll(categoryImage.getImagePlaforms());
            // for (ImagePlaform image : categoryImage.getImagePlaforms()) {
            // try {
            // System.out.println("===== categoryImageIMG" + image);
            // // deleteImage.deleteFileByUrl(image.getUrl());
            // imagePlaformRepository.delete(image);
            // } catch (Exception e) {
            // e.printStackTrace();
            // return false;
            // }
            // }

            List<ImagePlaform> imagePlaforms = new ArrayList<>();
            for (MultipartFile requestImage : images) {
                try {
                    String name = uploadImage.uploadFile("product", requestImage);
                    ImagePlaform imagePlaform = new ImagePlaform();
                    imagePlaform.setUrl(name);
                    imagePlaform.setCategoryImage(categoryImage);
                    imagePlaforms.add(imagePlaform);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            imagePlaformRepository.saveAll(imagePlaforms);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
