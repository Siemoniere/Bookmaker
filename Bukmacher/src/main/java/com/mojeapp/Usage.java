package com.mojeapp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Connection;

public class Usage {

    public Usage(String user, Scanner scanner){
        SecureScanner secureScanner = new SecureScanner(scanner);
        String thisuser = user;
        while(true){
            boolean isAdmin = isUserAdmin(thisuser);
            if (isAdmin) System.out.println("ADMIN");
            System.out.println("Co chcesz zrobić?");
            int option = 1;
            System.out.println((option++) + ". Ustawienia portfela");
            System.out.println((option++) + ". Postaw kupon");
            System.out.println((option++) + ". Usuń konto");
            System.out.println((option++) + ". Wyloguj");
            if(isAdmin){
                System.out.println((option++) + ". Dodaj mecz");
                System.out.println((option++) + ". Zmień kurs");
            }
            int activity = secureScanner.nextSecureInt();
            switch (activity) {
                case 1:
                    new Wallet(thisuser, scanner);
                    break;
                case 2:
                    new AddCoupon(thisuser, scanner);
                    break;
                case 3:
                    DeleteAccount del = new DeleteAccount(thisuser, scanner);
                    if(del.getDecision()){
                        return;
                    }
                    break;      
                case 4:
                    return;            
                case 5:
                    new AddMatch(scanner);
                    break;
                case 6:
                    new ChangeOdds(scanner);
                    break;
                default:
                    System.out.println("Nie podano opcji spośród podanych!");
                    break;
            }
        }
    }
    public boolean isUserAdmin(String login) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT isAdmin FROM Ludzie INNER JOIN Logowanie ON Ludzie.UserID = Logowanie.UserID WHERE Logowanie.Login = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("isAdmin") == 1;
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas sprawdzania uprawnień: " + e.getMessage());
        }
        return false;
    }
}