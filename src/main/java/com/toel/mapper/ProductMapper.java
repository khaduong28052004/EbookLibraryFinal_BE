package com.toel.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.toel.dto.seller.request.Product.Request_ProductCreate;
import com.toel.dto.seller.request.Product.Request_ProductUpdate;
import com.toel.dto.admin.response.Response_ProductListFlashSale;
import com.toel.dto.admin.response.Response_SearchAudio;
import com.toel.dto.admin.response.ThongKe.Response_TK_Product;

import com.toel.dto.seller.response.Response_Product;
import com.toel.dto.seller.response.Response_ProductInfo;
import com.toel.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Response_Product response_Product(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "imageProducts", ignore = true)
    Product productCreate(Request_ProductCreate request_Product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "imageProducts", ignore = true)
    Product productUpdate(Request_ProductUpdate request_Product);

    Response_ProductListFlashSale tProductListFlashSale(Product product);


    Response_TK_Product toResponse_TK_Product(Product product);

    Response_SearchAudio toResponse_TK_ProductSearchAudio(Product product);


    List<Response_Product> listResponse_Products(List<Product> products);
    List<Response_ProductListFlashSale> tProductListFlashSale(List<Product> product);


    List<Response_ProductInfo> Response_ProductInfo(List<Product> product);


}
