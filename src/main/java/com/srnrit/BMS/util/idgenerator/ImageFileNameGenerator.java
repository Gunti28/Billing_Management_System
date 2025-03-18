package com.srnrit.BMS.util.idgenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;

@Component
public class ImageFileNameGenerator 
{
     private static DataSource dataSource;

	 public ImageFileNameGenerator(DataSource dataSource) {
		super();
		ImageFileNameGenerator.dataSource = dataSource;
	}
	  
	public  static String getNewFileName(String fileName)
	 {
		 String suffix=fileName;
		 String prefix="";
		try 
		{
			Connection connection= ImageFileNameGenerator.dataSource.getConnection();
			System.out.println(connection);
			Statement statement = connection.createStatement();
			ResultSet rst = statement.executeQuery("select image_id_seq.nextval from dual");
			if(rst.next())
			{
				prefix=String.valueOf(rst.getInt(1));
			}
		} 
		catch (Exception e) 
		{
			
			throw new RuntimeException("Something went wrong");
		}
		
		
		return prefix+suffix;
		 
	 }
     
     
}
