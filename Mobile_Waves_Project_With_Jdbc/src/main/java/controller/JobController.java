package controller;

import exception.InvalidOperationException;
import exception.NonExistingEntityException;
import lombok.extern.slf4j.Slf4j;
import model.*;
import service.ApplicationService;
import service.CategoryService;
import service.JobService;
import service.UserService;
import view.Menu;
import view.application.NewApplicationDialog;
import view.category.AddCategoryToJobDialog;
import view.category.BrowseJobsByCategoryDialog;
import view.category.IdOfCategoryDialog;
import view.category.UpdateCategoryToJobDialog;
import view.job.*;

import java.util.List;

import static controller.MainController.userLoggedIn;

@Slf4j
public class JobController {
    private JobService jobService;
    private ApplicationService applicationService;
    private UserService userService;
    private CategoryService categoryService;

    public JobController(JobService jobService, ApplicationService applicationService, UserService userService, CategoryService categoryService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public void init() {
        var menu = new Menu("Job Menu", List.of(
                new Menu.Option("Browse All Jobs", () -> {
                    var jobs = jobService.getAllApprovedJobs();
                    if(jobs.isEmpty()){
                        return "There aren't any jobs";
                    }
                    for(Job job : jobs){
                        System.out.printf("| %-3d | %10.40s", job.getId(), job.getTitle());
                        System.out.println();
                    }
                    return "Total jobs count " + jobs.size();
                }),
                new Menu.Option("Browse Jobs from specific category", () -> {
                    var categories = categoryService.getAllCategories();
                    if(categories.isEmpty()){
                        return "There aren't any categories";
                    }
                    for(var category : categories){
                        System.out.printf("| %-3d | %1.50s ", category.getId(), category.getName());
                        System.out.println();
                    }
                    var categoryChoice = new BrowseJobsByCategoryDialog().input();
                    try {
                        var jobs = jobService.findJobByCategory(categoryChoice);
                        if(jobs.isEmpty()){
                            return "There aren't any jobs for this category";
                        }
                        jobs.forEach(System.out::println);
                    } catch (NonExistingEntityException e) {
                        e.printStackTrace();
                    }
                    return "";
                }),
                new Menu.Option("Browse jobs by keyword", () -> {
                    var jobKeyword = new KeywordSearchJobsDialog().input();
                    var jobs = jobService.findJobBySearchingForString(jobKeyword);
                    if(jobs.isEmpty()){
                        return "There aren't any jobs with this keyword";
                    }
                    jobs.forEach(System.out::println);
                    return " ";
                }),
                new Menu.Option("Browse Specific Job", () -> {
                    if(jobService.getAllApprovedJobs().isEmpty()){
                        return "There aren't any jobs";
                    }
                    var jobChoice = new BrowseJobDetailsDialog().input();
                    Job job = null;
                    try {
                        job = jobService.getApprovedJobById(jobChoice);
                        System.out.println(job);
                    } catch (NonExistingEntityException e) {
                        e.printStackTrace();
                    }

                    return " ";
                })
        ));

        if (userLoggedIn != null && userLoggedIn.getRole().equals(Role.APPLICANT)) {
            menu.getOptions().add(menu.getOptions().size() - 1,
                    new Menu.Option("Browse And Apply to Specific Job", () -> {
                        var jobChoice = new BrowseJobDetailsDialog().input();
                        try {
                            var applicant = userService.getApplicantById(userLoggedIn.getId());
                            var job = jobService.getApprovedJobById(jobChoice);
                            System.out.println(job);
                            var applicationChoice = new ApplicationJobDialog().input();
                            System.out.println(applicant.getApplications());
                            if (applicationChoice == true) {
                                var application = new NewApplicationDialog().input();

                                applicationService.createApplicationWithEverything(applicant.getId(),
                                        applicant.getCv().getId(), job.getId(), application);
                                if(applicant.getCv() == null ){
                                    return "Applicant doesn't have cv" ;
                                }
                           }
                        } catch (NonExistingEntityException e) {
                            e.printStackTrace();
                        }
                        return "You applied successfully.";
                    }));
        }

            if (userLoggedIn != null && userLoggedIn.getRole().equals(Role.ADMIN)) {
                menu.getOptions().add(menu.getOptions().size() - 1,
                        new Menu.Option("Approve Job", () -> {
                            if(jobService.getAllNotApprovedJobs().isEmpty()){
                                return "All jobs are approved";
                            }
                        jobService.getAllNotApprovedJobs().forEach(System.out::println);
                            var user = userService.getAdminById(userLoggedIn.getId());
                        var jobChoice = new BrowseJobDetailsDialog().input();
                            try {
                                jobService.approveJob(jobChoice, user.getId());
                            } catch (NonExistingEntityException e) {
                                e.printStackTrace();
                            } catch (InvalidOperationException e) {
                                e.printStackTrace();
                            }
                            return " ";
                        }));
                menu.getOptions().add(menu.getOptions().size() - 1,
                        new Menu.Option("Edit Job", () -> {
                            var jobChoice = new BrowseJobDetailsDialog().input();
                            try {
                                var job = jobService.getAnyJobById(jobChoice);
                                var newJob = new UpdateJobDialog().input();
                                var updateCategory = new UpdateCategoryToJobDialog().input();
                                if(updateCategory == true){
                                    var categories = categoryService.getAllCategories().stream().toList();
                                    categories.forEach(System.out::println);
                                    var categoryChoice = new BrowseJobsByCategoryDialog().input();
                                    var category = categoryService.updateCategoryFromList(categoryChoice);
                                    categoryService.updateJobCategory(job.getId(), category.getId());
                                }
                                jobService.updateJob(job.getId(), newJob);
                            } catch (NonExistingEntityException e) {
                                e.printStackTrace();
                            }

                            return " ";
                        }));
                menu.getOptions().add(menu.getOptions().size() - 1,
                        new Menu.Option("Delete Job", () -> {
                            var jobChoice = new BrowseJobDetailsDialog().input();
                            try {
                                jobService.deleteJobById(jobChoice);
                            } catch (NonExistingEntityException e) {
                                e.printStackTrace();
                            }
                            return " ";
                        }));
            }

            if (userLoggedIn != null && userLoggedIn.getRole().equals(Role.EMPLOYER)) {
                menu.getOptions().add(menu.getOptions().size() - 1,
                        new Menu.Option("Add Job", () -> {
                            var employer = userService.getEmployerById(userLoggedIn.getId());
                        var newJob = new NewJobDialog().input();
                        var categoryToJob = new AddCategoryToJobDialog().input();
                        if(categoryToJob == true){
                            categoryService.getAllCategories().forEach(System.out::println);
                            var categoryChoice = new IdOfCategoryDialog().input();
                            jobService.createJob(newJob, categoryChoice, employer.getId());
                            }
                        else{
                                jobService.createJobWithoutCategory(newJob, employer.getId());

                        }
                            return " ";
                        }));
                menu.getOptions().add(menu.getOptions().size() - 1,
                        new Menu.Option("Edit Own Job", () -> {
                            Employer employer = null;
                            try {
                                employer = userService.getEmployerById(userLoggedIn.getId());
                            if(employer.getJobs().isEmpty()){
                                return "You haven't created any jobs";
                            }
                            } catch (NonExistingEntityException e) {
                                e.printStackTrace();
                            }
                            employer.getJobs().forEach(System.out::println);
                            var jobChoice = new BrowseJobDetailsDialog().input();
                            try {
                                var job = jobService.getOwnJobById(employer.getId(), jobChoice);
                                var newJob = new UpdateJobDialog().input();
                                var updateCategory = new UpdateCategoryToJobDialog().input();
                                if(updateCategory == true){
                                    var categories = categoryService.getAllCategories().stream().toList();
                                    categories.forEach(System.out::println);
                                    var categoryChoice = new BrowseJobsByCategoryDialog().input();
                                    var category = categoryService.updateCategoryFromList(categoryChoice);
                                    categoryService.updateJobCategory(job.getId(), category.getId());
                                }
                                jobService.updateOwnJob(jobChoice, newJob, employer.getId());

                            } catch (NonExistingEntityException | InvalidOperationException e) {
                                e.printStackTrace();
                            }

                            return " ";
                        }));
                menu.getOptions().add(menu.getOptions().size() - 1,
                        new Menu.Option("Delete Own Job", () -> {
                            Employer employer = null;
                            try {
                                employer = userService.getEmployerById(userLoggedIn.getId());
                                if(employer.getJobs().isEmpty()){
                                    return "You haven't created any jobs";
                                }
                            } catch (NonExistingEntityException e) {
                                e.printStackTrace();
                            }
                            employer.getJobs().forEach(System.out::println);
                            var jobChoice = new BrowseJobDetailsDialog().input();
                            try {
                                jobService.deleteOwnJobById(employer.getId(), jobChoice);
                            } catch (NonExistingEntityException e) {
                                e.printStackTrace();
                            } catch (InvalidOperationException e) {
                                e.printStackTrace();
                            }
                            return " ";
                        }));
            }
            menu.show();
    }
}
