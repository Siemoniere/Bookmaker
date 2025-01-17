package com.mojeapp;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Haszowanie hasła
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)); // Koszt 12 (bezpieczny poziom)
    }

    // Weryfikacja hasła podczas logowania
    public static boolean checkPassword(String candidate, String hashed) {
        return BCrypt.checkpw(candidate, hashed);
    }
}
