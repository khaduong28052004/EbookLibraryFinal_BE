package com.toel.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	@Column(name = "discount", columnDefinition = "integer")
	Integer discount;

	@Column(name = "dateDelete", columnDefinition = "datetime")
	LocalDateTime dateDelete;

	@Column(name = "dateStart", columnDefinition = "datetime")
	LocalDateTime dateStart;

	@Column(name = "dateInsert", columnDefinition = "datetime")
	LocalDateTime dateInsert;

	@JsonIgnore
	@OneToMany(mappedBy = "discountRate")
	List<Bill> bills;

	@ManyToOne
	@JoinColumn(name = "account_id")
	Account account;

}
