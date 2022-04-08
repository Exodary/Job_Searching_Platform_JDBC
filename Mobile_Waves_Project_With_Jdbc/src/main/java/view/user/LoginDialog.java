package view.user;

import model.User;
import view.EntityDialog;

import java.util.Scanner;

public class LoginDialog implements EntityDialog {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public User input() {
            var user = new User();
            System.out.println("Enter email:");
            var ans = sc.nextLine();
            user.setEmail(ans);

            System.out.println("Enter valid password:");
            var ansPass = sc.nextLine();
            user.setPassword(ansPass);

        return user;
    }
}

