package com.toel.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "Bills")
public class Bill {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	double totalPrice;

	double discountPrice;

	int totalQuantity;

	double priceShipping;

	Date finishAt;

	Date createAt;

	Date updateAt;

	@JsonIgnore
	@OneToMany(mappedBy = "bill")
	List<BillDetail> billDetails;

	@OneToMany(mappedBy = "bill")
	List<VoucherDetail> voucherDetails;

	@ManyToOne
	@JoinColumn(name = "account_id")
	Account account;

	@OneToMany(mappedBy = "bill")
	List<Transaction> transactions;

	@ManyToOne
	@JoinColumn(name = "paymentmethod_id")
	PaymentMethod paymentMethod;

	@ManyToOne
	@JoinColumn(name = "orderstatus_id")
	OrderStatus orderStatus;

	@ManyToOne
	@JoinColumn(name = "address_id")
	Address address;

	@ManyToOne
	@JoinColumn(name = "discountrate_id")
	DiscountRate discountRate;

}
