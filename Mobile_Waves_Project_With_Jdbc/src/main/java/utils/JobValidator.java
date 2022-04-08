package utils;

import exception.ConstraintViolation;
import exception.ConstraintViolationException;
import model.Job;

import java.util.ArrayList;
import java.util.List;

public class JobValidator {
    public void validate(Job job) throws ConstraintViolationException {
        List<ConstraintViolation> violations = new ArrayList<>();


        var jobTitleLength = job.getTitle().trim().length();
        if(jobTitleLength < 2 || jobTitleLength > 70){
            violations.add(
                    new ConstraintViolation(job.getClass().getName(), "title", job.getTitle(),
                            "Job title length should be between 2 and 70 characters."));
        }

        var CompanyDescriptionLength = job.getCompanyDescription().trim().length();
        if(CompanyDescriptionLength < 5 || CompanyDescriptionLength > 500){
            violations.add(
            new ConstraintViolation(job.getClass().getName(), "companyDescription", job.getCompanyDescription(),
                    "Company Description length should be between 50 and 500 characters."));
        }


        var jobDescriptionLength = job.getJobDescription().trim().length();
         if(jobDescriptionLength < 5 || jobDescriptionLength > 1000){
             violations.add(
            new ConstraintViolation(job.getClass().getName(), "jobDescription", job.getJobDescription(),
                    "Job Description length should be between 5 and 1000 characters."));
        }

        var requirementsLength = job.getRequirements().trim().length();
        if(requirementsLength < 2 || requirementsLength > 500){
            violations.add(
            new ConstraintViolation(job.getClass().getName(), "requirements", job.getRequirements(),
                    "Requirements length should be between 2 and 500 characters."));
        }

        var salary = job.getSalary();
        if(salary < 0){
            violations.add(
            new ConstraintViolation(job.getClass().getName(), "salary", job.getSalary(),
                    "Salary cannot be negative."));
        }


        if (violations.size() > 0) {
            throw new ConstraintViolationException("Invalid user field", violations);
        }
    }

    public void validateUpdate(Job job) throws ConstraintViolationException {
        List<ConstraintViolation> violations = new ArrayList<>();

        if(job.getTitle() != null) {
            var jobTitleLength = job.getTitle().trim().length();
            if (jobTitleLength < 2 || jobTitleLength > 70) {
                violations.add(
                        new ConstraintViolation(job.getClass().getName(), "title", job.getTitle(),
                                "Job title length should be between 2 and 70 characters."));
            }
        }

        if(job.getCompanyDescription() != null) {
            var CompanyDescriptionLength = job.getCompanyDescription().trim().length();
            if (CompanyDescriptionLength < 5 || CompanyDescriptionLength > 500) {
                violations.add(
                new ConstraintViolation(job.getClass().getName(), "companyDescription", job.getCompanyDescription(),
                        "Company Description length should be between 50 and 500 characters."));
            }

        }

        if(job.getJobDescription() != null) {
            var jobDescriptionLength = job.getJobDescription().trim().length();
            if (jobDescriptionLength < 5 || jobDescriptionLength > 1000) {
                violations.add(
                new ConstraintViolation(job.getClass().getName(), "jobDescription", job.getJobDescription(),
                        "Job Description length should be between 5 and 1000 characters."));
            }
        }

        if(job.getRequirements() != null) {
            var requirementsLength = job.getRequirements().trim().length();
            if (requirementsLength < 2 || requirementsLength > 500) {
                violations.add(
                new ConstraintViolation(job.getClass().getName(), "requirements", job.getRequirements(),
                        "Requirements length should be between 2 and 500 characters."));
            }
        }

        if(job.getSalary() != null) {
            var salary = job.getSalary();
            if (salary < 0) {
                violations.add(
                new ConstraintViolation(job.getClass().getName(), "salary", job.getSalary(),
                        "Salary cannot be negative."));
            }
        }


        if (violations.size() > 0) {
            throw new ConstraintViolationException("Invalid user field", violations);
        }
    }
}
