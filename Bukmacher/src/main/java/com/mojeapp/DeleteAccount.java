package com.mojeapp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteAccount {
    private String decision = "";
    public DeleteAccount(String user, Scanner scanner){
        SecureScanner secureScanner = new SecureScanner(scanner);
        System.out.println("Na pewno? T/N");
        this.decision = secureScanner.nextSecureLine();
        if (this.decision.equals("T")){
            deleteUser(user);
        }
    }
    public boolean getDecision() {
        return this.decision.equals("T");
    }
    public void deleteUser(String login) {
        try (Connection conn = Database.getConnection()) {
            String getUserIdQuery = "SELECT UserID FROM Logowanie WHERE Login = ?";
            int userId = -1;

            try (PreparedStatement getUserIdStmt = conn.prepareStatement(getUserIdQuery)) {
                getUserIdStmt.setString(1, login);
                ResultSet rs = getUserIdStmt.executeQuery();

                if (rs.next()) {
                    userId = rs.getInt("UserID");
                }
            }

            if (userId == -1) {
                System.out.println("Nie znaleziono użytkownika o loginie: " + login);
                return;
            }

            String query = "{CALL UsunUzytkownika(?)}";
            try (CallableStatement stmt = conn.prepareCall(query)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
                System.out.println("Usunięto konto " + login);
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas usuwania konta: " + e.getMessage());
        }
    }
}