package dao;

import exception.NonExistingEntityException;
import model.Administrator;
import model.Applicant;
import model.Employer;
import model.User;

import java.util.Collection;

public interface UserRepository {
    Collection<User> findAll();
    User create(User entity);
    User findById(Long id);
    User update(Long oldUserId, User newUser) throws NonExistingEntityException;
    User createEmployer(User entity);
    User createApplicant(User entity);
    Applicant findApplicantById(Long id) throws NonExistingEntityException;
    Administrator findAdminById(Long id) throws NonExistingEntityException;
    Employer findEmployerById(Long id) throws NonExistingEntityException;
    User findByEmailAndPassword(String email, String password) throws NonExistingEntityException;
}
