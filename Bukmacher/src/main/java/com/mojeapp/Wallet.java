package com.mojeapp;

import java.util.Scanner;

public class Wallet {

    public Wallet(String user){
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Co chcesz zrobić?");
            System.out.println("1. Sprawdź stan portfela");
            System.out.println("2. Wpłać");
            System.out.println("3. Wypłać");
            System.out.println("4. Wróć");
            int activity = scanner.nextInt();
            int value;
            switch (activity) {
                case 1:
                    //TODO selectuj stan portfela tego usera
                    break;
                case 2:
                    System.out.println("Podaj kwote do wpłacenia");
                    value = scanner.nextInt();
                    //TODO dodaj userowi wpłacana kwote
                    break;
                case 3:
                    System.out.println("Podaj kwote do wypłacenia");
                    value = scanner.nextInt();
                    //TODO odejmij userowi wyplacana kwote
                    break;
                case 4:
                    scanner.close();
                    return;
                default:
                    System.out.println("Nie wybrano opcji spośród podanych!");
                    break;
            }
        }
    }
}