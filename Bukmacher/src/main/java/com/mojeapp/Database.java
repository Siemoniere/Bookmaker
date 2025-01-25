package com.mojeapp;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static String url;
    private static String adminuser, user;
    private static String adminpassword, password;
    private static Connection connection;

    static {
        try (InputStream input = Database.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Plik config.properties nie został znaleziony! Upewnij się, że znajduje się w src/resources/");
            }

            Properties properties = new Properties();
            properties.load(input);

            url = properties.getProperty("db.url");
            adminuser = properties.getProperty("db.adminuser");
            adminpassword = properties.getProperty("db.adminpassword");
            user = properties.getProperty("db.user");
            password = properties.getProperty("db.password");

        } catch (IOException e) {
            System.err.println("Błąd podczas ładowania konfiguracji bazy danych: " + e.getMessage());
        }
    }

    public static Connection getConnection(String accessLevel) throws SQLException {
        if (connection == null || connection.isClosed()) {
            if (url == null) {
                throw new SQLException("URL bazy danych nie może być null! Sprawdź config.properties.");
            }

            switch (accessLevel.toLowerCase()) {
                case "admin":
                    connection = DriverManager.getConnection(url, adminuser, adminpassword);
                    break;
                case "normal":
                    connection = DriverManager.getConnection(url, user, password);
                    break;
                default:
                    throw new IllegalArgumentException("Nieznany poziom dostępu: " + accessLevel);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Połączenie do bazy zostało zamknięte.");
            } catch (SQLException e) {
                System.err.println("Błąd podczas zamykania połączenia: " + e.getMessage());
            }
        }
    }
}
