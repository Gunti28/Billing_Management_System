package com.srnrit.BMS.util.idgenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

@SuppressWarnings("serial")
public class ProductIdGenerator implements IdentifierGenerator {

	private Connection connection;

	@Override
	public Object generate(SharedSessionContractImplementor session, Object object) {
		String prefix = "prodId_0";
		String suffix = "";

		try {
			this.connection = session.getJdbcConnectionAccess().obtainConnection();
			Statement sequenceStatement = this.connection.createStatement();
			ResultSet resultSequence = sequenceStatement.executeQuery("select product_id_seq.nextval from dual");
			if (resultSequence.next()) {
				int sequence = resultSequence.getInt(1);
				suffix = String.valueOf(sequence);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prefix + suffix;
	}

}
