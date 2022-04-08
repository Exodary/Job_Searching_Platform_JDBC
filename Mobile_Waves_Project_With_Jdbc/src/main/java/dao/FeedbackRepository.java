package dao;

import exception.NonExistingEntityException;
import model.Feedback;

import java.util.Collection;
import java.util.List;

public interface FeedbackRepository {
    Feedback update(Long oldFeedbackId, Feedback newFeedback) throws NonExistingEntityException;
    List<Feedback> getFeedbacksForEmployer(Long employerId);
    List<Feedback> findAllOwnFeedbacks(Long applicantId, Long employerId);
    Feedback createFeedback(Feedback entity, Long applicantId, Long employerId);
    Collection<Feedback> findAll();
    Feedback findById(Long id) throws NonExistingEntityException;
    Feedback deleteById(Long id) throws NonExistingEntityException;
}
