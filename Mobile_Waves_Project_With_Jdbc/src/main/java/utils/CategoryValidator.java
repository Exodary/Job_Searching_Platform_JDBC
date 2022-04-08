package utils;

import exception.ConstraintViolation;
import exception.ConstraintViolationException;
import model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryValidator {
    public void validate(Category category) throws ConstraintViolationException {
        List<ConstraintViolation> violations = new ArrayList<>();

        var categoryName = category.getName().trim().length();
        if(categoryName < 2 || categoryName > 120){
            violations.add(
            new ConstraintViolation(category.getClass().getName(), "name", category.getName(),
                    "Category name length should be between 2 and 120 characters."));
        }

        if (violations.size() > 0) {
            throw new ConstraintViolationException("Invalid user field", violations);
        }
    }
}
