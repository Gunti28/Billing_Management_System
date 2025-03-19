package com.srnrit.BMS.exception.categoryexceptions;

@SuppressWarnings("serial")
public class CategoryNotFoundException extends RuntimeException{

	public CategoryNotFoundException(String msg)
	{
		super(msg);
	}
}
