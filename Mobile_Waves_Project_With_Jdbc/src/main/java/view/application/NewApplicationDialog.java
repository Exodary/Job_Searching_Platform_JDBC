package view.application;

import model.Application;
import view.EntityDialog;

import java.util.Scanner;

public class NewApplicationDialog implements EntityDialog<Application> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public Application input() {
        var application = new Application();
        while (application.getFirstName() == null) {
            System.out.println("First Name:");
            var ans = sc.nextLine();
            if (ans.length() < 2 || ans.length() > 15) {
                System.out.println("Error: The First name should be between 2 and 15 characters.");
            } else {
                application.setFirstName(ans);
            }
        }
        while (application.getLastName() == null) {
            System.out.println("Last Name:");
            var ans = sc.nextLine();
            if (ans.length() < 2 || ans.length() > 15) {
                System.out.println("Error: The Last name should be between 2 and 15 characters.");
            } else {
                application.setLastName(ans);
            }
        }
        return application;
    }
}
