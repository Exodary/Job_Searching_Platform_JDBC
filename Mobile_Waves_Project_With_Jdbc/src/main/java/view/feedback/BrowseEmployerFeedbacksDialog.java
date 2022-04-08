package view.feedback;

import view.EntityDialog;

import java.util.Scanner;

public class BrowseEmployerFeedbacksDialog implements EntityDialog<Long> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public Long input() {
        System.out.println("Enter valid employer id");
        var choice = sc.nextLong();
        return choice;
    }
}
