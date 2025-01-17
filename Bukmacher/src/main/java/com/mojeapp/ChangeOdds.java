package com.mojeapp;

import java.util.Scanner;

public class ChangeOdds {

    public ChangeOdds(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj zespół którego mecz chcesz zmienić");
        String team1 = scanner.nextLine();
        //TODO sprawdź czy taki istnieje
        //TODO wypisz wszystkie mecze tego zespołu
        System.out.println("Podaj drugi zespół grający ten mecz");
        String team2 = scanner.nextLine();
        //TODO sprawdź czy taki mecz istnieje
        System.out.println("Podaj kurs zespołu który chcesz zmienić");
        System.out.println("1. Kurs 1");
        System.out.println("2. Kurs 2");
        System.out.println("3. Kurs na remis");
        int activity = scanner.nextInt();
        System.out.println("Podaj kurs docelowy");
        double finalOdd = scanner.nextDouble();
        //TODO zabezpiecz się przed wieloma miejscami po przecinku
        switch (activity) {
            case 1:
                //TODO modyfikuj rekord w tabeli Mecze
                break;
            case 2:
                //TODO to samo tutaj ale kurs 2
                break;
            case 3:
                //TODO analogicznie
                break;
            default:
                System.out.println("Nie wybrano opcji spośród podanych!");
                break;
        }

        scanner.close();
    }
}