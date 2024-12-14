// package com.toel.service.user;

// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.toel.dto.user.CategoryImageDTO;
// import com.toel.dto.user.PlatformDTO;
// import com.toel.model.CategoryImage;
// import com.toel.model.ImagePlaform;
// import com.toel.model.Platform;
// import com.toel.repository.CategoryImagesRepository;
// import com.toel.repository.ImagePlatformsRepository;
// import com.toel.repository.PlatformRepository;

// @Service
// public class PlatformService {
//     @Autowired
//     private PlatformRepository platformRepository;

//     @Autowired
//     private CategoryImagesRepository categoryImagesRepository;

//     @Autowired
//     private ImagePlatformsRepository imagePlatformsRepository;
//     // @Autowired
//     // private CategoryImage categoryImage;

//     // Truy vấn đầy đủ thông tin cho Admin
    
//     public List<Platform> getAllPlatforms() {
//         return platformRepository.findAll();
//     }

//     // Truy vấn thông tin phù hợp cho User
//     // public List<PlatformDTO> getPlatformsForUser() {
//     //     List<Platform> platforms = platformRepository.findAll();
//     //     return platforms.stream().map(platform -> {
//     //         PlatformDTO platformDTO = new PlatformDTO();
//     //         platformDTO.setToken(platform.getToken());
//     //         platformDTO.setAddress(platform.getAddress());
//     //         platformDTO.setPhone(platform.getPhone());
//     //         platformDTO.setEmail(platform.getEmail());

//     //         // Lấy danh mục hình ảnh
//     //         List<CategoryImage> categories = categoryImagesRepository.findByPlatformId(platform.getId());
//     //         List<CategoryDTO> categoryDTOs = categories.stream().map(category -> {
//     //             CategoryDTO categoryDTO = new CategoryDTO();
//     //             categoryDTO.setName(category.getName());

//     //             // Lấy URL hình ảnh
//     //             List<ImagePlaform> images = imagePlatformsRepository.findByCategoryImage_Id(category.getId());
//     //             List<String> imageUrls = images.stream().map(ImagePlaform::getUrl).collect(Collectors.toList());
//     //             categoryDTO.setImageUrls(imageUrls);

//     //             return categoryDTO;
//     //         }).collect(Collectors.toList());

//     //         platformDTO.setCategories(categoryDTOs);
//     //         return platformDTO;
//     //     }).collect(Collectors.toList());
//     // }

//     public List<PlatformDTO> getPlatformsByCategory(Integer categoryId) {
//         List<ImagePlaform> imagePlatforms = imagePlatformsRepository.findByCategoryImage_Id(categoryId);

//         // Chuyển đổi `ImagePlaform` thành `PlatformDTO`
//         return imagePlatforms.stream()
//                 .map(this::convertToDTO)
//                 .collect(Collectors.toList());
//     }
//     private PlatformDTO convertToDTO(ImagePlaform imagePlaform) {
//     // Lấy thông tin `CategoryImage` từ `ImagePlaform`
//     CategoryImage categoryImage = imagePlaform.getCategoryImage();
//     // Lấy danh sách URL của tất cả các hình ảnh thuộc về `CategoryImage`
//     List<String> imageUrls = imagePlaform.getCategoryImage().getImagePlaforms().stream()
//             .map(ImagePlaform::getUrl) // Lấy URL từ từng `ImagePlaform`
//             .collect(Collectors.toList());
//     // Tạo `CategoryImageDTO`
//     CategoryImageDTO categoryImageDTO = new CategoryImageDTO();
//     categoryImageDTO.setName(categoryImage.getName());
//     categoryImageDTO.setImageUrls(imageUrls);
//     // Tạo `PlatformDTO`
//     return new PlatformDTO(
//             "some-token", // Thay bằng giá trị token thực tế
//             "some-address", // Thay bằng địa chỉ thực tế
//             "0123456789", // Thay bằng số điện thoại thực tế
//             "example@email.com", // Thay bằng email thực tế
//             List.of(categoryImageDTO) // Danh sách chứa `CategoryImageDTO`
//     );
// }

// // public List<PlatformDTO> getPlatformsByCategory(Integer categoryId) {
// //     // Tìm danh sách ImagePlatform theo categoryId
// //     List<ImagePlaform> imagePlatforms = imagePlatformsRepository.findByCategoryImage_Id(categoryId);

// //     // Chuyển đổi từng ImagePlatform thành PlatformDTO
// //     return imagePlatforms.stream()
// //             .map(this::convertToDTO)
// //             .collect(Collectors.toList());
// // }

// // private PlatformDTO convertToDTO(ImagePlaform imagePlatform) {
// //     // Lấy thông tin CategoryImage từ ImagePlatform
// //     CategoryImage categoryImage = imagePlatform.getCategoryImage();
// //     if (categoryImage == null) {
// //         throw new IllegalArgumentException("CategoryImage không được null");
// //     }

// //     // Lấy danh sách URL của tất cả các hình ảnh trong CategoryImage
// //     List<String> imageUrls = categoryImage.getImagePlaforms().stream()
// //             .map(ImagePlaform::getUrl)
// //             .collect(Collectors.toList());

// //     // Tạo CategoryImageDTO
// //     CategoryImageDTO categoryImageDTO = new CategoryImageDTO();
// //     categoryImageDTO.setName(categoryImage.getName());
// //     categoryImageDTO.setImageUrls(imageUrls);

// //     // Tạo PlatformDTO
// //     PlatformDTO platformDTO = new PlatformDTO();
// //     platformDTO.setToken(imagePlatform.getPlatform.getToken());
// //     platformDTO.setAddress(imagePlatform.getPlatform().getAddress());
// //     platformDTO.setPhone(imagePlatform.getPlatform().getPhone());
// //     platformDTO.setEmail(imagePlatform.getPlatform().getEmail());
// //     platformDTO.setCategories(List.of(categoryImageDTO));

// //     return platformDTO;
// // }
// }
