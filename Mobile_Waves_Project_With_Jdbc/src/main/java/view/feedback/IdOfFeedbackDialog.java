package view.feedback;

import view.EntityDialog;

import java.util.Scanner;

public class IdOfFeedbackDialog implements EntityDialog<Long> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public Long input() {
        System.out.println("Enter id of feedback");
        var choice = sc.nextLong();
        return choice;
    }
}
