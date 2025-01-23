package com.mojeapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AddMatch {

    public AddMatch(Scanner scanner) {
        SecureScanner secureScanner = new SecureScanner(scanner);
        System.out.println("Podaj datę w postaci YYYY-MM-DD:");
        String date = secureScanner.nextSecureLine();
        System.out.println("Podaj zespół nr 1:");
        String team1 = secureScanner.nextSecureLine();
        int team1Id = getOrCreateTeam(team1);
        System.out.println("Podaj zespół nr 2:");
        String team2 = secureScanner.nextSecureLine();
        int team2Id = getOrCreateTeam(team2);
        double kurs1 = getValidOdds(secureScanner, "Podaj kurs na zespół nr 1 (musi być >1):");
        double kurs2 = getValidOdds(secureScanner, "Podaj kurs na zespół nr 2 (musi być >1):");
        double tie = getValidOdds(secureScanner, "Podaj kurs na remis (musi być >1):");
        if (teamPlaysOnDate(team1Id, date) || teamPlaysOnDate(team2Id, date)) {
            System.out.println("Jeden z zespołów już gra mecz w tym dniu. Spróbuj ponownie.");
            return;
        }

        if (addMatch(date, team1Id, team2Id, kurs1, kurs2, tie)) {
            System.out.println("Mecz został dodany!");
        } else {
            System.out.println("Wystąpił błąd podczas dodawania meczu.");
        }
    }

    private double getValidOdds(SecureScanner scanner, String prompt) {
        double odds;
        do {
            System.out.println(prompt);
            odds = scanner.nextSecureDouble();
        } while (odds <= 1 || !isValidDouble(odds));

        return odds;
    }

    private boolean isValidDouble(double number) {
        String text = String.valueOf(number);
        int decimalPlaces = text.contains(".") ? text.split("\\.")[1].length() : 0;
        return decimalPlaces <= 2;
    }
    

    private int getOrCreateTeam(String teamName) {
        int teamId = -1;
        try (Connection conn = Database.getConnection()) {
            String checkSql = "SELECT ZespolID FROM Zespol WHERE Nazwa = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, teamName);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                teamId = rs.getInt("ZespolID");
            } else {
                String insertSql = "INSERT INTO Zespol (Nazwa) VALUES (?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, teamName);
                insertStmt.executeUpdate();

                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    teamId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas sprawdzania/dodawania zespołu: " + e.getMessage());
        }
        return teamId;
    }

    private boolean teamPlaysOnDate(int teamId, String date) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT COUNT(*) FROM Mecze WHERE Data = ? AND (Zespol1 = ? OR Zespol2 = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            stmt.setInt(2, teamId);
            stmt.setInt(3, teamId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas sprawdzania meczów: " + e.getMessage());
        }
        return false;
    }

    private boolean addMatch(String date, int team1, int team2, double kurs1, double kurs2, double tie) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO Mecze (Data, Zespol1, Zespol2, Kurs1, Kurs2, KursRemis) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            stmt.setInt(2, team1);
            stmt.setInt(3, team2);
            stmt.setDouble(4, kurs1);
            stmt.setDouble(5, kurs2);
            stmt.setDouble(6, tie);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Błąd podczas dodawania meczu: " + e.getMessage());
        }
        return false;
    }
}
