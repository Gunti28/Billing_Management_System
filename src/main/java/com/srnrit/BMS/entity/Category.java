package com.srnrit.BMS.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
		this.products.remove(product);  //Here this.products.remove(product);
		product.setCategory(null);
	}
	
	@PrePersist
	public void generateCategoryId()
	{
		if(categoryId==null)
		{
			String prefix="CID";
	        String suffix="";
	        
	        long timestamp = Instant.now().toEpochMilli();
	        int randomPart = ThreadLocalRandom.current().nextInt(100000000,999999999);
	        
	        suffix=timestamp+""+randomPart;
	        
	       this.categoryId=prefix+suffix;
		}
	}
		
}