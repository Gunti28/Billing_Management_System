package com.srnrit.BMS.util.idgenerator;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class ProductImageFileNameGenerator {

   

    public static String getNewFileName(String originalFileName) 
    {
        String suffix=originalFileName;
        String prefix="";
        
        long timestamp = Instant.now().toEpochMilli();
        int randomPart = ThreadLocalRandom.current().nextInt(100000000,999999999);
        
        prefix=timestamp+""+randomPart;
        
        return prefix+suffix;
    }
}
