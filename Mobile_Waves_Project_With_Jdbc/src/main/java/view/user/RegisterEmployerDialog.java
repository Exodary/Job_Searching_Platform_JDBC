package view.user;

import model.User;
import view.EntityDialog;

import java.util.Scanner;

public class RegisterEmployerDialog implements EntityDialog {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public User input() {
        var employer = new User();
        while (employer.getEmail() == null) {
            System.out.println("Enter valid email:");
            var ans = sc.nextLine();
            employer.setEmail(ans);
        }
        while (employer.getUsername() == null) {
            System.out.println("Enter username:");
            var ans = sc.nextLine();
            if (ans.length() < 4 || ans.length() > 15) {
                System.out.println("Error: Username length should be between 4 and 15 characters.");
            } else {
                employer.setUsername(ans);
            }
        }
        while (employer.getPassword() == null) {
            System.out.println("Enter valid password:");
            var ans = sc.nextLine();
            if (ans.length() < 8 || ans.length() > 15) {
                System.out.println("Error: Password length should be between 8 and 15 characters.");
            } else {
                employer.setPassword(ans);
            }
        }
        return employer;
    }
}
