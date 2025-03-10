package com.srnrit.BMS.util.idgenerator;

import java.security.SecureRandom;
import java.util.Optional;

public class OTPGenerator {
	private static final String DIGITS = "0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static Optional<StringBuilder> generateOTP(int length)
    {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        return Optional.of(otp);
    }
}
