package com.mojeapp;

import java.util.Scanner;

public class Usage {

    public Usage(String user, Scanner scanner){
        SecureScanner secureScanner = new SecureScanner(scanner);
        String thisuser = user;
        while(true){
            System.out.println("Co chcesz zrobić?");
            //TODO sprawdź czy admin
            IsAdmin isAdmin = IsAdmin.YES; ///usun to poźniej
            int option = 1;
            System.out.println((option++) + ". Ustawienia portfela");
            System.out.println((option++) + ". Postaw kupon");
            System.out.println((option++) + ". Usuń konto");
            System.out.println((option++) + ". Wyloguj");
            if(isAdmin == IsAdmin.YES){
                System.out.println((option++) + ". Dodaj mecz");
                System.out.println((option++) + ". Zmień kurs");
            }
            int activity = secureScanner.nextSecureInt();
            switch (activity) {
                case 1:
                    new Wallet(thisuser, scanner);
                    break;
                case 2:
                    new AddCoupon(thisuser, scanner);
                    break;
                case 3:
                    DeleteAccount del = new DeleteAccount(thisuser, scanner);
                    if(del.getDecision()){
                        return;
                    }
                    break;      
                case 4:
                    return;            
                case 5:
                    new AddMatch(scanner);
                    break;
                case 6:
                    new ChangeOdds(scanner);
                    break;
                default:
                    System.out.println("Nie podano opcji spośród podanych!");
                    break;
            }
        }
    }
}