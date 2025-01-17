package com.mojeapp;

import java.util.Scanner;

public class AddMatch {

    public AddMatch(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj datę w postaci YYYY-MM-DD");
        String date = scanner.nextLine();
        System.out.println("Podaj zespół nr 1");
        String team1 = scanner.nextLine();
        //TODO sprawdz czy ten zespol istnieje - jak nie to dodaj do tabeli Zespoly
        System.out.println("Podaj zespół nr 2");
        String team2 = scanner.nextLine();
        //TODO to samo tutaj
        System.out.println("Podaj kurs na zespół nr 1");
        int kurs1 = scanner.nextInt();
        //TODO jakos sprawdź kurs czy nie za niski
        System.out.println("Podaj kurs na zespół nr 2");
        int kurs2 = scanner.nextInt();
        System.out.println("Podaj kurs na remis");
        int tie = scanner.nextInt();
        //TODO sprawdź czy Team jakiś nie gra tego samego dnia (opcjonalnie)

        //TODO dodaj mecz do tabeli Mecz
        scanner.close();
    }
}