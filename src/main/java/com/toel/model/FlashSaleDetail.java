package com.toel.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FlashSaleDetails")
public class FlashSaleDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	Integer quantity;

	Integer sale;

	@ManyToOne
	@JoinColumn(name = "product_id")
	Product product;

	@ManyToOne
	@JoinColumn(name = "flashsale_id")
	FlashSale flashSale;

	@OneToMany(mappedBy = "flashSaleDetail")
	List<BillDetail> billDetails;

}
