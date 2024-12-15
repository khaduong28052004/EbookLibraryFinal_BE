package com.toel.mapper.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.toel.dto.admin.response.Response_SearchAudio;
import com.toel.dto.user.response.Response_Product;
import com.toel.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMaperUser {
	@Mapping(target = "star", expression = "java(Math.round(product.getEvalues().stream().mapToDouble(Evalue::getStar).average().orElse(0.0)))")
	@Mapping(target = "quantityEvalue", expression = "java(product.getEvalues().size())")
	@Mapping(target = "sold", expression = "java(product.getBillDetails().stream().mapToInt(billDetail -> (billDetail.getBill().getOrderStatus().getId() == 5 || billDetail.getBill().getOrderStatus().getId() == 6) ? billDetail.getQuantity() : 0).sum())")
	Response_Product productToResponse_Product(Product product);

	List<Product> toResponse_SearchAudio(List<Response_SearchAudio> response_SearchAudio);
}
