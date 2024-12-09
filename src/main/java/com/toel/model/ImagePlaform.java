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
@Table(name = "ImagePlaforms")
public class ImagePlaform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String url;
    
    @ManyToOne
    @JoinColumn(name = "category_images_id")
    CategoryImage categoryImage;
}
