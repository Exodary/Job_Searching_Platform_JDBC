package view.feedback;

import model.Feedback;
import view.EntityDialog;

import java.util.Scanner;

public class NewFeedbackDialog implements EntityDialog<Feedback> {
    public static Scanner sc = new Scanner(System.in);


    @Override
    public Feedback input() {
        var feedback = new Feedback();
        while (feedback.getDescription() == null) {
            System.out.println("Feedback Description:");
            var ans = sc.nextLine();
            if (ans.length() < 3 || ans.length() > 2500) {
                System.out.println("Error: The Feedback description should be between 3 and 2500 characters.");
            } else {
                feedback.setDescription(ans);
            }
        }
        return feedback;
    }
}
