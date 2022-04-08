package service;

import exception.InvalidEntityDataException;
import exception.NonExistingEntityException;
import model.Category;

import java.util.Collection;

public interface CategoryService {
    Collection<Category> getAllCategories();
    Category findById(Long categoryId) throws NonExistingEntityException;
    Category createCategory(Category category, Long adminId) throws InvalidEntityDataException, NonExistingEntityException;
    Category updateCategory(Long oldCategoryId, Category newCategory) throws NonExistingEntityException, InvalidEntityDataException;
    void deleteCategoryById(Long adminId, Long id) throws NonExistingEntityException;
    Category updateCategoryFromList(Long choice) throws NonExistingEntityException;
    void updateJobCategory(Long oldJobId, Long categoryId) throws NonExistingEntityException;
}
