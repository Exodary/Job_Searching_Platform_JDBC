package service.impl;

import dao.FeedbackRepository;
import dao.UserRepository;
import exception.ConstraintViolationException;
import exception.InvalidEntityDataException;
import exception.InvalidOperationException;
import exception.NonExistingEntityException;
import model.Applicant;
import model.Employer;
import model.Feedback;
import model.Role;
import service.FeedbackService;
import utils.FeedbackValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepo;
    private final UserRepository userRepository;
    private final FeedbackValidator feedbackValidator;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepo, UserRepository userRepository) {
        this.feedbackRepo = feedbackRepo;
        this.userRepository = userRepository;
        this.feedbackValidator = new FeedbackValidator();
    }



    @Override
    public Collection<Feedback> getAllFeedbacks() {
        return feedbackRepo.findAll();
    }

    @Override
    public Feedback findById(Long feedbackId) throws NonExistingEntityException {

        var feedback = feedbackRepo.findById(feedbackId);
        if(feedback == null){
            throw new NonExistingEntityException("Feedback with Id='" + feedbackId + "' doesn't exist.");
        }

        return feedback;
    }

    @Override
    public Feedback updateOwnFeedback(Long applicantId, Long oldFeedbackId, Feedback newFeedback) throws NonExistingEntityException, InvalidEntityDataException, InvalidOperationException {

        var applicant = userRepository.findApplicantById(applicantId);
        if(applicant == null){
            throw new NonExistingEntityException("Applicant with Id='" + applicantId + "' doesn't exist.");
        }
        if(!(applicant.getRole().equals(Role.APPLICANT))){
            throw new NonExistingEntityException("Applicant with Id='" + applicantId + "' doesn't exist.");
        }
        var feedback = feedbackRepo.findById(oldFeedbackId);
        if(feedback == null){
            throw new NonExistingEntityException("Feedback with Id='" + oldFeedbackId + "' doesn't exist.");
        }

        try {
            feedbackValidator.validate(newFeedback);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error updating feedback '%d'", oldFeedbackId,
                    ex));
        }

        var foundFeedback = new ArrayList<Feedback>();

        for(Feedback feedback1 : applicant.getFeedbacks()){
            if(feedback1.getId().equals(oldFeedbackId)){
                foundFeedback.add(feedback1);
            }
        }

        if(foundFeedback.isEmpty()){
            throw new InvalidOperationException("You can update only your feedback.");
        }


        return feedbackRepo.update(oldFeedbackId, newFeedback);
    }

    @Override
    public Feedback updateFeedback(Long oldFeedbackId, Feedback newFeedback) throws NonExistingEntityException, InvalidEntityDataException {

        var feedback = feedbackRepo.findById(oldFeedbackId);
        if(feedback == null){
            throw new NonExistingEntityException("Feedback with Id='" + oldFeedbackId + "' doesn't exist.");
        }

        try {
            feedbackValidator.validate(newFeedback);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error updating feedback '%d'", oldFeedbackId,
                    ex));
        }

        return feedbackRepo.update(oldFeedbackId, newFeedback);
    }

    @Override
    public List<Feedback> getFeedbacksForSpecificEmployer(Long employerId) throws NonExistingEntityException {
        var employer = userRepository.findEmployerById(employerId);

        if(employer == null){
            throw new NonExistingEntityException("Employer with Id='" + employerId + "' doesn't exist.");
        }

        if(!(employer.getRole().equals(Role.EMPLOYER))){
            throw new NonExistingEntityException("Employer with Id='" + employerId + "' doesn't exist.");
        }

        return feedbackRepo.getFeedbacksForEmployer(employerId);
    }

    @Override
    public List<Feedback> getOwnFeedbacksForSpecificEmployer(Long applicantId, Long employerId) throws NonExistingEntityException {
        var employer = userRepository.findEmployerById(employerId);

        if(employer == null){
            throw new NonExistingEntityException("Employer with Id='" + employerId + "' doesn't exist.");
        }

        if(!(employer.getRole().equals(Role.EMPLOYER))){
            throw new NonExistingEntityException("Employer with Id='" + employerId + "' doesn't exist.");
        }

        var applicant = userRepository.findApplicantById(applicantId);

        if(applicant == null){
            throw new NonExistingEntityException("Applicant with Id='" + applicantId + "' doesn't exist.");
        }

        if(!(applicant.getRole().equals(Role.APPLICANT))){
            throw new NonExistingEntityException("Applicant with Id='" + applicantId + "' doesn't exist.");
        }

        feedbackRepo.findAllOwnFeedbacks(applicantId, employerId);


       return feedbackRepo.findAllOwnFeedbacks(applicantId, employerId);
    }

    @Override
    public Feedback createFeedbackWithEverything(Feedback feedback, Long applicantId, Long employerId) throws InvalidEntityDataException, NonExistingEntityException {
        userRepository.findEmployerById(employerId);

        var applicant = userRepository.findApplicantById(applicantId);

        try {

            feedbackValidator.validate(feedback);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error creating feedback '%s'", feedback.getId()),
                    ex);
        }

        var feedbacks = applicant.getFeedbacks();
        feedbacks.add(feedback);
        applicant.setFeedbacks(feedbacks);

        return feedbackRepo.createFeedback(feedback, applicantId, employerId);
    }

}
