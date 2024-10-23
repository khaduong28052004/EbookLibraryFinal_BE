package com.toel.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Nationalized;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "OrderStatuses")
public class OrderStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	@Nationalized
	String name;
	
	@Temporal(TemporalType.DATE)
	Date date;
	
	@JsonIgnore
	@OneToMany(mappedBy = "orderStatus")
	List<Bill> bills;
}
