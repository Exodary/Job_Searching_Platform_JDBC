package view.job;

import view.EntityDialog;

import java.util.Scanner;

public class KeywordSearchJobsDialog implements EntityDialog<String> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public String input() {
        System.out.println("Enter keyword");
        var keyword = sc.nextLine();
        return keyword;
    }
}
