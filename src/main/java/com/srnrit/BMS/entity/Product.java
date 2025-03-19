package com.srnrit.BMS.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "PRODUCT_TABLE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
	@Id
	@Column(name = "PRODUCT_ID")
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

	public Product(String productName, String productImage, Integer productQuantity, Double productPrice,
			Boolean inStock) {
		super();
		this.productName = productName;
		this.productImage = productImage;
		this.productQuantity = productQuantity;
		this.productPrice = productPrice;
		this.inStock = inStock;
	}

	@PrePersist
	public void generateId() {
		if (this.productId == null) {
			productId = this.generateCustomId();
		}
	}

	private String generateCustomId() {
		String prefix = "PID";
		String suffix = "";
		long timeStamp = Instant.now().toEpochMilli();
		int randomPart = ThreadLocalRandom.current().nextInt(100000000, 999999999);
		suffix = timeStamp + "" + randomPart;

		return prefix + suffix;
	}

}
