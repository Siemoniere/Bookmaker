package com.mojeapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AddCoupon {

    public AddCoupon(String user, Scanner scanner) {
        SecureScanner secureScanner = new SecureScanner(scanner);

        System.out.println("Lista dostępnych klubów:");
        listAllTeams();

        System.out.println("Podaj nazwę zespołu, którego mecz Cię interesuje:");
        String team1 = secureScanner.nextSecureLine();
        int team1Id = getTeamId(team1);
        if (team1Id == -1) {
            System.out.println("Nie znaleziono zespołu: " + team1);
            return;
        }

        System.out.println("Zespoły grające z " + team1 + ":");
        listTeamsPlayingAgainst(team1Id);

        System.out.println("Podaj zespół przeciwko któremu gra " + team1 + ":");
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
        
        double kurs1 = 0, kurs2 = 0, remis = 0;
        try (Connection conn = Database.getConnection("normal")) {
            String sql = "SELECT Zespol1, Zespol2, Kurs1, Kurs2, KursRemis FROM Mecze WHERE MeczID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, matchId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int zespol1Id = rs.getInt("Zespol1");
                int zespol2Id = rs.getInt("Zespol2");
        
                if (zespol1Id == team1Id && zespol2Id == team2Id) {
                    kurs1 = rs.getDouble("Kurs1");
                    kurs2 = rs.getDouble("Kurs2");
                } else if (zespol1Id == team2Id && zespol2Id == team1Id) {
                    kurs1 = rs.getDouble("Kurs2");
                    kurs2 = rs.getDouble("Kurs1");
                }
                remis = rs.getDouble("KursRemis");
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania kursów: " + e.getMessage());
        }
        

        System.out.println("Podaj kurs, który chcesz obstawić:");
        System.out.println("1. " + team1 + " - Kurs: " + kurs1);
        System.out.println("2. " + team2 + " - Kurs: " + kurs2);
        System.out.println("3. Remis - Kurs: " + remis);
        int activity = secureScanner.nextSecureInt();

        double selectedOdd = 0.0;
        switch (activity) {
            case 1:
                selectedOdd = kurs1;
                break;
            case 2:
                selectedOdd = kurs2;
                break;
            case 3:
                selectedOdd = remis;
                break;
            default:
                System.out.println("Nieprawidłowy wybór!");
                return;
        }

        double userBalance = getUserBalance(user);
        System.out.println("Twoje saldo: " + userBalance + " PLN");
        double price = getValidAmount(secureScanner, "Podaj kwotę, którą chcesz obstawić:", userBalance);

        if (addCoupon(user, matchId, selectedOdd, price)) {
            updateUserBalance(user, userBalance - price);
            System.out.println("Kupon został dodany!");
        } else {
            System.out.println("Nie udało się dodać kuponu.");
        }
    }

    private void listAllTeams() {
        try (Connection conn = Database.getConnection("normal")) {
            String sql = "SELECT ZespolID, Nazwa FROM Zespol";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("ZespolID") + ", Nazwa: " + rs.getString("Nazwa"));
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania zespołów: " + e.getMessage());
        }
    }

    private void listTeamsPlayingAgainst(int teamId) {
        try (Connection conn = Database.getConnection("normal")) {
            String sql = "SELECT DISTINCT Z.Nazwa FROM Mecze M " +
                        "JOIN Zespol Z ON (M.Zespol1 = Z.ZespolID OR M.Zespol2 = Z.ZespolID) " +
                        "WHERE M.Zespol1 = ? OR M.Zespol2 = ? AND Z.ZespolID <> ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, teamId);
            stmt.setInt(2, teamId);
            stmt.setInt(3, teamId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("Nazwa"));
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania drużyn: " + e.getMessage());
        }
    }

    private int getTeamId(String teamName) {
        try (Connection conn = Database.getConnection("normal")) {
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

    private double getUserBalance(String login) {
        try (Connection conn = Database.getConnection("normal")) {
            String sql = "SELECT stanKonta FROM Ludzie INNER JOIN Logowanie ON Ludzie.UserID = Logowanie.UserID WHERE Logowanie.Login = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("stanKonta");
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania salda: " + e.getMessage());
        }
        return -1;
    }

    private double getValidAmount(SecureScanner scanner, String prompt, double maxAmount) {
        double amount;
        do {
            System.out.println(prompt);
            amount = scanner.nextSecureDouble();
            if (amount > maxAmount) {
                System.out.println("Nie możesz obstawić więcej niż masz w portfelu!");
            }
        } while (amount <= 0 || amount > maxAmount || !isValidDouble(amount));
        return amount;
    }

    private boolean isValidDouble(double number) {
        return ((number * 100) % 1) == 0;
    }

    private void updateUserBalance(String login, double newBalance) {
        try (Connection conn = Database.getConnection("normal")) {
            String sql = "UPDATE Ludzie SET stanKonta = ? WHERE UserID = (SELECT UserID FROM Logowanie WHERE Login = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, newBalance);
            stmt.setString(2, login);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Błąd podczas aktualizacji salda: " + e.getMessage());
        }
    }

    private boolean addCoupon(String user, int matchId, double odd, double amount) {
        try (Connection conn = Database.getConnection("normal")) {
            conn.setAutoCommit(false);

            String sqlKupon = "INSERT INTO Kupon (UserID, Kurs, Wplata, MozliwaWygrana) " +
                            "VALUES ((SELECT UserID FROM Logowanie WHERE Login = ?), ?, ?, ?)";
            PreparedStatement stmtKupon = conn.prepareStatement(sqlKupon, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtKupon.setString(1, user);
            stmtKupon.setDouble(2, odd);
            stmtKupon.setDouble(3, amount);
            double winAmount = Math.round(amount * odd * 0.88 * 100.0) / 100.0;
            stmtKupon.setDouble(4, winAmount);

            int rowsInserted = stmtKupon.executeUpdate();
            if (rowsInserted == 0) {
                conn.rollback();
                System.out.println("Failed to add coupon.");
                return false;
            }

            ResultSet generatedKeys = stmtKupon.getGeneratedKeys();
            int kuponId;
            if (generatedKeys.next()) {
                kuponId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                System.out.println("Failed to retrieve coupon ID.");
                return false;
            }

            String sqlKuponMecze = "INSERT INTO KuponMecze (KuponID, MeczID, Kurs) VALUES (?, ?, ?)";
            PreparedStatement stmtKuponMecze = conn.prepareStatement(sqlKuponMecze);
            stmtKuponMecze.setInt(1, kuponId);
            stmtKuponMecze.setInt(2, matchId);
            stmtKuponMecze.setDouble(3, odd);

            if (stmtKuponMecze.executeUpdate() == 0) {
                conn.rollback();
                System.out.println("Failed to link coupon with match.");
                return false;
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding coupon: " + e.getMessage());
        }
        return false;
    }
    private int getMatchId(int team1Id, int team2Id) {
        try (Connection conn = Database.getConnection("normal")) {
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
            System.err.println("Error retrieving match ID: " + e.getMessage());
        }
        return -1;
    }
}
