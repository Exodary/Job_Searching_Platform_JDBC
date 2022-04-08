package utils;

import exception.ConstraintViolation;
import exception.ConstraintViolationException;
import model.CV;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Regex.Regexes.*;

public class CVValidator {
    public void validate(CV cv) throws ConstraintViolationException {
        List<ConstraintViolation> violations = new ArrayList<>();

        var firstNameLength = cv.getFirstName().trim().length();
        if (firstNameLength < 2 || firstNameLength > 15) {
            violations.add(
            new ConstraintViolation(cv.getClass().getName(), "firstName", cv.getFirstName(),
                    "First name length should be between 2 and 15 characters."));
        }


        var lastNameLength = cv.getLastName().trim().length();
        if (lastNameLength < 2 || lastNameLength > 15) {
            violations.add(
            new ConstraintViolation(cv.getClass().getName(), "lastName", cv.getLastName(),
                    "Last name length should be between 2 and 15 characters."));
        }

        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(cv.getEmail());
        if (!emailMatcher.matches()) {
            violations.add(
            new ConstraintViolation(cv.getClass().getName(), "email", cv.getEmail(),
                    "Email should be valid."));
        }

        Pattern phoneNumberPattern = Pattern.compile(phoneRegex);
        Matcher phoneNumberMatcher = phoneNumberPattern.matcher(cv.getPhoneNumber());
        if (!phoneNumberMatcher.matches()) {
            violations.add(
            new ConstraintViolation(cv.getClass().getName(), "phoneNumber", cv.getPhoneNumber(),
                    "Phone Number should be valid."));
        }

        var phoneNumberLength = cv.getPhoneNumber().trim().length();
        if (phoneNumberLength < 4 || phoneNumberLength > 15) {
            violations.add(
            new ConstraintViolation(cv.getClass().getName(), "phoneNumber", cv.getPhoneNumber(),
                    "Phone number length should be between 4 and 15 characters."));
        }

        var birthdate = cv.getBirthDate();
        if (birthdate.isAfter(LocalDate.now())) {
            violations.add(
            new ConstraintViolation(cv.getClass().getName(), "birthDate", cv.getBirthDate(),
                    "Invalid birth date"));
        }

        var locationLength = cv.getLocation().trim().length();
        if (locationLength < 2 || locationLength > 50) {
            violations.add(
            new ConstraintViolation(cv.getClass().getName(), "location", cv.getLocation(),
                    "Location length should be between 2 and 50 characters."));
        }

        var educationLength = cv.getEducation().trim().length();
        if (educationLength < 5 || educationLength > 1000) {
            violations.add(
            new ConstraintViolation(cv.getClass().getName(), "education", cv.getEducation(),
                    "Education length should be between 5 and 1000 characters."));
        }

        var workExperienceLength = cv.getWorkExperience().trim().length();
        if (workExperienceLength < 5 || workExperienceLength > 5000) {
            violations.add(
            new ConstraintViolation(cv.getClass().getName(), "workExperience", cv.getWorkExperience(),
                    "Work experience length should be between 5 and 5000 characters."));
        }

        var pictureUrl = cv.getPictureUrl();
        Pattern pattern = Pattern.compile(UrlRegex);
        Matcher matcher = pattern.matcher(pictureUrl);
        if (!matcher.matches()) {
            violations.add(
            new ConstraintViolation(cv.getClass().getName(), "pictureUrl", cv.getPictureUrl(),
                    "URL of the picture should be valid"));
        }

        if (violations.size() > 0) {
            throw new ConstraintViolationException("Invalid user field", violations);
        }
    }

    public void validateUpdate(CV cv) throws ConstraintViolationException {
        List<ConstraintViolation> violations = new ArrayList<>();

        if(cv.getFirstName() != null) {
            var firstNameLength = cv.getFirstName().trim().length();
            if (firstNameLength < 2 || firstNameLength > 15) {
                violations.add(
                        new ConstraintViolation(cv.getClass().getName(), "firstName", cv.getFirstName(),
                                "First name length should be between 2 and 15 characters."));
            }
        }

        if(cv.getLastName() != null) {
            var lastNameLength = cv.getLastName().trim().length();
            if (lastNameLength < 2 || lastNameLength > 15) {
                violations.add(
                        new ConstraintViolation(cv.getClass().getName(), "lastName", cv.getLastName(),
                                "Last name length should be between 2 and 15 characters."));
            }
        }

        if(cv.getEmail() != null) {
            Pattern emailPattern = Pattern.compile(emailRegex);
            Matcher emailMatcher = emailPattern.matcher(cv.getEmail());
            if (!emailMatcher.matches()) {
                violations.add(
                        new ConstraintViolation(cv.getClass().getName(), "email", cv.getEmail(),
                                "Email should be valid."));
            }
        }

        if(cv.getPhoneNumber() != null) {
            Pattern phoneNumberPattern = Pattern.compile(phoneRegex);
            Matcher phoneNumberMatcher = phoneNumberPattern.matcher(cv.getPhoneNumber());
            if (!phoneNumberMatcher.matches()) {
                violations.add(
                        new ConstraintViolation(cv.getClass().getName(), "phoneNumber", cv.getPhoneNumber(),
                                "Phone Number should be valid."));
            }
        }

        if(cv.getPhoneNumber() != null) {
            var phoneNumberLength = cv.getPhoneNumber().trim().length();
            if (phoneNumberLength < 4 || phoneNumberLength > 15) {
                violations.add(
                        new ConstraintViolation(cv.getClass().getName(), "phoneNumber", cv.getPhoneNumber(),
                                "Phone number length should be between 4 and 15 characters."));
            }
        }

        if(cv.getBirthDate() != null) {
            var birthdate = cv.getBirthDate();
            if (birthdate.isAfter(LocalDate.now())) {
                violations.add(
                        new ConstraintViolation(cv.getClass().getName(), "birthDate", cv.getBirthDate(),
                                "Invalid birth date"));
            }
        }

        if(cv.getLocation() != null) {
            var locationLength = cv.getLocation().trim().length();
            if (locationLength < 2 || locationLength > 50) {
                violations.add(
                        new ConstraintViolation(cv.getClass().getName(), "location", cv.getLocation(),
                                "Location length should be between 2 and 50 characters."));
            }
        }

        if(cv.getEducation() != null) {
            var educationLength = cv.getEducation().trim().length();
            if (educationLength < 5 || educationLength > 1000) {
                violations.add(
                        new ConstraintViolation(cv.getClass().getName(), "education", cv.getEducation(),
                                "Education length should be between 5 and 1000 characters."));
            }
        }

        if(cv.getWorkExperience() != null) {
            var workExperienceLength = cv.getWorkExperience().trim().length();
            if (workExperienceLength < 5 || workExperienceLength > 5000) {
                violations.add(
                        new ConstraintViolation(cv.getClass().getName(), "workExperience", cv.getWorkExperience(),
                                "Work experience length should be between 5 and 5000 characters."));
            }
        }

        if(cv.getPictureUrl() != null) {
            var pictureUrl = cv.getPictureUrl();
            Pattern pattern = Pattern.compile(UrlRegex);
            Matcher matcher = pattern.matcher(pictureUrl);
            if (!matcher.matches()) {
                violations.add(
                        new ConstraintViolation(cv.getClass().getName(), "pictureUrl", cv.getPictureUrl(),
                                "URL of the picture should be valid"));
            }
        }

        if (violations.size() > 0) {
            throw new ConstraintViolationException("Invalid user field", violations);
        }
    }
}
