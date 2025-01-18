package com.mojeapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM Logowanie WHERE Login = ?")) {
            
            stmt.setString(1, login);
            stmt.executeUpdate();
            System.out.println("Usunięto konto " + login);
        } catch (SQLException e) {
            System.err.println("Błąd podczas usuwania konta: " + e.getMessage());
        }
    }
}