package view.job;

import model.Job;
import view.EntityDialog;

import java.util.Scanner;

public class NewJobDialog implements EntityDialog<Job> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public Job input() {
        var job = new Job();
        while (job.getTitle() == null) {
            System.out.println("Title:");
            var ans = sc.nextLine();
            if (ans.length() < 2 || ans.length() > 70) {
                System.out.println("Error: Job Title should be between 2 and 70 characters.");
            } else {
                job.setTitle(ans);
            }
        }
        while (job.getCompanyDescription() == null) {
            System.out.println("Company Description:");
            var ans = sc.nextLine();
            if (ans.length() < 5 || ans.length() > 500) {
                System.out.println("Error: Company Description should be between 5 and 500 characters.");
            } else {
                job.setCompanyDescription(ans);
            }
        }
        while (job.getJobDescription() == null) {
            System.out.println("Job Description: ");
            var ans = sc.nextLine();
            if (ans.length() < 5 || ans.length() > 1000) {
                System.out.println("Error: Job Description should be between 5 and 1000 characters.");
            } else {
                job.setJobDescription(ans);
            }
        }
        while (job.getRequirements() == null) {
            System.out.println("Requirements:");
            var ans = sc.nextLine();
            if (ans.length() < 2 || ans.length() > 500) {
                System.out.println("Error: Job Requirements should be between 2 and 500 characters.");
            } else {
                job.setRequirements(ans);
            }
        }
        while (job.getSalary() == null) {
            System.out.println("Salary:");
            String ans;
            double salary = 0;
            ans = sc.nextLine();
            try {
                salary = Double.parseDouble(ans);
            } catch (NumberFormatException ex) {
                System.out.println("Error: Invalid year format - numbers only.");
            }

            if (salary <= 0) {
                System.out.println("Error: Job Salary can't be negative");
            } else {
                job.setSalary(salary);
            }
        }
        while (job.getRequirements() == null) {
            System.out.println("Requirements:");
            var ans = sc.nextLine();
            if (ans.length() < 2 || ans.length() > 500) {
                System.out.println("Error: Job Requirements should be between 2 and 500 characters.");
            } else {
                job.setRequirements(ans);
            }
        }
        return job;
    }
}
