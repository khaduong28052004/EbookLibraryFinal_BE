package com.toel.mapper;

import org.mapstruct.Mapper;
import com.toel.dto.admin.response.Response_ImagePlaform;
import com.toel.model.ImagePlaform;

@Mapper(componentModel = "Spring")
public interface ImagePlaformMapper {
    Response_ImagePlaform toImagePlaform(ImagePlaform imagePlaform);
}
