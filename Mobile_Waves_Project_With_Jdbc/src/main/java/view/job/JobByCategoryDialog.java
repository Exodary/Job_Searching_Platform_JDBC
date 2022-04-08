package view.job;

import view.EntityDialog;

import java.util.Scanner;


public class JobByCategoryDialog implements EntityDialog<Boolean> {
    public static Scanner sc = new Scanner(System.in);
    @Override
    public Boolean input() {
        var willBrowse = false;
        System.out.println("Do you wish to browse any of these jobs");
        var ans = sc.nextLine().toLowerCase();
        if(ans.equals("yes")){
            willBrowse = true;
        }
         return willBrowse;
    }
}
