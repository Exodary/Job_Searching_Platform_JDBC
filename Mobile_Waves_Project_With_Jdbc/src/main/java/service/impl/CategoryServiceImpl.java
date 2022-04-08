package service.impl;

import dao.CategoryRepository;
import dao.JobRepository;
import dao.UserRepository;
import exception.ConstraintViolationException;
import exception.InvalidEntityDataException;
import exception.NonExistingEntityException;
import model.Administrator;
import model.Category;
import model.Role;
import model.User;
import service.CategoryService;
import utils.CategoryValidator;

import java.util.Collection;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final CategoryValidator categoryValidator;

    public CategoryServiceImpl(CategoryRepository categoryRepo, UserRepository userRepository, JobRepository jobRepository) {
        this.categoryRepo = categoryRepo;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.categoryValidator = new CategoryValidator();
    }


    @Override
    public Collection<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category findById(Long categoryId) throws NonExistingEntityException {
        var category = categoryRepo.findById(categoryId);

        if(category == null){
            throw new NonExistingEntityException("Category with Id='" + categoryId + "' doesn't exist.");
        }
        return category;
    }

    @Override
    public Category createCategory(Category category, Long adminId) throws InvalidEntityDataException, NonExistingEntityException {

        var admin = userRepository.findAdminById(adminId);

        if(admin == null){
            throw new NonExistingEntityException("Admin with Id='" + adminId + "' doesn't exist.");
        }

        if(!(admin.getRole().equals(Role.ADMIN))){
            throw new NonExistingEntityException("Admin with Id='" + adminId + "' doesn't exist.");
        }

        try {
            categoryValidator.validate(category);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error creating category with name '%s'",
                    category.getName(),
                    ex));
        }

        validateCategoryNameUniqueness(category.getName());

        List<Category> managedCategories = (admin).getManagedCategories();
        managedCategories.add(category);
        admin.setManagedCategories(managedCategories);

        category.setAuthor(admin);

        return categoryRepo.create(category, adminId);
    }


    @Override
    public Category updateCategory(Long oldCategoryId, Category newCategory) throws NonExistingEntityException, InvalidEntityDataException {

        try {
            categoryValidator.validate(newCategory);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error updating category with name '%s'",
                    newCategory.getName(),
                    ex));
        }

       return categoryRepo.update(oldCategoryId, newCategory);
    }

    public Category updateCategoryFromList(Long choice) throws NonExistingEntityException {
        var categories = categoryRepo.findAll().stream()
                .filter(category -> category.getId().equals(choice))
                .toList();
        if(categories.isEmpty()){
            throw new NonExistingEntityException("Category with Id='" + choice + "' doesn't exist");
        }

        var category = categoryRepo.findById(categories.get(0).getId());

        return category;
    }

    @Override
    public void updateJobCategory(Long oldJobId, Long categoryId) throws NonExistingEntityException{

        jobRepository.updateJobCategory(oldJobId, categoryId);
    }

    @Override
    public void deleteCategoryById(Long adminId,Long id) throws NonExistingEntityException {

        var admin = userRepository.findAdminById(adminId);
        if(admin == null){
            throw new NonExistingEntityException("Admin with Id='" + adminId + "' doesn't exist.");
        }
        if(!(admin.getRole().equals(Role.ADMIN))){
            throw new NonExistingEntityException("Admin with Id='" + adminId + "' doesn't exist.");
        }
        var category = categoryRepo.findById(id);
        if(category == null){
            throw new NonExistingEntityException("Category with Id='" + id + "' doesn't exist.");
        }

/*        jobRepository.findAll().stream()
                .map(job -> {
                    if(job.getCategory().equals(category)){
                        job.setCategory(null);
                    return job;
                }});*/

        admin = category.getAuthor();
        List<Category> categories = admin.getManagedCategories();
        categories.remove(category);
        admin.setManagedCategories(categories);


         categoryRepo.deleteById(id);
    }

    public void validateCategoryNameUniqueness(String name) throws InvalidEntityDataException {

        List<Category> allCategories = categoryRepo.findAll().stream().toList();
        for(Category category : allCategories) {
            if (category.getName().equals(name)) {
                throw new InvalidEntityDataException("Category with that email'" + name + "' already exists.");
            }
        }
    }
}

