package com.toel.controller.user;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.user.response.Response_Product;
import com.toel.dto.user.response.Response_Search;
import com.toel.service.user.Service_Search;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/user")
public class ApiSearch {

	@Autowired
	Service_Search service_Search;

	@GetMapping("search")
	public ApiResponse<Map<String, Object>> search(@RequestParam("text") String text) {
		return ApiResponse.<Map<String, Object>>build().message("success")
				.result(service_Search.getProductByName(text, 10000, "createAt"));
	}

	@GetMapping("filtercategory")
	public ApiResponse<Map<String, Object>> filtercategory(@RequestParam("id_categories") List<Integer> id_categories) {
		return ApiResponse.<Map<String, Object>>build().message("success")
				.result(service_Search.filterProductByCategory(id_categories, 100000, "createAt"));
	}

	@GetMapping("filterprice")
	public ApiResponse<Map<String, Object>> filterPrice(@RequestParam("priceMin") double priceMin,
			@RequestParam("priceMax") double priceMax) {
		return ApiResponse.<Map<String, Object>>build().message("success")
				.result(service_Search.filterProductByPrice(priceMin, priceMax, 1000000, "createAt"));
	}

	@GetMapping("filtercategory/audio")
	public ApiResponse<PageImpl<Response_Product>> filtercategoryAudio(
			@RequestParam("id_categories") List<Integer> id_categories,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "9") Integer size) {
		return ApiResponse.<PageImpl<Response_Product>>build().message("success")
				.result(service_Search.filterProductByCategoryAudio(id_categories, page, size));
	}

	@GetMapping("filterprice/audio")
	public ApiResponse<PageImpl<Response_Product>> filterPriceAudio(
			@RequestParam("priceMin") double priceMin,
			@RequestParam("priceMax") double priceMax,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "3") Integer size) {
		return ApiResponse.<PageImpl<Response_Product>>build().message("success")
				.result(service_Search.filterProductByPriceAudio(priceMin, priceMax, page, 9));
	}

	@GetMapping("searchImage")
	public ApiResponse<PageImpl<Response_Product>> searchImage(
			@RequestParam(value = "idProducts") List<Integer> idProducts,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "9") Integer size) {
		return ApiResponse.<PageImpl<Response_Product>>build()
				.result(service_Search.searchImage(idProducts, page, size));
	}

	@GetMapping("search/audio")
	public ApiResponse<Response_Search<?>> searchText(
			@RequestParam("text") String text,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "9") Integer size) {
		try {
			Response_Search<?> pageSearch = service_Search.searchAudio(text, page, size);
			return ApiResponse.<Response_Search<?>>build()
					.message("Tìm thành công")
					.result(pageSearch);
		} catch (Exception e) {
			return ApiResponse.<Response_Search<?>>build()
					.message(e.getMessage());
		}

	}

}
