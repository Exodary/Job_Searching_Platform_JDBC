package model;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Applicant extends User {
    private CV cv;
    private List<Application> applications = new ArrayList<>();
    private List<Feedback> writtenFeedbacks = new ArrayList<>();

    public Applicant(String email, String username, String password, List<Application> applications, List<Feedback> feedbacks) {
        super(email, username, password, Role.APPLICANT);
        this.writtenFeedbacks = feedbacks;
        this.applications = applications;
    }

    public Applicant(Long id, String email, String username, String password, Role role, CV cv, List<Feedback> writtenFeedbacks) {
        super(id, email, username, password, role);
        this.cv = cv;
        this.writtenFeedbacks = writtenFeedbacks;
    }

    public Applicant(String email, String password, Role role, CV cv) {
        super(email, password, role);
        this.cv = cv;
    }

    public Applicant(Long id, String email, String username, String password) {
        super(id, email, username, password, Role.APPLICANT);
    }

    public Applicant(Long id, String email, String username, String password, Role role) {
        super(id, email, username, password, role);
    }

    public Applicant(){}

    public CV getCv() {
        return cv;
    }

    public void setCv(CV cv) {
        this.cv = cv;
    }

    public List<Feedback> getFeedbacks() {
        return writtenFeedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.writtenFeedbacks = feedbacks;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    @Override
    public String toString() {
        return "Applicant{" +
                    "id=" + super.getId() +
                "} " + super.toString();
    }
}
