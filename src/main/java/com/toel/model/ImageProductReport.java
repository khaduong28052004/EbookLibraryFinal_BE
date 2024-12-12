package com.toel.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ImageReportProducts")
public class ImageProductReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	String src;

	@ManyToOne
	@JoinColumn(name = "productReport_id")
	ProductReport productReport;
}
