package com.toel.dto.user.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toel.model.Account;
import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_FlashSale {


	Integer id;

	LocalDateTime dateStart;

	LocalDateTime dateEnd;

	boolean isDelete;
}
