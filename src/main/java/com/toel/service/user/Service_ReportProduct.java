package com.toel.service.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.user.resquest.Request_ProductReport;
import com.toel.mapper.user.ProductReportUserMapper;
import com.toel.model.Account;
import com.toel.model.Product;
import com.toel.model.ProductReport;
import com.toel.repository.AccountRepository;
import com.toel.repository.ProductReportRepository;
import com.toel.repository.ProductRepository;

@Service
public class Service_ReportProduct {
	@Autowired
	ProductRepository productRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	ProductReportRepository productReportRepository;
	@Autowired
	ProductReportUserMapper mapper;

	public boolean report(Request_ProductReport request) {
		Product product = productRepository.findById(request.getId_product()).get();
		Account account = accountRepository.findById(request.getId_user()).get();
		ProductReport productReport = mapper.productReportToRequesProductReport(request);
		productReport.setCreateAt(new Date());
		productReport.setStatus(false);
		productReport.setAccount(account);
		productReport.setProduct(product);
		productReport = productReportRepository.save(productReport);
		if (productReportRepository != null) {
			return true;
		}
		return false;
	}

	public Boolean checkReport(Integer id_user, Integer id_product) {
		try {
			Product product = productRepository.findById(id_product).get();
			Account account = accountRepository.findById(id_user).get();
			ProductReport productReport = productReportRepository.findByAccountAndProduct(account, product).get(0);
			if (productReport != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

}
