package com.srnrit.BMS.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATEGORY_TABLE")
public class Category implements Serializable
{
	@Id
	@Column(name = "CATEGORY_ID")
	private String categoryId;

	@Column(name = "CATEGORY_NAME")
	private String categoryName;

	@OneToMany(cascade = CascadeType.ALL,mappedBy = "category",fetch = FetchType.LAZY,orphanRemoval = true)
	private List<Product> products;

	@PrePersist
	public void generateId()
	{
		if(this.categoryId==null)
		{
			categoryId=this.generateCustomId();
		}
	}

	private String generateCustomId()
	{
		String prefix="CID";
		String suffix="";
		long timeStamp = Instant.now().toEpochMilli();
		int randomPart = ThreadLocalRandom.current().nextInt(100000000, 999999999);
		suffix=timeStamp+""+randomPart;		
		return prefix+suffix;
	}

	//helper method to add product
	public void addProduct(Product product)
	{
		if (this.products == null) {  
			this.products = new ArrayList<>();
		}
		this.products.add(product);
		product.setCategory(this);
	}

	//helper method to remove product
	public void removeProduct(Product product)
	{
		this.products.remove(product);  
		product.setCategory(null);
	}

}
