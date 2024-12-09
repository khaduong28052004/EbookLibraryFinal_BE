package com.toel.service.firebase;

import java.io.IOException;
import java.util.UUID;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadImage {
    public String uploadFile(String status, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.OBJECT_NOT_FOUND, "Ảnh ");
        }
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.create(status + "/" + fileName, file.getInputStream(), file.getContentType());

        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucket.getName(), blob.getName().replace("/", "%2F"));
    }
}
