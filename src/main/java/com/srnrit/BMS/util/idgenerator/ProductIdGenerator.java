package com.srnrit.BMS.util.idgenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class ProductIdGenerator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        String prefix = "prodId_0";
        String suffix = "";

        try (
            Connection connection = session.getJdbcConnectionAccess().obtainConnection();
            Statement sequenceStatement = connection.createStatement();
            ResultSet resultSequence = sequenceStatement.executeQuery("SELECT product_id_seq.NEXTVAL FROM dual")
        ) {
            if (resultSequence.next()) {
                int sequence = resultSequence.getInt(1);
                suffix = String.valueOf(sequence);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating product ID", e);
        }

        return prefix + suffix;
    }
}
