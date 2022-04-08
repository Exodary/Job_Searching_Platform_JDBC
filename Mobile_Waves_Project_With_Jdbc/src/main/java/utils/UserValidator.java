package utils;

import exception.ConstraintViolation;
import exception.ConstraintViolationException;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Regex.Regexes.emailRegex;
import static Regex.Regexes.passwordRegex;

public class UserValidator {
    public void validate(User user) throws ConstraintViolationException {
        List<ConstraintViolation> violations = new ArrayList<>();

        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(user.getEmail());
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        Matcher passwordMatcher = passwordPattern.matcher(user.getPassword());

        var usernameLength  = user.getUsername().trim().length();
        if(usernameLength < 4 || usernameLength > 20 ){
            violations.add(
                    new ConstraintViolation(user.getClass().getName(), "username", user.getUsername(),
                            "Username length should be between 4 and 20 characters."));
        }

        if(!emailMatcher.matches()){
            violations.add(
                    new ConstraintViolation(user.getClass().getName(), "email", user.getEmail(),
                            "Email should be valid."));
        }

        var passwordLength  = user.getPassword().trim().length();
        if(passwordLength < 8 || passwordLength > 15 ){
            violations.add(
            new ConstraintViolation(user.getClass().getName(), "password", user.getPassword(),
                    "Password length should be between 8 and 15 characters."));
        }


        if(!(passwordMatcher.matches())){
            violations.add(
            new ConstraintViolation(user.getClass().getName(), "password", user.getPassword(),
                    "Password should include at least one digit, one capital letter " +
                            "and one sign different than letter or digit."));
        }

        if(violations.size() > 0) {
            throw new ConstraintViolationException("Invalid user field", violations);
        }
    }

    public void validateUpdate(User user) throws ConstraintViolationException {
        List<ConstraintViolation> violations = new ArrayList<>();

        if(user.getUsername() != null) {
            var usernameLength  = user.getUsername().trim().length();
            if (usernameLength < 4 || usernameLength > 20) {
                violations.add(
                        new ConstraintViolation(user.getClass().getName(), "username", user.getUsername(),
                                "Username length should be between 4 and 20 characters."));
            }
        }

        if(user.getPassword() != null) {
            var passwordLength  = user.getPassword().trim().length();
            if (passwordLength < 8 || passwordLength > 15) {
                violations.add(
                new ConstraintViolation(user.getClass().getName(), "password", user.getPassword(),
                        "Password length should be between 8 and 15 characters."));
            }
        }


        if(user.getPassword() != null) {
            Pattern passwordPattern = Pattern.compile(passwordRegex);
            Matcher passwordMatcher = passwordPattern.matcher(user.getPassword());
            if (!(passwordMatcher.matches())) {
                violations.add(
                new ConstraintViolation(user.getClass().getName(), "password", user.getPassword(),
                        "Password should include at least one digit, one capital letter " +
                                "and one sign different than letter or digit."));
            }
        }

        if(user.getEmail() != null){
            violations.add(
            new ConstraintViolation(user.getClass().getName(), "email", user.getEmail(),
                    "Invalid email"));
        }

        if(violations.size() > 0) {
            throw new ConstraintViolationException("Invalid user field", violations);
        }
    }

}
