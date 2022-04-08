package view.user;

import model.User;
import view.EntityDialog;

import java.util.Scanner;

public class UpdateAllInfoDialog implements EntityDialog<User> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public User input() {
        var user = new User();
        while (user.getUsername() == null) {
            System.out.println("New Username: (Enter 0 if you don't want to change)");
            var ans = sc.nextLine();
            if (ans.equals("0")) {
                break;
            }
            if (ans.length() < 4 || ans.length() > 20) {
                System.out.println("Error: Username length should be between 4 and 20 characters.");
            } else {
                user.setUsername(ans);
            }
        }
        while (user.getPassword() == null) {
            System.out.println("New password: (Enter 0 if you don't want to change)");
            var ans = sc.nextLine();
            if (ans.equals("0")) {
                break;
            }
            if (ans.length() < 8 || ans.length() > 15) {
                System.out.println("Error: Password length should be between 8 and 15 characters.");
            } else {
                user.setPassword(ans);
            }
        }

        return user;
    }
}
