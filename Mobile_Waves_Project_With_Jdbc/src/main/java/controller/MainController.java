package controller;

import exception.NonExistingEntityException;
import lombok.extern.slf4j.Slf4j;
import model.*;
import service.JobService;
import service.UserService;
import view.Menu;
import view.user.LoginDialog;
import view.user.RegisterApplicantDialog;
import view.user.RegisterEmployerDialog;

import java.util.List;

@Slf4j
public class MainController {
    private UserService userService;
    private JobService jobService;
    private CategoryController categoryController;
    private JobController jobController;
    private ProfileController profileController;
    private FeedbackController feedbackController;
    public static User userLoggedIn;

    public MainController(UserService userService, JobService jobService, CategoryController categoryController,
                          JobController jobController, ProfileController profileController, FeedbackController feedbackController) {
        this.userService = userService;
        this.jobService = jobService;
        this.categoryController = categoryController;
        this.jobController = jobController;
        this.profileController = profileController;
        this.feedbackController = feedbackController;
    }


    public void init() {
        System.out.println("Job Searching Platform help its users to find their dream job with only few clicks. \n" +
                "The platform provides ability for employers to create job advertisements and the applicant to apply for them. \n" +
                "Applicants can leave feedback for the employer and inn addition to that it allows users to register, and administrators to manage them.");

        var menu = new Menu("Home Menu", List.of(
                new Menu.Option("Register as Applicant", () -> {
                    if(userLoggedIn != null){
                        return "You need to logout first";
                    }
                    var applicant = new RegisterApplicantDialog().input();
                    Applicant createdApplicant =  userService.addUserAsApplicant(applicant);
                    return String.format("Applicant with email = %s registered successfully.",
                            createdApplicant.getEmail());
                }),
                new Menu.Option("Register as Employer", () -> {
                    if(userLoggedIn != null){
                        return "You need to logout first";
                    }
                    var employer = new RegisterEmployerDialog().input();
                    Employer createdEmployer =  userService.addUserAsEmployer(employer);
                    return String.format("Employer with email = %s registered successfully.",
                            createdEmployer.getEmail());
                }),
                new Menu.Option("Login", () -> {
                    if(userLoggedIn != null){
                        return "You need to logout first";
                    }
                    var user = new LoginDialog().input();
                    var loggedInUser = new User();
                    try {
                        loggedInUser =  userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
                        if(loggedInUser.getRole().equals(Role.APPLICANT)){
                            var applicant = new Applicant(loggedInUser.getId(), loggedInUser.getEmail(),
                                    loggedInUser.getUsername(), loggedInUser.getPassword(), Role.APPLICANT);

                            userLoggedIn = applicant;

                            return applicant.toString();
                        }
                        else if(loggedInUser.getRole().equals(Role.EMPLOYER)){
                            var employer = new Employer(loggedInUser.getId(), loggedInUser.getEmail(),
                                    loggedInUser.getUsername(), loggedInUser.getPassword(), Role.EMPLOYER);

                            userLoggedIn = employer;

                            return employer.toString();
                        }
                        else {
                            var admin = new Administrator(loggedInUser.getId(), loggedInUser.getEmail(),
                                    loggedInUser.getUsername(), loggedInUser.getPassword(), Role.ADMIN);

                            userLoggedIn = admin;

                            return admin.toString();
                        }
                    } catch (NonExistingEntityException e) {
                        e.printStackTrace();
                            return "";
                    }
                }),
                new Menu.Option("Jobs", () -> {
                    jobController.init();
                    return "";

                }),
                new Menu.Option("Categories", () -> {
                    categoryController.init();
                    return "";

                }),
                new Menu.Option("Feedbacks", () -> {
                    feedbackController.init();
                    return "";

                }),
                new Menu.Option("Profile", () -> {
                    if(userLoggedIn != null){
                        profileController.init();
                    }
                    else{
                        System.out.println("You are not logged in");
                    }
                    return "";

                }),
                new Menu.Option("Log Out", () -> {
                    userLoggedIn = null;
                    return "Logged out successfully";

                })
        ));
        menu.show();
    }
}
