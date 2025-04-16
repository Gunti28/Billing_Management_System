package com.srnrit.BMS.runners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductDatabaseInitializer implements CommandLineRunner
{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... args) throws Exception {
		if(!isSequenceExists("product_id_seq"))
		{
			jdbcTemplate.execute("CREATE SEQUENCE product_id_seq START WITH 1 INCREMENT BY 1");
			System.out.println("Sequence product_id_seq created successfully.");
		}
		else System.out.println("Sequence product_id_seq already exists.");
	}
	
	private boolean isSequenceExists(String sequenceName) {
		String sql = "SELECT COUNT(*) FROM USER_SEQUENCES WHERE SEQUENCE_NAME = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, sequenceName.toUpperCase());
		return count != null && count > 0;
	}
}