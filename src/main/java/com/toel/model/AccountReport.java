package com.toel.model;

import java.util.Date;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "AccountReports")
public class AccountReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	@Nationalized
	String title;
	Date createAt;
	boolean status;
	String content;
	@ManyToOne
	@JoinColumn(name = "shop_id")
	Account shop;
	// Integer parent_id;
	// @ManyToOne
	// @JoinColumn(name = "product_id")
	// Product product;

	@ManyToOne
	@JoinColumn(name = "account_id")
	Account account;

}
