package com.srnrit.BMS.entity;

import java.io.Serializable;

import org.hibernate.annotations.GenericGenerator;

import com.srnrit.BMS.util.idgenerator.ProductIdGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings({ "deprecation", "serial" })
@Entity
@Table(name = "PRODUCT_TABLE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
	@Id
	@Column(name = "PRODUCT_ID")
	@GenericGenerator(name = "product_id_generator", type = ProductIdGenerator.class)
	@GeneratedValue(generator = "product_id_generator")
	private String productId;
	@Column(name = "PRODUCT_NAME")
	private String productName;
	@Column(name = "PRODUCT_IMAGE")
	private String productImage;
	@Column(name = "PRODUCT_QUANTITY")
	private Integer productQuantity;
	@Column(name = "PRODUCT_PRICE")
	private Double productPrice;
	@Column(name = "PRODUCT_STOCK")
	private Boolean inStock;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CATEGORY_ID")
	private Category category;

	public Product(String productId, String productName, String productImage, Integer productQuantity,
			Double productPrice, Boolean inStock) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productImage = productImage;
		this.productQuantity = productQuantity;
		this.productPrice = productPrice;
		this.inStock = inStock;
	}

}
