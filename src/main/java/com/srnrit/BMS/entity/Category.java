package com.srnrit.BMS.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.srnrit.BMS.util.idgenerator.CategoryIdGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	@SuppressWarnings("deprecation")
	@Id
	@GenericGenerator(name = "category_id_generator",type = CategoryIdGenerator.class)
	@GeneratedValue(generator = "category_id_generator", strategy = GenerationType.SEQUENCE)
	@Column(name = "CATEGORY_ID")
	private String categoryId;
	
	@Column(name = "CATEGORY_NAME")
	private String categoryname;
	
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
		products.remove(product);  //Here this.products.remove(product);
		product.setCategory(null);
	}
		
}