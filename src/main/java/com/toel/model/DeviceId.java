package com.toel.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DeviceIds")
public class DeviceId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String token;
    
    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

}
