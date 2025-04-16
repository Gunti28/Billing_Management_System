package com.srnrit.BMS.exception.categoryexceptions;

@SuppressWarnings("serial")
public class CategoryNameAlreadyExistsException extends RuntimeException 
{
	public CategoryNameAlreadyExistsException(String msg)
	{
		super(msg);
	}
}
