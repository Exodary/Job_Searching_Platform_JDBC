package view.user;

import model.User;
import view.EntityDialog;

import java.util.Scanner;

public class RegisterApplicantDialog implements EntityDialog {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public User input() {
        var applicant = new User();
        while (applicant.getEmail() == null) {
            System.out.println("Enter valid email:");
            var ans = sc.nextLine();
            applicant.setEmail(ans);
        }
        while (applicant.getUsername() == null) {
            System.out.println("Enter username:");
            var ans = sc.nextLine();
            if (ans.length() < 4 || ans.length() > 15) {
                System.out.println("Error: Username length should be between 4 and 15 characters.");
            } else {
                applicant.setUsername(ans);
            }
        }
        while (applicant.getPassword() == null) {
            System.out.println("Enter valid password:");
            var ans = sc.nextLine();
            if (ans.length() < 8 || ans.length() > 15) {
                System.out.println("Error: Password length should be between 8 and 15 characters.");
            } else {
                applicant.setPassword(ans);
            }
        }
        return applicant;
    }
}