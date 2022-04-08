package model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {
    private Long id;
    private String firstName;
    private String lastName;
    private Job job;
    private CV cv;
    private Applicant applicant;

    public Application(){}

    public Application(String firstName, String lastName, Job job, CV cv) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
        this.cv = cv;
    }


    public Application(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Application(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Application(Long id, String firstName, String lastName, CV cv) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cv = cv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public CV getCv() {
        return cv;
    }

    public void setCv(CV cv) {
        this.cv = cv;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Application that = (Application) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", cv=" + cv +
                '}';
    }
}
