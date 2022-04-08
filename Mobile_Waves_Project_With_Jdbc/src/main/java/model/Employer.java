package model;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Employer extends User {
    private List<Job> jobs = new ArrayList<>();
    private List<Feedback> ownFeedbacks = new ArrayList<>();

    public Employer(String email, String username ,String password, List<Job> jobs, List<Feedback> ownFeedbacks) {
        super(email, username, password, Role.EMPLOYER);
        this.jobs = jobs;
        this.ownFeedbacks = ownFeedbacks;
    }

    public Employer(Long id, String email, String username, String password) {
        super(id, email, username, password, Role.EMPLOYER);
    }

    public Employer(Long id, String email, String username, String password, Role role) {
        super(id, email, username, password, role);
    }

    public Employer(){}

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public List<Feedback> getOwnFeedbacks() {
        return ownFeedbacks;
    }

    public void setOwnFeedbacks(List<Feedback> ownFeedbacks) {
        this.ownFeedbacks = ownFeedbacks;
    }

    @Override
    public String toString() {
        return "Employer{" +
                "id=" + super.getId() +
                "} " + super.toString();
    }
}
