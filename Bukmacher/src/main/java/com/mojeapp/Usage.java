package com.mojeapp;

import java.util.Scanner;

public class Usage {

    public Usage(String user){
        String thisuser = user;
        Scanner scanner = new Scanner(System.in);
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
            int activity = scanner.nextInt();
            switch (activity) {
                case 1:
                    new Wallet(thisuser);
                    break;
                case 2:
                    
                    break;
            
                case 3:
                    new DeleteAccount(thisuser);            
                case 4:
                    scanner.close();
                    return;            
                case 5:
                    
                    break;
            
                case 6:
                    
                    break;
            

                default:
                    System.out.println("Nie podano opcji spośród podanych!");
                    break;
            }
        }
    }
}