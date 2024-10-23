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
@Table(name = "Evalues")
public class Evalue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	int star;
	
	@Nationalized
	String content;

	@ManyToOne
	@JoinColumn(name = "product_id")
	Product product;

	@JsonIgnore
	@OneToMany(mappedBy = "evalue")
	List<ImageEvalue> imageEvalues;

	@ManyToOne
	@JoinColumn(name = "account_id")
	Account account;
	
	@OneToOne
	@JoinColumn(name = "bill_id")
	BillDetail billDetail; 


}