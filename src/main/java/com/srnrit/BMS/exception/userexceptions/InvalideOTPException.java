package com.srnrit.BMS.exception.userexceptions;

@SuppressWarnings("serial")
public class InvalideOTPException extends RuntimeException
{
     public InvalideOTPException(String msg)
     {
    	 super(msg);
     }
}
