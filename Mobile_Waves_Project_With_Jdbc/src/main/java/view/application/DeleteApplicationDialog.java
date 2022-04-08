package view.application;

import view.EntityDialog;

import java.util.Scanner;

public class DeleteApplicationDialog implements EntityDialog<Long> {
    public static Scanner sc = new Scanner(System.in);

        public Long input() {
            System.out.println("Enter id of application");
            var choice = sc.nextLong();
            return choice;
        }
}
