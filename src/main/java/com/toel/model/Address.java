package com.toel.model;

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
@Table(name = "Address")
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	boolean status;

	String phone;

	String street;

	@Nationalized
	String fullNameAddress;

	Integer province;

	Integer district;

	String wardCode;

	String street;
	@ManyToOne
	@JoinColumn(name = "account_id ")
	Account account;

	@JsonIgnore
	@OneToMany(mappedBy = "address")
	List<Bill> bills;
}
