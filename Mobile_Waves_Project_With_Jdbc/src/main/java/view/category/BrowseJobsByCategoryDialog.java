package view.category;

import view.EntityDialog;

import java.util.Scanner;

public class BrowseJobsByCategoryDialog implements EntityDialog<Long> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public Long input() {
        System.out.println("Enter valid category id");
        var choice = sc.nextLong();
        return choice;
    }
}
