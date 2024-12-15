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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "followers")
public class Follower {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	int shopId;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	Account account;
	
}