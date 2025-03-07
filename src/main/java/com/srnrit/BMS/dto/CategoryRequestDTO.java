package com.srnrit.BMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CategoryRequestDTO 
{
   @NotNull(message = "Category Name must not be null")
   @NotBlank (message = "Category Name must not be blank")
   @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
   @Pattern(regexp = "^[A-Za-z ]+$",message = "Name must contain only alphabets")
   private String categoryName;
   
   
}
