package com.mojeapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {

    private String user;
    public Login(Scanner scanner){
        boolean success = false;
        SecureScanner secureScanner = new SecureScanner(scanner);
        while(!success){
            System.out.println("Podaj login");
            String login = secureScanner.nextSecureLine();
            System.out.println("Podaj hasło");
            String password = secureScanner.nextSecureLine();
            try (Connection conn = Database.getConnection("normal")) {
                String sql = "SELECT Password FROM Logowanie WHERE Login = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, login);
                ResultSet rs = stmt.executeQuery();
    
                if (rs.next()) {
                    String hashedPassword = rs.getString("Password");
    
                    if (PasswordUtil.checkPassword(password, hashedPassword)) { // Weryfikacja hasła
                        user = login;
                        System.out.println("Zalogowano pomyślnie!");
                        success = true;
                    } else {
                        System.out.println("Błędny login lub hasło!");
                    }
                } else {
                    System.out.println("Błędny login lub hasło!");
                }
            } catch (SQLException e) {
                System.err.println("Błąd podczas logowania: " + e.getMessage());
            }
        }
    }
    public String getUser(){
        return user;
    }
}