package controller;

import exception.NonExistingEntityException;
import lombok.extern.slf4j.Slf4j;
import model.Administrator;
import model.Category;
import model.Role;
import model.User;
import service.CategoryService;
import service.UserService;
import view.Menu;
import view.category.IdOfCategoryDialog;
import view.category.NewCategoryDialog;

import java.util.List;

import static controller.MainController.userLoggedIn;

@Slf4j
public class CategoryController {
    private CategoryService categoryService;
    private UserService userService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    public void init() {
        var menu = new Menu("Category Menu", List.of(
                new Menu.Option("Print All Categories", () -> {
                    var categories = categoryService.getAllCategories();
                    if(categories.isEmpty()){
                        return "There aren't any categories";
                    }
                    for(var category : categories){
                        System.out.printf("| %-3d | %1.50s ", category.getId(), category.getName());
                        System.out.println();
                    }
                    return "Total category count " + categories.size();
                })));

        if (userLoggedIn != null && userLoggedIn.getRole() == Role.ADMIN) {
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("Add New Category", () -> {
                        try {
                            var admin = userService.getAdminById(userLoggedIn.getId());
                        var category = new NewCategoryDialog().input();
                            categoryService.createCategory(category, admin.getId());
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        return String.format("Category added successfully.");

                    }));
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("Edit Category", () -> {
                        if(categoryService.getAllCategories().isEmpty()){
                            return "There aren't any categories";
                        }
                        var categoryChoice = new IdOfCategoryDialog().input();
                        var category = new NewCategoryDialog().input();
                        try {
                            categoryService.updateCategory(categoryChoice, category);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        return String.format("");

                    }));
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("Delete Category", () -> {
                        if(categoryService.getAllCategories().isEmpty()){
                            return "There aren't any categories";
                        }
                        categoryService.getAllCategories().forEach(System.out::println);
                        var admin = userService.getAdminById(userLoggedIn.getId());
                        var categoryChoice = new IdOfCategoryDialog().input();
                        try {
                            categoryService.deleteCategoryById(admin.getId(), categoryChoice);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        return String.format("");

                    }));
        }
        menu.show();
    }
}

