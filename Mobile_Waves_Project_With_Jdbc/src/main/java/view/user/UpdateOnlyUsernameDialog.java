package view.user;

import model.User;
import view.EntityDialog;

import java.util.Scanner;

public class UpdateOnlyUsernameDialog implements EntityDialog<User> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public User input() {
        var user = new User();
        while (user.getUsername() == null) {
            System.out.println("New Username: ");
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

        return user;
    }
}
