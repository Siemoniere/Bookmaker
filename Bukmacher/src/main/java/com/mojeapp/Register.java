package com.mojeapp;

import java.util.Scanner;

public class Register {

    public Register(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj imie");
        String imie = scanner.nextLine();
        System.out.println("Podaj nazwisko");
        String nazwisko = scanner.nextLine();
        System.out.println("Podaj date urodzenia w formacie YYYY-MM-DD");
        //TODO sprawdź czy jest pełnoletni
        String data = scanner.nextLine();
        System.out.println("Podaj login");
        String login = scanner.nextLine();
        boolean isCorrect = false;
        while (!isCorrect){
            System.out.println("Podaj hasło");
            String password = scanner.nextLine();
            System.out.println("Powtorz hasło");
            String second = scanner.nextLine();
            if (password == second){
                isCorrect = true;
            } else {
                System.out.println("Wprowadzone hasła są różne!");
                isCorrect = false;
            }
        }
        scanner.close();
        // TODO dodanie Usera do bazy danych
        
    }

}