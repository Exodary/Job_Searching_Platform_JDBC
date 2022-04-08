package dao;

import exception.NonExistingEntityException;
import model.Category;

import java.util.Collection;

public interface CategoryRepository{
    Category findById(Long id) throws NonExistingEntityException;
    Category deleteById(Long id);
    Category update(Long oldCategoryId, Category newCategory) throws NonExistingEntityException;
    Category create(Category entity, Long adminId);
    Collection<Category> findAll();
}
