package view.cv;

import model.CV;
import model.Gender;
import view.EntityDialog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class NewCvDialog implements EntityDialog<CV> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public CV input() {
        var cv = new CV();
        while (cv.getFirstName() == null) {
            System.out.println("First name:");
            var ans = sc.nextLine();
            if (ans.length() < 2 || ans.length() > 15) {
                System.out.println("Error: First name should be between 2 and 15 characters.");
            } else {
                cv.setFirstName(ans);
            }
        }
        while (cv.getLastName() == null) {
            System.out.println("Last name:");
            var ans = sc.nextLine();
            if (ans.length() < 2 || ans.length() > 15) {
                System.out.println("Error: Last name should be between 2 and 15 characters.");
            } else {
                cv.setLastName(ans);
            }
        }
        while (cv.getEmail() == null) {
            System.out.println("Email: ");
            var ans = sc.nextLine();
            if (ans.length() < 3 || ans.length() > 30) {
                System.out.println("Error: The email should be between 3 and 30 characters.");
            } else {
                cv.setEmail(ans);
            }
        }
        while (cv.getBirthDate() == null) {
            System.out.println("Birth Date (ex. 1990-10-01): ");
            var ans = sc.nextLine();
            cv.setBirthDate(LocalDate.parse(ans));

        }
        while (cv.getGender() == null) {
            System.out.println("Gender(Male or Female):");
            var ans = sc.nextLine().toLowerCase();
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
            System.out.println("Phone number:");
            var ans = sc.nextLine();
            if (ans.length() < 4 || ans.length() > 15) {
                System.out.println("Error: The phone number should be between 4 and 15 characters.");
            } else {
                cv.setPhoneNumber(ans);
            }
        }
        while (cv.getLocation() == null) {
            System.out.println("Location:");
            var ans = sc.nextLine();
            if (ans.length() < 2 || ans.length() > 50) {
                System.out.println("Error: The location should be between 2 and 50 characters.");
            } else {
                cv.setLocation(ans);
            }
        }
        while (cv.getEducation() == null) {
            System.out.println("Education:");
            var ans = sc.nextLine();
            if (ans.length() < 5 || ans.length() > 1000) {
                System.out.println("Error: Education should be between 5 and 1000 characters.");
            } else {
                cv.setEducation(ans);
            }
        }
        while (cv.getWorkExperience() == null) {
            System.out.println("Work Experience:");
            var ans = sc.nextLine();
            if (ans.length() < 5 || ans.length() > 5000) {
                System.out.println("Error: Work experience should be between 5 and 5000 characters.");
            } else {
                cv.setWorkExperience(ans);
            }
        }
        while (cv.getPictureUrl() == null) {
            System.out.println("Picture URL:");
            var ans = sc.nextLine();
                cv.setPictureUrl(ans);
        }
        return cv;
    }
}
