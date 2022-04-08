package model;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class CV {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private Gender gender;
    private String phoneNumber;
    private String location;
    private String education;
    private String workExperience;
    private String pictureUrl;
    private Applicant author;

    public CV() {
    }

    public CV(String firstName, String lastName, String email, LocalDate birthDate, Gender gender,
              String phoneNumber, String location, String education, String workExperience, String pictureUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.education = education;
        this.workExperience = workExperience;
        this.pictureUrl = pictureUrl;
    }

    public CV(Long id, String firstName, String lastName, String email, LocalDate birthDate, Gender gender, String phoneNumber, String location, String education, String workExperience, String pictureUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.education = education;
        this.workExperience = workExperience;
        this.pictureUrl = pictureUrl;
    }

    public CV(Long id, String firstName, String lastName, String email, LocalDate birthDate, Gender gender, String phoneNumber, String location, String education, String workExperience, String pictureUrl, Applicant author) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.education = education;
        this.workExperience = workExperience;
        this.pictureUrl = pictureUrl;
        this.author = author;
    }

    public Applicant getAuthor() {
        return author;
    }

    public void setAuthor(Applicant author) {
        this.author = author;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CV cv = (CV) o;

        return id != null ? id.equals(cv.id) : cv.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CV{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", gender=" + gender +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", location='" + location + '\'' +
                ", education='" + education + '\'' +
                ", workExperience='" + workExperience + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                '}';
    }
}
