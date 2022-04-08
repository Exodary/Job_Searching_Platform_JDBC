package service.impl;

import dao.CVRepository;
import dao.CategoryRepository;
import dao.FeedbackRepository;
import dao.UserRepository;
import exception.ConstraintViolationException;
import exception.InvalidEntityDataException;
import exception.InvalidOperationException;
import exception.NonExistingEntityException;
import model.*;
import service.UserService;
import utils.UserValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CVRepository cvRepository;
    private final CategoryRepository categoryRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserValidator userValidator;

    public UserServiceImpl(UserRepository userRepository, CVRepository cvRepository,
                           CategoryRepository categoryRepository, FeedbackRepository feedbackRepository) {
        this.userRepository = userRepository;
        this.cvRepository = cvRepository;
        this.categoryRepository = categoryRepository;
        this.feedbackRepository = feedbackRepository;
        this.userValidator = new UserValidator();
    }

    public UserServiceImpl(UserRepository userRepository, CVRepository cvRepository,
                           CategoryRepository categoryRepository, FeedbackRepository feedbackRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.cvRepository = cvRepository;
        this.categoryRepository = categoryRepository;
        this.feedbackRepository = feedbackRepository;
        this.userValidator = userValidator;
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) throws NonExistingEntityException {
        var user = userRepository.findById(id);

        if(user == null){
            throw new NonExistingEntityException("User with Id='" + id + "' doesn't exist.");
        }

        return user;
    }

    @Override
    public Employer getEmployerById(Long id) throws NonExistingEntityException {
        return userRepository.findEmployerById(id);
    }

    @Override
    public Applicant getApplicantById(Long id) throws NonExistingEntityException {
        return userRepository.findApplicantById(id);
    }

    @Override
    public Administrator getAdminById(Long id) throws NonExistingEntityException {
        return userRepository.findAdminById(id);
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws NonExistingEntityException {

        var user = userRepository.findByEmailAndPassword(email, password);

        if(user == null){
            throw new NonExistingEntityException("Wrong username or password");
        }

        return user;
    }

    @Override
    public Administrator addUserAsAdmin(User user) throws InvalidEntityDataException {

        try {
            userValidator.validate(user);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error creating admin '%s'", user.getEmail()),
                    ex);
        }

        validateEmailUniqueness(user.getEmail());

        var newAdmin = new Administrator(user.getEmail(), user.getUsername(), user.getPassword(), new ArrayList<>(),
                new ArrayList<>());

        userRepository.create(newAdmin);

        return  newAdmin;
    }

    @Override
    public Applicant addUserAsApplicant(User user) throws InvalidEntityDataException {

        try {
            userValidator.validate(user);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error creating applicant '%s'", user.getEmail()),
                    ex);
        }

        validateEmailUniqueness(user.getEmail());

        var newApplicant = new Applicant(user.getEmail(), user.getUsername(), user.getPassword(), new ArrayList<>(),
                new ArrayList<>());

        userRepository.createApplicant(newApplicant);

        return  newApplicant;
    }

    @Override
    public Employer addUserAsEmployer(User user) throws InvalidEntityDataException {

        try {
            userValidator.validate(user);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error creating employer '%s'", user.getEmail()),
                    ex);
        }

        validateEmailUniqueness(user.getEmail());

        var newEmployer = new Employer(user.getEmail(), user.getUsername(), user.getPassword(), new ArrayList<>(),
                new ArrayList<>());

        userRepository.createEmployer(newEmployer);

        return  newEmployer;
    }

    @Override
    public User updateInfo(Long oldUserId, User newUser) throws NonExistingEntityException, InvalidEntityDataException {
        var user = userRepository.findById(oldUserId);

        try {
            userValidator.validateUpdate(newUser);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error updating employer '%s'", user.getEmail()),
                    ex);
        }

        if(user == null){
            throw new NonExistingEntityException("User with Id='" + oldUserId + "' doesn't exist.");
        }

        return userRepository.update(oldUserId, newUser);
    }

    @Override
    public Applicant deleteOwnFeedback(Long applicantId, Long feedbackId, Long EmployerId) throws NonExistingEntityException, InvalidOperationException {
        var applicant = userRepository.findApplicantById(applicantId);
        if(applicant == null){
            throw new NonExistingEntityException("Applicant with Id='" + applicantId + "' doesn't exist.");
        }
        if(!(applicant.getRole().equals(Role.APPLICANT)))
            throw new NonExistingEntityException("Applicant with Id='" + applicantId + "' doesn't exist.");
        var feedback = feedbackRepository.findById(feedbackId);
        if(feedback == null){
            throw new NonExistingEntityException("Feedback with Id='" + feedbackId + "' doesn't exist.");
        }
        var employer = userRepository.findEmployerById(EmployerId);
        if(employer == null){
            throw new NonExistingEntityException("Employer with Id='" + EmployerId + "' doesn't exist.");
        }

        if(!(employer.getRole().equals(Role.EMPLOYER))){
            throw new NonExistingEntityException("Employer with Id='" + EmployerId + "' doesn't exist.");
        }

        var foundFeedback = new ArrayList<Feedback>();

        for(Feedback feedback1 : applicant.getFeedbacks()){
            if(feedback1.getId().equals(feedbackId)){
                foundFeedback.add(feedback1);
            }
        }

        if(foundFeedback.isEmpty()){
            throw new InvalidOperationException("You can delete only your feedback.");
        }

        var employerFeedback = new ArrayList<Feedback>();

        for(Feedback feedback1 : employer.getOwnFeedbacks()){
            if(feedback1.getId().equals(feedbackId)){
                employerFeedback.add(feedback1);
            }
        }

/*        if(employerFeedback.isEmpty()){
            throw new InvalidOperationException("You don't have any feedbacks for this employer.");
        }*/

        var applicantFeedbacks = applicant.getFeedbacks();
        applicantFeedbacks.remove(feedbackRepository.findById(feedbackId));
        applicant.setFeedbacks(applicantFeedbacks);

        var employerFeedbacks = employer.getOwnFeedbacks();
        employerFeedbacks.remove(feedbackRepository.findById(feedbackId));
        employer.setOwnFeedbacks(employerFeedbacks);

        feedbackRepository.deleteById(feedbackId);

        return applicant;
    }

    @Override
    public Administrator deleteFeedback(Long adminId, Long feedbackId, Long EmployerId) throws NonExistingEntityException {
        var admin = userRepository.findAdminById(adminId);
        if(admin == null){
            throw new NonExistingEntityException("Admin with Id='" + adminId + "' doesn't exist.");
        }
        if(!(admin.getRole().equals(Role.ADMIN))){
            throw new NonExistingEntityException("Admin with Id='" + adminId + "' doesn't exist.");
        }
        var feedback = feedbackRepository.findById(feedbackId);
        if(feedback == null){
            throw new NonExistingEntityException("Feedback with Id='" + feedbackId + "' doesn't exist.");
        }
        var employer = userRepository.findEmployerById(EmployerId);
        if(employer == null){
            throw new NonExistingEntityException("Employer with Id='" + EmployerId + "' doesn't exist.");
        }
        if(!(employer.getRole().equals(Role.EMPLOYER))){
            throw new NonExistingEntityException("Employer with Id='" + EmployerId + "' doesn't exist.");
        }


        var employerFeedback = new ArrayList<Feedback>();

        for(Feedback feedback1 : employer.getOwnFeedbacks()){
            if(feedback1.getId().equals(feedbackId)){
                employerFeedback.add(feedback1);
            }
        }

        if(employerFeedback.isEmpty()){
            try {
                throw new InvalidOperationException("This employer doesn't have feedback with Id'=" + feedbackId
                        + "'.");
            } catch (InvalidOperationException e) {
                e.printStackTrace();
            }
        }

        var applicant = feedback.getApplicant();
        var feedbacksOfApplicant = applicant.getFeedbacks();
        feedbacksOfApplicant.remove(feedback);
        applicant.setFeedbacks(feedbacksOfApplicant);

        var employerFeedbacks = employer.getOwnFeedbacks();
        employerFeedbacks.remove(feedback);
        employer.setOwnFeedbacks(employerFeedbacks);

        feedbackRepository.deleteById(feedbackId);

        return admin;
    }

    @Override
    public List<Job> getJobForSpecificEmployer(Long employerId) throws NonExistingEntityException {
        var employer = userRepository.findEmployerById(employerId);

        return employer.getJobs();
    }


    public void validateEmailUniqueness(String email) throws InvalidEntityDataException {

        List<User> allUsersEmails = userRepository.findAll().stream().toList();
        for(User user : allUsersEmails) {
            if (user.getEmail().equals(email)) {
                throw new InvalidEntityDataException("User with that email'" + email + "' already exists.");
            }
        }
    }
}
