package service;

import exception.InvalidEntityDataException;
import exception.InvalidOperationException;
import exception.NonExistingEntityException;
import model.Feedback;

import java.util.Collection;
import java.util.List;

public interface FeedbackService {
    Collection<Feedback> getAllFeedbacks();
    Feedback findById(Long feedbackId) throws NonExistingEntityException;
    Feedback updateOwnFeedback(Long applicantId, Long oldFeedbackId, Feedback newFeedback) throws NonExistingEntityException, InvalidEntityDataException, InvalidOperationException;
    Feedback updateFeedback(Long oldFeedbackId, Feedback newFeedback) throws NonExistingEntityException, InvalidEntityDataException;
    List<Feedback> getFeedbacksForSpecificEmployer(Long employerId) throws NonExistingEntityException;
    List<Feedback> getOwnFeedbacksForSpecificEmployer(Long applicantId, Long employerId) throws NonExistingEntityException;
    Feedback createFeedbackWithEverything(Feedback feedback, Long applicantId, Long employerId) throws InvalidEntityDataException, NonExistingEntityException;
}
