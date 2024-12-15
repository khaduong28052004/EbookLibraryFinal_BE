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
@Table(name = "ImageReportAccounts")
public class ImageAccountReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	String src;

	@ManyToOne
	@JoinColumn(name = "accountReport_id")
	AccountReport accountReport;
    // hành đông cân nhắc tư tương cứng rắng không tiếp nhận ý kiến
}