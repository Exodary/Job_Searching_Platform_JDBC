package view.user;

import view.EntityDialog;

import java.util.Scanner;

public class GetIdOfUserDialog implements EntityDialog<Long> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public Long input() {
        System.out.println("Enter id of the user");
        var choice = sc.nextLong();
        return choice;
    }
}
