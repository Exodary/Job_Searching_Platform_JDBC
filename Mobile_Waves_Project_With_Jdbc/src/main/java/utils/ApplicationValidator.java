package utils;

import exception.ConstraintViolation;
import exception.ConstraintViolationException;
import model.Application;

import java.util.ArrayList;
import java.util.List;

public class ApplicationValidator {
    public void validate(Application application) throws ConstraintViolationException {
        List<ConstraintViolation> violations = new ArrayList<>();


        var firstNameLength = application.getFirstName().trim().length();
        if(firstNameLength < 2 || firstNameLength > 15){
            violations.add(
            new ConstraintViolation(application.getClass().getName(), "firstName", application.getFirstName(),
                    "First name length should be between 2 and 15 characters."));
        }

        var lastNameLength = application.getLastName().trim().length();
        if(lastNameLength < 2 || lastNameLength > 15){
            violations.add(
            new ConstraintViolation(application.getClass().getName(), "lastName", application.getLastName(),
                    "Last name length should be between 2 and 15 characters."));
        }


        if (violations.size() > 0) {
            throw new ConstraintViolationException("Invalid user field", violations);
        }
    }
}
