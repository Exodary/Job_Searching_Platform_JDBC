package view.feedback;

import view.EntityDialog;

import java.util.Scanner;

public class BrowseOwnFeedbacksForEmployerDialog implements EntityDialog<Long> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public Long input() {
        System.out.println("Enter id of your own feedback");
        var choice = sc.nextLong();
        return choice;
    }
}
