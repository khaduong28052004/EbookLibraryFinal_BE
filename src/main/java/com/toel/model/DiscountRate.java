package com.toel.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DiscountRates")
public class DiscountRate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	boolean isDelete;

	@Column(name = "dateDelete", columnDefinition = "datetime")
	Date dateDelete;

	@Column(name = "name", columnDefinition = "text")
	String name;

	@Column(name = "dateStart", columnDefinition = "datetime")
	Date dateStart;

	@Column(name = "dateInsert", columnDefinition = "datetime")
	Date dateInsert;

	@OneToMany(mappedBy = "discountRate")
	List<Bill> bills;

	@ManyToOne
	@JoinColumn(name = "account_id")
	Account account;

}
