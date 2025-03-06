package com.srnrit.BMS.entity;

import java.io.Serializable;

import org.hibernate.annotations.GenericGenerator;

import com.srnrit.BMS.util.idgenerator.CategoryIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
		
}