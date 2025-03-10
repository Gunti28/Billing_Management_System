package com.srnrit.BMS.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileStorageProperties {

    @Value("${file.storage.images}")
    private String imageStoragePath;

    public String getImageStoragePath() {
        return imageStoragePath;
    }


}
