package com.go.go_planner.infrastructure.config.utils;

import java.security.SecureRandom;
import java.text.DecimalFormat;

public class CodeGeneratorUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final DecimalFormat FORMATTER = new DecimalFormat("000000");

    // Construtor privado para evitar instanciação da classe utilitária
    private CodeGeneratorUtil() {}

    public static String generateSixDigitCode() {
        // nextInt(1000000) gera de 0 a 999999
        int number = SECURE_RANDOM.nextInt(1000000);
        return FORMATTER.format(number);
    }
}
