package com.mojeapp;

import java.util.Scanner;

public class SecureScanner {

    private final Scanner scanner;

    public SecureScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public String nextSecureLine() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches(".*[\"\\=!].*")) {
                System.out.println("Błąd: Nie można używać znaków: \" - = !");
                System.out.print("Wpisz ponownie: ");
            } else {
                return input;
            }
        }
    }
    public int nextSecureInt() {
        while (true) {
            String input = scanner.nextLine().trim();
                if (input.matches(".*[\"\\-=!].*")) {
                System.out.println("Błąd: Nie można używać znaków: \" - = !");
                System.out.print("Wpisz ponownie liczbę całkowitą: ");
                continue;
            }
                try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Błąd: Wprowadź poprawną liczbę całkowitą!");
                System.out.print("Wpisz ponownie: ");
            }
        }
    }
    public double nextSecureDouble() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches(".*[\"\\-=!].*")) {
                System.out.println("Błąd: Nie można używać znaków: \" - = !");
                System.out.print("Wpisz ponownie liczbę zmiennoprzecinkową: ");
                continue;
            }
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Błąd: Wprowadź poprawną liczbę zmiennoprzecinkową!");
                System.out.print("Wpisz ponownie: ");
            }
        }
    }
    
    
}
