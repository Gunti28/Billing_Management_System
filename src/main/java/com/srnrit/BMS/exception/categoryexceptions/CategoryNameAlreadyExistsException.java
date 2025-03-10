package com.srnrit.BMS.exception.categoryexceptions;

@SuppressWarnings("serial")
public class CategoryNameAlreadyExistsException extends Exception 
{
	public CategoryNameAlreadyExistsException(String msg)
	{
		super(msg);
	}
}
