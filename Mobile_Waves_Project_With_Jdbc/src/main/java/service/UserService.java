package service;

import exception.InvalidEntityDataException;
import exception.InvalidOperationException;
import exception.NonExistingEntityException;
import model.*;

import java.util.Collection;
import java.util.List;

public interface UserService {
    Collection<User> getAllUsers();
    User getUserById(Long id) throws NonExistingEntityException;
    Employer getEmployerById(Long id) throws NonExistingEntityException;
    Applicant getApplicantById(Long id) throws NonExistingEntityException;
    Administrator getAdminById(Long id) throws NonExistingEntityException;
    User findByEmailAndPassword(String email, String password) throws NonExistingEntityException;
    Administrator addUserAsAdmin(User user) throws InvalidEntityDataException;
    Applicant addUserAsApplicant(User user) throws InvalidEntityDataException;
    Employer addUserAsEmployer(User user) throws  InvalidEntityDataException;
    User updateInfo(Long oldUserId, User newUser) throws NonExistingEntityException, InvalidEntityDataException;
    Applicant deleteOwnFeedback(Long applicantId, Long feedbackId, Long EmployerId) throws NonExistingEntityException, InvalidOperationException;
    Administrator deleteFeedback(Long adminId, Long feedbackId, Long EmployerId) throws NonExistingEntityException;
    List<Job> getJobForSpecificEmployer(Long employerId) throws NonExistingEntityException;
}
