package com.srnrit.BMS.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class FileStorageProperties {
    
    @Value("${file.storage.images}")
    private String imageStoragePath;
    
    public String getImageStoragePath() {
        return imageStoragePath;
    }
    
    public long getMaxFileSize() {
        return getMaxFileSize();
    }
}

