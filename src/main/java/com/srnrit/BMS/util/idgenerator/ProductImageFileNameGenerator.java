package com.srnrit.BMS.util.idgenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;

@Component
public class ProductImageFileNameGenerator {

    private static DataSource dataSource;

    public ProductImageFileNameGenerator(DataSource dataSource) {
        ProductImageFileNameGenerator.dataSource = dataSource;
    }

    public static String getNewFileName(String originalFileName) {
        String sequenceNumber = "";
        
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT image_id_seq.NEXTVAL FROM dual")) {

            if (resultSet.next()) {
                sequenceNumber = String.valueOf(resultSet.getInt(1));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error generating image file name: " + e.getMessage());
        }

        // Split file name and extension
        int dotIndex = originalFileName.lastIndexOf(".");
        String baseName = (dotIndex == -1) ? originalFileName : originalFileName.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : originalFileName.substring(dotIndex);

        // Format: "originalFileName_Sequence.extension"
        return baseName + "_" + sequenceNumber + extension;
    }
}
