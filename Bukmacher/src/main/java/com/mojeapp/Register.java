package com.mojeapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Register {

    public Register(Scanner scanner) {
        SecureScanner secureScanner = new SecureScanner(scanner);
        System.out.println("Podaj imię:");
        String imie = secureScanner.nextSecureLine();
        System.out.println("Podaj nazwisko:");
        String nazwisko = secureScanner.nextSecureLine();
        System.out.println("Podaj datę urodzenia (YYYY-MM-DD):");
        String data = secureScanner.nextSecureLine();
        System.out.println("Podaj login:");
        String login = secureScanner.nextSecureLine();

        if (isLoginTaken(login)) {
            System.out.println("Ten login jest już zajęty! Wybierz inny.");
            return;
        }

        String password = null;
        boolean isCorrect = false;

        while (!isCorrect) {
            System.out.println("Podaj hasło:");
            String pass1 = secureScanner.nextSecureLine();
            System.out.println("Powtórz hasło:");
            String pass2 = secureScanner.nextSecureLine();
            if (pass1.equals(pass2)) {
                password = PasswordUtil.hashPassword(pass1);
                isCorrect = true;
            } else {
                System.out.println("Wprowadzone hasła są różne! Spróbuj ponownie.");
            }
        }

        try (Connection conn = Database.getConnection("normal")) {
            conn.setAutoCommit(false); // Rozpoczynamy transakcję

            int userId = getUserIdFromLudzie(conn, imie, nazwisko, data);

            if (userId == -1) { 
                String insertLudzieSQL = "INSERT INTO Ludzie (Imie, Nazwisko, DataUrodzenia, StanKonta, isAdmin) VALUES (?, ?, ?, 0, 0)";
                try (PreparedStatement stmtLudzie = conn.prepareStatement(insertLudzieSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmtLudzie.setString(1, imie);
                    stmtLudzie.setString(2, nazwisko);
                    stmtLudzie.setString(3, data);
                    stmtLudzie.executeUpdate();

                    try (ResultSet generatedKeys = stmtLudzie.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            userId = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("Błąd: Nie udało się pobrać UserID!");
                        }
                    }
                }
            }

            String insertLogowanieSQL = "INSERT INTO Logowanie (UserID, Login, Password) VALUES (?, ?, ?)";
            try (PreparedStatement stmtLogowanie = conn.prepareStatement(insertLogowanieSQL)) {
                stmtLogowanie.setInt(1, userId);
                stmtLogowanie.setString(2, login);
                stmtLogowanie.setString(3, password);
                stmtLogowanie.executeUpdate();
            }

            conn.commit();
            System.out.println("Użytkownik został zarejestrowany!");
        } catch (SQLException e) {
            System.err.println("Błąd podczas rejestracji: " + e.getMessage());
        }
    }

    private int getUserIdFromLudzie(Connection conn, String imie, String nazwisko, String data) throws SQLException {
        String sql = "SELECT UserID FROM Ludzie WHERE Imie = ? AND Nazwisko = ? AND DataUrodzenia = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, imie);
            stmt.setString(2, nazwisko);
            stmt.setString(3, data);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // Zwracamy istniejące UserID
                }
            }
        }
        return -1; // Nie znaleziono osoby
    }

    private boolean isLoginTaken(String login) {
        try (Connection conn = Database.getConnection("normal")) {
            String sql = "SELECT COUNT(*) FROM Logowanie WHERE Login = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, login);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas sprawdzania loginu: " + e.getMessage());
        }
        return false;
    }
}