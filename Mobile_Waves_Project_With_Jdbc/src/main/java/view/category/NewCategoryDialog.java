package view.category;

import model.Category;
import view.EntityDialog;

import java.util.Scanner;

public class NewCategoryDialog implements EntityDialog<Category> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public Category input() {
        var category = new Category();
        while (category.getName() == null) {
            System.out.println("Category Name:");
            var ans = sc.nextLine();
            if (ans.length() < 2 || ans.length() > 120) {
                System.out.println("Error: The Category name should be between 2 and 120 characters.");
            } else {
                category.setName(ans);
            }
        }
        return category;
    }
}
