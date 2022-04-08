package model;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static model.Status.NOT_APPROVED;

@Slf4j
public class Job {
    private Long id;
    private String title;
    private String companyDescription;
    private String jobDescription;
    private Double salary;
    private String requirements;
    private Status status = NOT_APPROVED;
    private Category category;
    private Employer author;
    private List<Application> applications = new ArrayList<>();


    public Job(String title, String companyDescription, String jobDescription, Double salary, String requirements,
                List<Application> applications) {
        this.title = title;
        this.companyDescription = companyDescription;
        this.jobDescription = jobDescription;
        this.salary = salary;
        this.requirements = requirements;
        this.status = NOT_APPROVED;
        this.applications = applications;
    }

    public Job(Long id, String title, String companyDescription, String jobDescription, Double salary, String requirements, Status status, Category category, Employer author, List<Application> applications) {
        this.id = id;
        this.title = title;
        this.companyDescription = companyDescription;
        this.jobDescription = jobDescription;
        this.salary = salary;
        this.requirements = requirements;
        this.status = status;
        this.category = category;
        this.author = author;
        this.applications = applications;
    }

    public Job(Long id, String title, String companyDescription, String jobDescription, Double salary, String requirements) {
        this.id = id;
        this.title = title;
        this.companyDescription = companyDescription;
        this.jobDescription = jobDescription;
        this.salary = salary;
        this.requirements = requirements;
    }

    public Job(Long id, String title, String companyDescription, String jobDescription, Double salary, String requirements, Status status) {
        this.id = id;
        this.title = title;
        this.companyDescription = companyDescription;
        this.jobDescription = jobDescription;
        this.salary = salary;
        this.requirements = requirements;
        this.status = status;
    }

    public Job() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Employer getAuthor() {
        return author;
    }

    public void setAuthor(Employer author) {
        this.author = author;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        return id != null ? id.equals(job.id) : job.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", companyDescription='" + companyDescription + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", salary=" + salary +
                ", requirements='" + requirements + '\'' +
                ", status=" + status +
                '}';
    }
}
