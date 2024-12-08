package com.toel.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	double price;

	double sale;

	double weight;

	String name;
	@Lob
    @Column(columnDefinition = "LONGTEXT")
	String introduce;

	String writerName;

	String publishingCompany;

	Date createAt;

	boolean isDelete;

	Integer quantity;

	boolean isActive;
	// account
	@ManyToOne
	@JoinColumn(name = "account_id")
	Account account;

	// category
	@ManyToOne
	@JoinColumn(name = "category_id")
	Category category;
	// flashsale
	@JsonIgnore
	@OneToMany(mappedBy = "product")
	List<FlashSaleDetail> flashSaleDetails;

	@JsonIgnore
	@OneToMany(mappedBy = "product")
	List<Like> likes;

	@JsonIgnore
	@OneToMany(mappedBy = "product")
	List<ImageProduct> imageProducts;
	
	@JsonIgnore
	@OneToMany(mappedBy = "product")
	List<Evalue> evalues;

	@JsonIgnore
	@OneToMany(mappedBy = "product")
	List<BillDetail> billDetails;

	@JsonIgnore
	@OneToMany(mappedBy = "product")
	List<ProductReport> productReports;
}
