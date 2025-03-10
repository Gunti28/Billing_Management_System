package com.srnrit.BMS.util.idgenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

// Here we are written auto increment functionality with Custom generator
@SuppressWarnings("serial")
public class CategoryIdGenerator implements IdentifierGenerator
{
	private Connection connection;
	@Override
	public Object generate(SharedSessionContractImplementor session, Object object) 
	{
		String prefix="cid_0";
		String suffix ="";

		try {
			this.connection=session.getJdbcConnectionAccess().obtainConnection();
			Statement sequenceStatement=this.connection.createStatement();
			ResultSet rstSequence=sequenceStatement.executeQuery("select category_id_seq.nextval from dual");
			if(rstSequence.next())
			{
				int sequence=rstSequence.getInt(1);
				suffix=String.valueOf(sequence);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return prefix+suffix;
	}

}
