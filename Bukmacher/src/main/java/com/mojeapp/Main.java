package com.mojeapp;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mariadb://localhost:3306/bukmacherka";
    private static final String USER = "root";
    private static final String PASSWORD = "a32837**";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()){
            System.out.println("Połączono");
        } catch (SQLException e){
            System.out.println("Nie udało się połączyć" + e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Witaj!");
        while (true){
            System.out.println("Co chcesz zrobić?");
            System.out.println("1. Zaloguj");
            System.out.println("2. Zarejestruj");
            System.out.println("3. Exit");
            int activity = scanner.nextInt();
            switch (activity) {
                case 1:
                    Login login = new Login();
                    String user = login.getUser();
                    new Usage(user);
                    break;
                case 2:
                    new Register();
                    break;
                case 3:
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Nie wybrano opcji spośród podanych!");
                    break;
            }
            
        }
    }
}