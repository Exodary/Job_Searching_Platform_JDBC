package view.category;

import view.EntityDialog;

import java.util.Scanner;

public class IdOfCategoryDialog implements EntityDialog<Long> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public Long input() {
        System.out.println("Enter id of category");
        var choice = sc.nextLong();
        return choice;
    }
}
