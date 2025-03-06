package com.srnrit.BMS.util.idgenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

@SuppressWarnings("serial")
public class UserIdGenerator implements IdentifierGenerator {

	private Connection connection ;

	@Override
	public Object generate(SharedSessionContractImplementor session, Object object) {
		
	   String prefix="Uid_0";
	   String suffix="";
	   
	   try {
		this.connection=session.getJdbcConnectionAccess().obtainConnection();
		Statement sequenceStatement = this.connection.createStatement();
		ResultSet rs = sequenceStatement.executeQuery("select user_id_seq.nextval from dual");
		if(rs.next()) 
		{
		int sequence = rs.getInt(1);
		System.out.println(sequence);
		suffix = String.valueOf(sequence);
		}
			
	} catch (SQLException e) {
		e.printStackTrace();
	}  
	   
		return prefix+suffix;
	}

}
