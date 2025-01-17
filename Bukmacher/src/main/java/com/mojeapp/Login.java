package com.mojeapp;

import java.util.Scanner;

public class Login {

    private String user;
    public Login(){
        boolean success = false;
        Scanner scanner = new Scanner(System.in);
        while(!success){
            System.out.println("Podaj login");
            String login = scanner.nextLine();
            System.out.println("Podaj hasło");
            String password = scanner.nextLine();
            //TODO przeszukaj userów
            boolean isUser = true; //wtedy to usun
            if (isUser){
                user = login;
                System.out.println("Zalogowano! Witaj " + login);
                success = true;
            } else {
                System.out.println("Błędny login lub hasło!");
                success = false;
            }
        }
        scanner.close();
    }
    public String getUser(){
        return user;
    }
}