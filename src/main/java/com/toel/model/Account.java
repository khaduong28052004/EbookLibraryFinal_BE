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
@Table(name = "accounts")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	String username;

	String password;

	String avatar;
	@Nationalized

	String fullname;

	boolean gender;

	String email;

	@Temporal(TemporalType.DATE)
	// @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	Date birthday;

	String phone;

	String background;

	@Nationalized
	String shopName;

	String numberId;

	String beforeIdImage;

	String afterIdImage;

	boolean status;

	Date createAt;

	Date createAtSeller;

	// 1
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<VoucherDetail> voucherDetails;
	// 2
	@ManyToOne
	@JoinColumn(name = "role_id")
	Role role;
	// 3
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Address> addresses;
	// 4
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Voucher> vouchers;
	// 5
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Follower> followers;
	// 6
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Transaction> transections;
	// 7
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Bill> bills;
	// 8
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Cart> carts;
	// 9
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Evalue> evalues;
	// 10
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Like> likes;
	// 11
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Product> products;
	// 12 chiet khau
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<DiscountRate> discountRates;
	// 13 report
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<AccountReport> accountReports;

	@JsonIgnore
	@OneToMany(mappedBy = "shop")
	List<AccountReport> shopReports;

	// 14 Log
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Log> logs;
	// 15 flash sale
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<FlashSale> flashSales;

	// 16 categories
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Category> categories;

	// 17 deviceId
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<DeviceId> deviceIds;

	// @JsonIgnore
	// @OneToMany(mappedBy = "account")
	// List<Platform> platforms;

	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<ProductReport> productReports;

	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Token> tokens;

	@ManyToOne
    @JoinColumn(name = "platform_id")
	Platform platform;
}
