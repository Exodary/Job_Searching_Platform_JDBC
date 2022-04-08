package model.mock;

import model.Category;

public class MockCategories {
    public static final Category[] MOCK_CATEGORIES;
    static {
        MOCK_CATEGORIES = new Category[]{
                new Category("IT"),
                new Category("Healthcare"),
                new Category("Hospitality Industry"),
                new Category("Logistics")
        };
    }
}
