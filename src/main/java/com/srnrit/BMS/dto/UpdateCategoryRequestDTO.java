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
<<<<<<< HEAD
	@NotBlank(message = "Category id must not be blank or null")
	private String categoryId;

	
	@NotBlank (message = "Category Name must not be null or blank")
=======
	@NotBlank(message = "Id cannot be null or blank")
	private String categoryId;

	
	@NotBlank (message = "Category Name must not be blank")
>>>>>>> 02d058093685d0df4d9480f65b8a49b1ba11c605
	@Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
	@Pattern(regexp = "^[A-Za-z ]+$",message = "Name must contain only alphabets")
	private String categoryName;
}
