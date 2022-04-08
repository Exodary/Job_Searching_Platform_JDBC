package controller;

import exception.InvalidOperationException;
import exception.NonExistingEntityException;
import lombok.extern.slf4j.Slf4j;
import model.Administrator;
import model.Applicant;
import model.Employer;
import model.Role;
import service.ApplicationService;
import service.UserService;
import view.Menu;
import view.application.DeleteApplicationDialog;
import view.job.BrowseJobDetailsDialog;
import view.user.GetIdOfUserDialog;
import view.user.UpdateAllInfoDialog;
import view.user.UpdateOnlyUsernameDialog;

import java.util.List;

import static controller.MainController.userLoggedIn;

@Slf4j
public class ProfileController {
    private UserService userService;
    private CVController cvController;
    private ApplicationService applicationService;

    public ProfileController(UserService userService, CVController cvController, ApplicationService applicationService) {
        this.userService = userService;
        this.cvController = cvController;
        this.applicationService = applicationService;
    }

    public void init(){
        var menu = new Menu("Profile Menu", List.of());

        if(userLoggedIn != null) {
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("Update Profile", () -> {
                        System.out.println(userLoggedIn);
                        var updatedUser = new UpdateAllInfoDialog().input();
                        try {
                            userService.updateInfo(userLoggedIn.getId(), updatedUser);
                            System.out.println(userLoggedIn);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        return "";
                    }));
        }

        if(userLoggedIn != null && userLoggedIn.getRole().equals(Role.APPLICANT)) {
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("CV", () -> {
                        cvController.init();
                        return "";
                    }));
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("View all your applications", () -> {
                        Applicant user = null;
                        try {
                            user = userService.getApplicantById(userLoggedIn.getId());
                            if(user.getApplications().isEmpty()){
                                return "You don't have any applications.";
                            }
                            user.getApplications().forEach(System.out::println);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        return "";
                    }));
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("Delete application", () -> {
                        Applicant applicant = null;
                        try {
                            applicant = userService.getApplicantById(userLoggedIn.getId());
                            if(applicant.getApplications().isEmpty()){
                                return "You don't have any applications.";
                            }
                            var applicationChoice = new DeleteApplicationDialog().input();
                            applicationService.deleteOwnApplication(applicant.getId(), applicationChoice);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        return "";
                    }));
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("View all feedbacks", () -> {
                        Applicant user = null;
                        try {
                            user = userService.getApplicantById(userLoggedIn.getId());
                            if(user.getApplications().isEmpty()){
                                return "You haven't written any feedbacks.";
                            }
                            user.getFeedbacks().forEach(System.out::println);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        return "";
                    }));
        }

        if(userLoggedIn != null && userLoggedIn.getRole().equals(Role.EMPLOYER)) {
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("View all jobs created by you", () -> {
                        Employer user = null;
                        try {
                            user = userService.getEmployerById(userLoggedIn.getId());
                            if(user.getJobs().isEmpty()){
                                return "You haven't created any jobs.";
                            }
                            user.getJobs().forEach(System.out::println);
                            return "";
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        return "";
                    }));
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("View all applications for specific job created by you", () -> {
                        Employer employer = null;
                        try {
                            employer = userService.getEmployerById(userLoggedIn.getId());
                            if(employer.getJobs().isEmpty()){
                                return "You haven't created any jobs.";
                            }
                            employer.getJobs().forEach(System.out::println);
                            var jobChoice = new BrowseJobDetailsDialog().input();
                            try {
                                var applicationsForJob = applicationService.getAllApplicationForJob
                                        (employer.getId(), jobChoice);
                                if(applicationsForJob.isEmpty()){
                                    return "There aren't any applications for this job.";
                                }
                                applicationsForJob.forEach(System.out::println);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        } catch (InvalidOperationException e) {
                                e.printStackTrace();
                            }
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }

                        return "";
                    }));
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("View all feedbacks for you", () -> {
                        Employer user = null;
                        try {
                            user = userService.getEmployerById(userLoggedIn.getId());
                            if(user.getOwnFeedbacks().isEmpty()){
                                return "There aren't any feedbacks for you.";
                            }
                            user.getOwnFeedbacks().forEach(System.out::println);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        return "";
                    }));
        }

        if(userLoggedIn != null && userLoggedIn.getRole().equals(Role.ADMIN)) {
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("Update another user's username", () -> {
                        var updatedUserId = new GetIdOfUserDialog().input();
                        try {
                            System.out.println(userService.getUserById(updatedUserId));
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        var updatedUserInfo = new UpdateOnlyUsernameDialog().input();
                        try {
                            userService.updateInfo(updatedUserId, updatedUserInfo);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        try {
                            System.out.println(userService.getUserById(updatedUserId));
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        return "";
                    }));
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("View all categories created by you", () -> {
                        Administrator admin = null;
                        try {
                            admin = userService.getAdminById(userLoggedIn.getId());
                            if(admin.getManagedCategories().isEmpty()){
                                return "There aren't any categories";
                            }
                            admin.getManagedCategories().forEach(System.out::println);
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        return "";
                    }));
        }
        menu.show();
    }
}
