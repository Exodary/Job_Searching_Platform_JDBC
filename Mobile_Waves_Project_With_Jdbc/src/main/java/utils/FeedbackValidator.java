package utils;

import exception.ConstraintViolation;
import exception.ConstraintViolationException;
import model.Feedback;

import java.util.ArrayList;
import java.util.List;

public class FeedbackValidator {
    public void validate(Feedback feedback) throws ConstraintViolationException {
        List<ConstraintViolation> violations = new ArrayList<>();

        var newFeedbackDescription = feedback.getDescription().trim().length();
        if (newFeedbackDescription < 3 || newFeedbackDescription > 2500) {
            violations.add(
            new ConstraintViolation(feedback.getClass().getName(), "description", feedback.getDescription(),
                    "Feedback description length should be between 3 and 2500 characters."));
        }

        if(violations.size() > 0) {
            throw new ConstraintViolationException("Invalid feedback field", violations);
        }
    }
}
