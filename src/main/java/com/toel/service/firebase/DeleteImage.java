package com.toel.service.firebase;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

@Service
public class DeleteImage {
    public void deleteFileByUrl(String fileUrl) {
        try {
            // Lấy tên bucket từ StorageClient
            Bucket bucket = StorageClient.getInstance().bucket();

            // Tách tên file từ URL
            String bucketName = bucket.getName();
            String filePath = fileUrl
                    .replace(String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/", bucketName), "")
                    .replace("?alt=media", "");

            // Giải mã URL
            filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8.toString());

            // Xóa file
            Blob blob = bucket.get(filePath);
            if (blob != null && blob.exists()) {
                blob.delete();
                System.out.println("Deleted image: " + filePath);
            } else {
                System.out.println("File not found: " + filePath);
            }
        } catch (Exception e) {
            System.err.println("Error deleting file: " + fileUrl);
            e.printStackTrace();
        }
    }

}
