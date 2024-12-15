package com.toel.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@Table(name = "logs")
public class Log {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	String level;
	Date timestamps;
	String tableName;

	@Lob
    @Column(columnDefinition = "LONGTEXT",name = "dataNew")
	String dataNew;
	@Lob
    @Column(columnDefinition = "LONGTEXT",name = "dataOld")
	String dataOld;
	String action_type;

	@ManyToOne
	@JoinColumn(name = "account_id")
	Account account;
}
