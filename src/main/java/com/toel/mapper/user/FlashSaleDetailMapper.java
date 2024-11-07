package com.toel.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.toel.dto.user.response.Response_FlashSaleDetail;
import com.toel.model.FlashSaleDetail;


@Mapper(componentModel = "spring")
public interface FlashSaleDetailMapper {

//	@Mapping(target = "product.evalues", source = "product.evalues") // Ánh xạ evalues từ product vào Response_Product
//	@Mapping(target = "product.star", expression = "java(product.getEvalues().stream().mapToDouble(Evalue::getStar).average().orElse(5.0))") // Tính																																		// toán																																		// st																																																																		// evalues
//	@Mapping(target = "product.quantityEvalue", expression = "java(product.getEvalues().size()")
//	Response_FlashSaleDetail flashSaleDetailToResponseFlashSaleDetail(FlashSaleDetail flashSaleDetail);
	
	@Mapping(target = "product.star", expression = "java(product.getEvalues().stream().mapToDouble(Evalue::getStar).average().orElse(5.0))")  // Tính toán sao cho average không bị lỗi nếu evalues là null hoặc rỗng
	@Mapping(target = "product.quantityEvalue", expression = "java(product.getEvalues().size())")
//	@Mapping(target = "product.evalues", expression = "java(product.getEvalues())")
	Response_FlashSaleDetail flashSaleDetailToResponseFlashSaleDetail(FlashSaleDetail flashSaleDetail);
	

	

}