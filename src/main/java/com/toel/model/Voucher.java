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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "Vouchers")
public class Voucher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	@Nationalized
	String name;
	
	@Nationalized
	String note;
	
	double totalPriceOrder;
	
	double sale;
	
	int quantity;
	
	boolean isDelete;
	
	@Temporal(TemporalType.DATE)
	Date dateStart;
	
	@Temporal(TemporalType.DATE)
	Date dateEnd;

	@JsonIgnore
	@OneToMany(mappedBy = "voucher")
	List<VoucherDetail> voucherDetails;

	@ManyToOne
	@JoinColumn(name = "typeVourcher_id")
	TypeVoucher typeVoucher;

	@ManyToOne
	@JoinColumn(name = "account_id")
	Account account;
}