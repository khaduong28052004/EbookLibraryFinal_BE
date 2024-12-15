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
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productreports")
public class ProductReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Nationalized
    String title;
    Date createAt;
    boolean status;
    String content;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

	@ManyToOne
	@JoinColumn(name = "product_id")
	Product product;
	
	@JsonIgnore
	@OneToMany(mappedBy = "productReport")
	List<ImageProductReport> imageReportProducts;

}
