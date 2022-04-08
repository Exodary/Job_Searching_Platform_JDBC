package model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Feedback  {
    private Long id;
    private String description;
    private Applicant applicant;
    private Employer employer;

    public Feedback(String description) {
        this.description = description;
    }

    public Feedback(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Feedback(Long id, String description, Applicant applicant, Employer employer) {
        this.id = id;
        this.description = description;
        this.applicant = applicant;
        this.employer = employer;
    }

    public Feedback() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}

