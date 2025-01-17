package com.mojeapp;

import java.util.Scanner;

public class DeleteAccount {

    public DeleteAccount(String user){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Na pewno? T/N");
        String decision = scanner.nextLine();
        if (decision == "T"){
            //TODO usun z bazy tego usera
        }
        scanner.close();

    }
}