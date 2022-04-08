package view.cv;

import model.CV;
import model.Gender;
import view.EntityDialog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UpdateCVDialog implements EntityDialog<CV> {
    public static Scanner sc = new Scanner(System.in);


    @Override
    public CV input() {
        var cv = new CV();
        while (cv.getFirstName() == null) {
            System.out.println("First name: (Enter 0 if you don't want to change)");
            var ans = sc.nextLine();
            if (ans.equals("0")) {
                break;
            }
            if (ans.length() < 2 || ans.length() > 15) {
                System.out.println("Error: First name should be between 2 and 15 characters.");
            } else {
                cv.setFirstName(ans);
            }
        }
        while (cv.getLastName() == null) {
            System.out.println("Last name: (Enter 0 if you don't want to change)");
            var ans = sc.nextLine();
            if (ans.equals("0")) {
                break;
            }
            if (ans.length() < 2 || ans.length() > 15) {
                System.out.println("Error: Last name should be between 2 and 15 characters.");
            } else {
                cv.setLastName(ans);
            }
        }
        while (cv.getEmail() == null) {
            System.out.println("Email: (Enter 0 if you don't want to change)");
            var ans = sc.nextLine();
            if (ans.equals("0")) {
                break;
            }
            if (ans.length() < 3 || ans.length() > 30) {
                System.out.println("Error: The email should be between 3 and 30 characters.");
            } else {
                cv.setEmail(ans);
            }
        }
        while (cv.getBirthDate() == null) {
            System.out.println("Birth Date (ex. 12.07.1980): (Enter 0 if you don't want to change)");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            var ans = sc.nextLine();
            if (ans.equals("0")) {
                break;
            }
            try {
                if (LocalDate.parse(ans, dtf).isAfter(LocalDate.now())) {
                    System.out.println("Error: Invalid Birth date");
                } else {
                    cv.setBirthDate(LocalDate.parse(ans, dtf));
                }
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid format - numbers only.");
            }
        }
        while (cv.getGender() == null) {
            System.out.println("Gender(Male or Female): (Enter 0 if you don't want to change)");
            var ans = sc.nextLine().toLowerCase();
            if (ans.equals("0")) {
                break;
            }
            if (!(ans.equals("male") || (ans.equals("female")))) {
                System.out.println("Error: Enter male or female gender.");
            } else if(ans.equals("male")){
                cv.setGender(Gender.MALE);
            }
            else{
                cv.setGender(Gender.FEMALE);
            }
        }
        while (cv.getPhoneNumber() == null) {
            System.out.println("Phone number: (Enter 0 if you don't want to change)");
            var ans = sc.nextLine();
            if (ans.equals("0")) {
                break;
            }
            if (ans.length() < 4 || ans.length() > 15) {
                System.out.println("Error: The phone number should be between 4 and 15 characters.");
            } else {
                cv.setPhoneNumber(ans);
            }
        }
        while (cv.getLocation() == null) {
            System.out.println("Location: (Enter 0 if you don't want to change)");
            var ans = sc.nextLine();
            if (ans.equals("0")) {
                break;
            }
            if (ans.length() < 2 || ans.length() > 50) {
                System.out.println("Error: The location should be between 2 and 50 characters.");
            } else {
                cv.setLocation(ans);
            }
        }
        while (cv.getEducation() == null) {
            System.out.println("Education: (Enter 0 if you don't want to change)");
            var ans = sc.nextLine();
            if (ans.equals("0")) {
                break;
            }
            if (ans.length() < 5 || ans.length() > 1000) {
                System.out.println("Error: Education should be between 5 and 1000 characters.");
            } else {
                cv.setEducation(ans);
            }
        }
        while (cv.getWorkExperience() == null) {
            System.out.println("Work Experience: (Enter 0 if you don't want to change)");
            var ans = sc.nextLine();
            if (ans.equals("0")) {
                break;
            }
            if (ans.length() < 5 || ans.length() > 5000) {
                System.out.println("Error: Work experience should be between 5 and 5000 characters.");
            } else {
                cv.setWorkExperience(ans);
            }
        }
        while (cv.getPictureUrl() == null) {
            System.out.println("Picture URL: (Enter 0 if you don't want to change)");
            var ans = sc.nextLine();
            if (ans.equals("0")) {
                break;
            }
            cv.setPictureUrl(ans);
        }
        return cv;
    }
}
