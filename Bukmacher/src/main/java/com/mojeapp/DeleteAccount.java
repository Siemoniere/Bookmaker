package com.mojeapp;

import java.util.Scanner;

public class DeleteAccount {
    private String decision = "";
    public DeleteAccount(String user, Scanner scanner){
        SecureScanner secureScanner = new SecureScanner(scanner);
        System.out.println("Na pewno? T/N");
        this.decision = secureScanner.nextSecureLine();
        if (this.decision.equals("T")){
            System.out.println("Usunięto konto!");
            //TODO usun z bazy tego usera
        }
    }
    public boolean getDecision() {
        return this.decision.equals("T");
    }
}