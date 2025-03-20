package com.srnrit.BMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequestDTO 
{
	@NotBlank(message = "Category id must not be blank or null")
	private String categoryId;

	
	@NotBlank (message = "Category Name must not be null or blank")
	@Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
	@Pattern(regexp = "^[A-Za-z ]+$",message = "Name must contain only alphabets")
	private String categoryName;
}
