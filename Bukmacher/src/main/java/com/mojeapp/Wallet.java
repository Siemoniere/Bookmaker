package com.mojeapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Wallet {

    public Wallet(String user, Scanner scanner){
        SecureScanner secureScanner = new SecureScanner(scanner);
        while (true){
            System.out.println("Co chcesz zrobić?");
            System.out.println("1. Sprawdź stan portfela");
            System.out.println("2. Wpłać");
            System.out.println("3. Wypłać");
            System.out.println("4. Wróć");
            int activity = secureScanner.nextSecureInt();
            int value = 0;
            switch (activity) {
                case 1:
                    balanceOperation(user, value);
                    break;
                case 2:
                    System.out.println("Podaj kwote do wpłacenia");
                    value = secureScanner.nextSecureInt();
                    balanceOperation(user, value);
                    break;
                case 3:
                    System.out.println("Podaj kwote do wypłacenia");
                    value = secureScanner.nextSecureInt();
                    balanceOperation(user, -value);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Nie wybrano opcji spośród podanych!");
                    break;
            }
        }
    }
    public double balanceOperation(String login, int value) {
        double balance = -1;
        try (Connection conn = Database.getConnection()) {
            String sqlSelect = "SELECT stanKonta FROM Ludzie INNER JOIN Logowanie ON Ludzie.UserID = Logowanie.UserID WHERE Logowanie.Login = ?";
            PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect);
            stmtSelect.setString(1, login);
            
            try (ResultSet rs = stmtSelect.executeQuery()) {
                if (rs.next()) {
                    balance = rs.getDouble("stanKonta");
                } else {
                    System.out.println("Użytkownik o podanym loginie nie istnieje.");
                    return -1; // użytkownik nie istnieje
                }
            }
    
            double newBalance = balance + value;
            if (newBalance < 0) {
                System.out.println("Nie można wypłacić więcej niż dostępne saldo!");
                return balance;
            }
    
            String sqlUpdate = "UPDATE Ludzie SET stanKonta = ? WHERE UserID = (SELECT UserID FROM Logowanie WHERE Login = ?)";
            PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
            stmtUpdate.setDouble(1, newBalance);
            stmtUpdate.setString(2, login);
    
            int rowsAffected = stmtUpdate.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Operacja zakończona sukcesem. Nowe saldo: " + newBalance + " PLN");
                return newBalance;
            } else {
                System.out.println("Nie udało się zaktualizować salda.");
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas operacji portfela: " + e.getMessage());
        }
        return balance;
    }
    
}