package controller;

import exception.InvalidOperationException;
import exception.NonExistingEntityException;
import lombok.extern.slf4j.Slf4j;
import model.Administrator;
import model.Applicant;
import model.Role;
import service.FeedbackService;
import service.UserService;
import view.Menu;
import view.feedback.BrowseEmployerFeedbacksDialog;
import view.feedback.BrowseOwnFeedbacksForEmployerDialog;
import view.feedback.IdOfFeedbackDialog;
import view.feedback.NewFeedbackDialog;

import java.util.List;

import static controller.MainController.userLoggedIn;

@Slf4j
public class FeedbackController {
    private FeedbackService feedbackService;
    private UserService userService;

    public FeedbackController(FeedbackService feedbackService, UserService userService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
    }

    public void init() {
        var menu = new Menu("Feedback Menu", List.of(
                new Menu.Option("View all Employers", () -> {
                    var employers = userService.getAllUsers().stream()
                            .filter(employer -> employer.getRole().equals(Role.EMPLOYER)).toList();
                    if(employers.isEmpty()){
                        return "There aren't any employers.";
                    }
                    for(var employer : employers){
                        System.out.printf("| %-3d | %1.40s", employer.getId(), employer.getUsername());
                        System.out.println();
                    }
                    return String.format("");
                }),
                new Menu.Option("View feedbacks for specific employer", () -> {
                    var employerChoice = new BrowseEmployerFeedbacksDialog().input();
                    try {
                        var feedbacks = feedbackService.getFeedbacksForSpecificEmployer(employerChoice);
                        if(feedbacks.isEmpty()){
                            return "There aren't any feedbacks for this employer.";
                        }
                        feedbacks.forEach(System.out::println);
                    } catch (NonExistingEntityException e) {
                        e.printStackTrace();
                    }
                    return String.format("");
                })
        ));

        if (userLoggedIn != null && userLoggedIn.getRole().equals(Role.APPLICANT)) {
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("Add Feedback for specific employer", () -> {
                        var employerChoice = new BrowseEmployerFeedbacksDialog().input();
                        var applicant = userLoggedIn;
                        var feedback = new NewFeedbackDialog().input();
                        feedbackService.createFeedbackWithEverything(feedback, applicant.getId(), employerChoice);

                        return String.format("Feedback Added successfully.");
                    }));
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("Edit Own Feedback for specific employer", () -> {
                        var user = userService.getApplicantById(userLoggedIn.getId());
                        if(user.getFeedbacks().isEmpty()){
                            return "You haven't written any feedbacks.";
                        }
                        var feedbackChoice = new IdOfFeedbackDialog().input();
                        try {
                            var feedbackFromChoice = feedbackService.findById(feedbackChoice);
                            System.out.println(feedbackFromChoice);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                            return "";
                        }
                        var feedback = new NewFeedbackDialog().input();
                        try {
                            feedbackService.updateOwnFeedback(user.getId(),feedbackChoice, feedback);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        } catch (InvalidOperationException e) {
                            e.printStackTrace();
                        }
                        return String.format("");
                    }));
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("Delete Own Feedback for specific employer", () -> {
                        var applicant = userService.getApplicantById(userLoggedIn.getId());
                        if(applicant.getFeedbacks().isEmpty()){
                            return "You haven't written any feedbacks.";
                        }
                        var employerChoice = new BrowseEmployerFeedbacksDialog().input();
                        try {
                            feedbackService.getOwnFeedbacksForSpecificEmployer(applicant.getId(), employerChoice).forEach(System.out::println);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                            return "";
                        }
                        var feedbackChoice = new BrowseOwnFeedbacksForEmployerDialog().input();
                        try {
                            userService.deleteOwnFeedback(applicant.getId(), feedbackChoice, employerChoice);
                        } catch (NonExistingEntityException | InvalidOperationException e) {
                            e.printStackTrace();
                        }
                        return String.format("Feedback deleted successfully.");
                    }));
        }

        if (userLoggedIn != null && userLoggedIn.getRole().equals(Role.ADMIN)) {
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("Edit Feedback for specific employer", () -> {
                        var feedbackChoice = new IdOfFeedbackDialog().input();
                        var feedback = new NewFeedbackDialog().input();
                        try {
                            feedbackService.updateFeedback(feedbackChoice, feedback);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        return String.format("");
                    }));
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("Delete Feedback for specific employer", () -> {
                        var employerChoice = new BrowseEmployerFeedbacksDialog().input();
                        var admin = userService.getAdminById(userLoggedIn.getId());
                        var feedbackChoice = new IdOfFeedbackDialog().input();
                        try {
                            userService.deleteFeedback(admin.getId(), feedbackChoice, employerChoice);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        return String.format("");
                    }));
        }
        menu.show();
    }
}
