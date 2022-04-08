package view.job;

import view.EntityDialog;

import java.util.Scanner;

public class BrowseJobDetailsDialog implements EntityDialog<Long> {
    public static Scanner sc = new Scanner(System.in);
    @Override
    public Long input() {
        System.out.println("Enter job id");
        var choice = sc.nextLong();
        return choice;
    }
}
