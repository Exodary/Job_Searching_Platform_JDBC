package view.job;

import view.EntityDialog;

import java.util.Scanner;


public class ApplicationJobDialog implements EntityDialog<Boolean> {
    public static Scanner sc = new Scanner(System.in);
    @Override
    public Boolean input() {
        System.out.println("Do you wish to apply for the current job - YES OR NO");
        var choice = sc.nextLine().toLowerCase();
        Boolean userChoice = false;
        if(choice.equals("yes")){
            userChoice = true;
        }
        return userChoice;
    }
}
