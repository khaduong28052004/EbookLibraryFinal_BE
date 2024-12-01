package com.toel.service.user;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.response.Response_ProductListFlashSale;
import com.toel.dto.admin.response.Response_SearchAudio;
import com.toel.dto.admin.response.ThongKe.Page_TK_Product;
import com.toel.dto.user.response.Response_Product;
import com.toel.mapper.user.ProductMaperUser;
import com.toel.model.Category;
import com.toel.model.Product;
import com.toel.repository.CategoryRepository;
import com.toel.repository.ProductRepository;
import com.toel.service.admin.Service_Product;
import com.toel.service.admin.Thongke.Service_ThongKe_Product;

@Service
public class Service_Search {
	@Autowired
	ProductRepository productRepo;
	List<Product> listProducts = new ArrayList<Product>();
	List<Product> listProductByCategory = new ArrayList<Product>();
	@Autowired
	ProductMaperUser productMaperUser;
	@Autowired
	CategoryRepository categoryRepo;
	@Autowired
	Service_ThongKe_Product service_ThongKe_Product;
	@Autowired
	Service_Product service_Product;
	@Autowired
	ProductRepository productRepository;

	public Map<String, Object> getProductByName(String name, Integer size, String sort) {
		Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, sort));
		// Page<Product> pageProducts =
		// productRepo.findAllByNameContainingAndIsActiveTrueAndIsDeleteFalse(name,
		// pageable);
		listProducts = productRepo.findAllProduct(Sort.by(Sort.Direction.DESC, "price"));
		// listProducts = listProducts.stream().filter(product -> {
		// String[] names = name.split(" ");
		// for (String word : names) {
		// if (product.getName().contains(word)) {
		// return false;
		// }
		// }
		// return true;
		//
		// }).collect(Collectors.toList());

		String[] names = name.trim().split("\\s*,\\s*|\\s+");
		System.out.println("max " + names.length);
		// System.out.println("totalIndex " + index);
		// System.out.println("currentProportion " + currentProportion);
		if (names.length > 0) {
			List<Integer> listIdProduct = new ArrayList<>();
			float totalIndex = 0;
			float maxProportion = 0;
			for (Product product : listProducts) {
				String productName = product.getName().toLowerCase();
				int totalWords = productName.split("\\s*,\\s*|\\s+").length;
				float index = 0;

				for (String word : names) {
					if (productName.contains(word.toLowerCase())) {
						index++;
					}
				}

				float currentProportion = index / totalWords;
				if (index > totalIndex) {
					maxProportion = currentProportion;
					totalIndex = index;
					listIdProduct.clear();
					listIdProduct.add(product.getId());
				} else if (index >= totalIndex) {
					maxProportion = currentProportion;
					totalIndex = index;
					listIdProduct.add(product.getId());
				}
			}

			listProducts = listProducts.stream().filter(product -> listIdProduct.contains(product.getId()))
					.collect(Collectors.toList());
		}

		List<Response_Product> listResponse_Products = new ArrayList<Response_Product>();
		Map<String, Object> response = new HashMap<String, Object>();
		Map<Integer, Category> categoryMap = new HashMap<Integer, Category>();
		for (Product product : listProducts) {
			listResponse_Products.add(productMaperUser.productToResponse_Product(product));
			categoryMap.computeIfAbsent(product.getCategory().getId(), id -> product.getCategory());
		}
		response.put("datas", listResponse_Products);
		response.put("categories", categoryMap.values());
		return response;
	}

	public Map<String, Object> filterProductByCategory(List<Integer> id_categories, Integer size, String sort) {
		Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, sort));
		List<Category> listCategories = new ArrayList<Category>();
		for (Integer id_catgory : id_categories) {
			listCategories.add(categoryRepo.findById(id_catgory).get());
		}
		List<Integer> listIdProducts = listProducts.stream().map(product -> product.getId())
				.collect(Collectors.toList());
		Page<Product> pageProducts = productRepo.findByCategoryInAndIdIn(listCategories, listIdProducts, pageable);

		listProductByCategory = pageProducts.getContent();

		List<Response_Product> listResponse_Products = new ArrayList<Response_Product>();
		Map<String, Object> response = new HashMap<String, Object>();
		for (Product product : pageProducts) {
			listResponse_Products.add(productMaperUser.productToResponse_Product(product));
		}
		response.put("datas", listResponse_Products);
		return response;
	}

	public Map<String, Object> filterProductByPrice(double priceMin, double priceMax, Integer size, String sort) {
		Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, sort));

		List<Integer> listIdProducts = listProducts.stream().map(product -> product.getId())
				.collect(Collectors.toList());
		Page<Product> pageProducts = productRepo.findByPriceBetweenAndIdIn(priceMin, priceMax, listIdProducts,
				pageable);

		listProductByCategory = pageProducts.getContent();

		List<Response_Product> listResponse_Products = new ArrayList<Response_Product>();
		Map<String, Object> response = new HashMap<String, Object>();
		for (Product product : pageProducts) {
			listResponse_Products.add(productMaperUser.productToResponse_Product(product));
		}
		response.put("datas", listResponse_Products);
		return response;
	}

	public PageImpl<Response_Product> searchImage(List<Integer> idProducts, Integer page, Integer size) {
		List<Integer> uniqueIdList = idProducts.stream()
				.distinct()
				.collect(Collectors.toList());
		Pageable pageable = PageRequest.of(page, size);

		Page<Product> pageProduct = productRepo.findProductsByIdsSortedByTotalSales(uniqueIdList, pageable);

		List<Response_Product> list = pageProduct.stream()
				.map(product -> productMaperUser.productToResponse_Product(product))
				.collect(Collectors.toList());

		return new PageImpl<>(list, pageable, pageProduct.getTotalElements());

	}

	public PageImpl<?> searchAudio(String search, Integer page, Integer size) {
		String normalizedSearch = normalizeString(search);
		LocalDate startDate = null;
		LocalDate endDate = null;
		LocalDate today = LocalDate.now();
		boolean thoigian = false;
		String sortKey = "";
		String option = null;
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "id"));
		Page<Product> pageProduct;

		if (normalizedSearch.contains("san pham ") || normalizedSearch.contains("sach")) {

			if (normalizedSearch.contains("hom qua")) {
				startDate = today.minusDays(1);
				endDate = today.minusDays(1);
				thoigian = true;
			} else if (normalizedSearch.contains("hom nay")) {
				startDate = today;
				endDate = today;
				thoigian = true;
			} else if (normalizedSearch.contains("tuan truoc")) {
				startDate = today.minusWeeks(1).with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
				endDate = today.minusWeeks(1).with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
				thoigian = true;
			} else if (normalizedSearch.contains("tuan nay")) {
				startDate = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
				endDate = today.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
				thoigian = true;
			} else if (normalizedSearch.contains("thang truoc")) {
				startDate = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
				endDate = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
				thoigian = true;
			} else if (normalizedSearch.contains("thang nay")) {
				startDate = today.with(TemporalAdjusters.firstDayOfMonth());
				endDate = today.with(TemporalAdjusters.lastDayOfMonth());
				thoigian = true;
			} else if (normalizedSearch.contains("nam truoc")) {
				startDate = today.minusYears(1).with(TemporalAdjusters.firstDayOfYear());
				endDate = today.minusYears(1).with(TemporalAdjusters.lastDayOfYear());
				thoigian = true;
			} else if (normalizedSearch.contains("nam nay") || normalizedSearch.contains("trong nam")) {
				startDate = today.with(TemporalAdjusters.firstDayOfYear());
				endDate = today.with(TemporalAdjusters.lastDayOfYear());
				thoigian = true;
			} else {
				if (normalizedSearch.contains("nam")) {
					int year = extractNumberAfterWord(normalizedSearch, "nam");
					if (year > 0) {
						startDate = LocalDate.of(year, 1, 1);
						endDate = LocalDate.of(year, 12, 31);
						thoigian = true;
					}
				} else if (normalizedSearch.contains("thang")) {
					int month = extractNumberAfterWord(normalizedSearch, "thang");
					if (month > 0 && month <= 12) {
						YearMonth yearMonth = YearMonth.of(today.getYear(), month);
						startDate = yearMonth.atDay(1);
						endDate = yearMonth.atEndOfMonth();
						thoigian = true;
					}
				}
			}

			// Xử lý điều kiện sắp xếp
			if (normalizedSearch.contains("moi nhat")) {
				option = "moi nhat";
				sortKey = "createat";
			} else if (normalizedSearch.contains("luot ban") ||
					normalizedSearch.contains("hoa don") ||
					normalizedSearch.contains("bill") ||
					normalizedSearch.contains("bills")) {
				option = "bill";
				sortKey = "sumbill";
			} else if (normalizedSearch.contains("danh gia")) {
				option = "danhgia";
				sortKey = "sumevalue";
			} else if (normalizedSearch.contains("like") || normalizedSearch.contains("yeu thich")) {
				option = "yeuthich";
				sortKey = "sumlike";
			}

			// Gọi service và trả về dữ liệu
			if (thoigian) {
				return service_ThongKe_Product.get_Search_Product(
						java.sql.Date.valueOf(startDate),
						java.sql.Date.valueOf(endDate), option, null, page, size, true, sortKey);
			} else if (option != null) {
				return service_ThongKe_Product.get_Search_Product(null,
						null, option, null, page, size, true, sortKey);
			} else {
				pageProduct = productRepository.selectAllMatchingKey(search, null,
						pageable);
			}
		} else {
			pageProduct = productRepository.selectAllMatchingKey(search, null,
					pageable);

		}
		List<Response_Product> list = pageProduct.stream()
				.map(Product -> productMaperUser.productToResponse_Product(Product))
				.collect(Collectors.toList());
		return new PageImpl<>(list, pageable, pageProduct.getTotalElements());
	}

	// Hàm lấy số sau từ khóa
	private int extractNumberAfterWord(String text, String word) {
		Pattern pattern = Pattern.compile(word + "\\s*(\\d+)");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		}
		return -1;
	}

	private String normalizeString(String input) {
		if (input == null) {
			return "";
		}
		String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(normalized).replaceAll("").toLowerCase();
	}
}
