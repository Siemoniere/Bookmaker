package com.mojeapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ChangeOdds {

    public ChangeOdds(Scanner scanner) {
        SecureScanner secureScanner = new SecureScanner(scanner);

        System.out.println("Lista wszystkich zespołów:");
        listAllTeams();

        System.out.println("Podaj nazwę zespołu, którego mecz chcesz zmienić:");
        String team1 = secureScanner.nextSecureLine();
        int team1Id = getTeamId(team1);
        if (team1Id == -1) {
            System.out.println("Nie znaleziono zespołu: " + team1);
            return;
        }

        System.out.println("Mecze zespołu " + team1 + ":");
        listTeamMatches(team1Id);

        System.out.println("Podaj drugi zespół grający ten mecz:");
        String team2 = secureScanner.nextSecureLine();
        int team2Id = getTeamId(team2);
        if (team2Id == -1) {
            System.out.println("Nie znaleziono zespołu: " + team2);
            return;
        }

        int matchId = getMatchId(team1Id, team2Id);
        if (matchId == -1) {
            System.out.println("Mecz między " + team1 + " a " + team2 + " nie istnieje.");
            return;
        }

        System.out.println("Pobieranie kursów dla meczu...");
        double kursGospodarze = 0, kursGoscie = 0, remis = 0;
        try (Connection conn = Database.getConnection("admin")) {
            String sql = "SELECT Zespol1, Zespol2, Kurs1, Kurs2, KursRemis FROM Mecze WHERE MeczID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, matchId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int gospodarzeId = rs.getInt("Zespol1");
                int goscieId = rs.getInt("Zespol2");
        
                kursGospodarze = rs.getDouble("Kurs1");
                kursGoscie = rs.getDouble("Kurs2");
                remis = rs.getDouble("KursRemis");
        
                System.out.println("Gospodarze: " + (gospodarzeId == team1Id ? team1 : team2) + " (Kurs: " + kursGospodarze + ")");
                System.out.println("Goście: " + (goscieId == team1Id ? team1 : team2) + " (Kurs: " + kursGoscie + ")");
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania kursów: " + e.getMessage());
            return;
        }
        
        System.out.println("Podaj kurs zespołu, który chcesz zmienić:");
        System.out.println("1. Kurs na gospodarzy (Kurs: " + kursGospodarze + ")");
        System.out.println("2. Kurs na gości (Kurs: " + kursGoscie + ")");
        System.out.println("3. Kurs na remis (Kurs: " + remis + ")");
        int activity = secureScanner.nextSecureInt();
        double finalOdd = getValidOdds(secureScanner, "Podaj kurs docelowy:");

        switch (activity) {
            case 1:
                updateOdds(matchId, "Kurs1", finalOdd);
                break;
            case 2:
                updateOdds(matchId, "Kurs2", finalOdd);
                break;
            case 3:
                updateOdds(matchId, "KursRemis", finalOdd);
                break;
            default:
                System.out.println("Nie wybrano opcji spośród podanych!");
                break;
        }
    }

    private void listAllTeams() {
        try (Connection conn = Database.getConnection("admin")) {
            String sql = "SELECT ZespolID, Nazwa FROM Zespol";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("ZespolID") + ", Nazwa: " + rs.getString("Nazwa"));
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania listy zespołów: " + e.getMessage());
        }
    }

    private int getTeamId(String teamName) {
        try (Connection conn = Database.getConnection("admin")) {
            String sql = "SELECT ZespolID FROM Zespol WHERE Nazwa = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, teamName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ZespolID");
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas sprawdzania zespołu: " + e.getMessage());
        }
        return -1;
    }

    private void listTeamMatches(int teamId) {
        try (Connection conn = Database.getConnection("admin")) {
            String sql = "SELECT M.MeczID, M.Data, Z1.Nazwa AS Team1, Z2.Nazwa AS Team2, M.Kurs1, M.Kurs2, M.KursRemis " +
                        "FROM Mecze M " +
                        "JOIN Zespol Z1 ON M.Zespol1 = Z1.ZespolID " +
                        "JOIN Zespol Z2 ON M.Zespol2 = Z2.ZespolID " +
                        "WHERE M.Zespol1 = ? OR M.Zespol2 = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, teamId);
            stmt.setInt(2, teamId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("MeczID: " + rs.getInt("MeczID") + ", Data: " + rs.getString("Data") +
                        ", " + rs.getString("Team1") + " vs " + rs.getString("Team2") +
                        ", Kurs1: " + rs.getDouble("Kurs1") + ", Kurs2: " + rs.getDouble("Kurs2") +
                        ", Remis: " + rs.getDouble("KursRemis"));
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania meczów: " + e.getMessage());
        }
    }

    private int getMatchId(int team1Id, int team2Id) {
        try (Connection conn = Database.getConnection("admin")) {
            String sql = "SELECT MeczID FROM Mecze WHERE (Zespol1 = ? AND Zespol2 = ?) OR (Zespol1 = ? AND Zespol2 = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, team1Id);
            stmt.setInt(2, team2Id);
            stmt.setInt(3, team2Id);
            stmt.setInt(4, team1Id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("MeczID");
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas sprawdzania meczu: " + e.getMessage());
        }
        return -1;
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
        return ((number * 100) % 1) == 0;
    }

    private void updateOdds(int matchId, String column, double newOdds) {
        try (Connection conn = Database.getConnection("admin")) {
            String sql = "UPDATE Mecze SET " + column + " = ? WHERE MeczID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, newOdds);
            stmt.setInt(2, matchId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Kurs został zaktualizowany!");
            } else {
                System.out.println("Nie udało się zaktualizować kursu.");
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas aktualizacji kursu: " + e.getMessage());
        }
    }
}
