package com.toel.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "FlashSales")
public class FlashSale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	LocalDateTime dateStart;

	LocalDateTime dateEnd;

	boolean isDelete;

	@ManyToOne
	@JoinColumn(name = "account_id")
	Account account;

	@JsonIgnore
	@OneToMany(mappedBy = "flashSale")
	List<FlashSaleDetail> flashSaleDetails;

}
