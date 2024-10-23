package com.toel.model;

import java.util.Date;

import jakarta.persistence.Entity;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PendingRequest")
public class PendingRequest {

	@Id
	Integer id;

	Integer id_Object;

	boolean status;

	String tableName;

	Date timestamps;

	@ManyToOne
	@JoinColumn(name = "account_id")
	Account account;

}
