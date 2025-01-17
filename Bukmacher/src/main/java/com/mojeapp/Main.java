package com.mojeapp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = Database.getConnection();
        ){
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
            scanner.nextLine();
            switch (activity) {
                case 1:
                    Login login = new Login(scanner);
                    String user = login.getUser();
                    new Usage(user, scanner);
                    break;
                case 2:
                    new Register(scanner);
                    break;
                case 3:
                    scanner.close();
                    System.exit(0);
                    Database.closeConnection();
                default:
                    System.out.println("Nie wybrano opcji spośród podanych!");
                    break;
            }
            
        }
    }
}