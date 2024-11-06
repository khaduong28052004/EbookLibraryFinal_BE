package com.toel.mapper.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.Mapping;

import com.toel.dto.user.response.Response_FlashSaleDetail;
import com.toel.dto.user.response.Response_Product;
import com.toel.model.Evalue;
import com.toel.model.FlashSaleDetail;
import com.toel.model.Product;

@Mapper(componentModel = "spring")
public interface FlashSaleDetailMapper {

//	@Mapping(target = "product.evalues", source = "product.evalues") // Ánh xạ evalues từ product vào Response_Product
	   @Mapping(target = "product.star", expression = "java(product.getEvalues().stream().mapToDouble(Evalue::getStar).average().orElse(5.0))")  // Tính toán star từ evalues
	Response_FlashSaleDetail flashSaleDetailToResponseFlashSaleDetail(FlashSaleDetail flashSaleDetail);
	   
}