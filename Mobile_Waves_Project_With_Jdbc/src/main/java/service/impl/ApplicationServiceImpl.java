package service.impl;

import dao.ApplicationRepository;
import dao.JobRepository;
import dao.UserRepository;
import exception.ConstraintViolationException;
import exception.InvalidEntityDataException;
import exception.InvalidOperationException;
import exception.NonExistingEntityException;
import model.Applicant;
import model.Application;
import model.Employer;
import model.Status;
import service.ApplicationService;
import utils.ApplicationValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static model.Role.APPLICANT;
import static model.Role.EMPLOYER;

public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepo;
    private final UserRepository userRepository;
    private final JobRepository jobRepo;
    private final ApplicationValidator applicationValidator;

    public ApplicationServiceImpl(ApplicationRepository applicationRepo, UserRepository userRepository, JobRepository jobRepo) {
        this.applicationRepo = applicationRepo;
        this.jobRepo = jobRepo;
        this.userRepository = userRepository;
        this.applicationValidator = new ApplicationValidator();
    }


    @Override
    public Collection<Application> getAllApplications() {
        return applicationRepo.findAll();
    }

    @Override
    public Application createApplicationWithEverything(Long applicantId, Long cvId, Long jobId, Application application) throws InvalidEntityDataException {


        try {
            applicationValidator.validate(application);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error creating application with first name '%s'",
                    application.getFirstName(),
                    ex));
        }


        return applicationRepo.create(application, cvId, jobId, applicantId);
    }

    @Override
    public List<Application> getAllApplicationForJob(Long employerId, Long jobId) throws NonExistingEntityException, InvalidOperationException {

        var employer = userRepository.findEmployerById(employerId);
        if (employer == null) {
            throw new NonExistingEntityException("Employer with Id='" + employerId + "' doesn't exist.");
        }
        if(!(employer.getRole().equals(EMPLOYER))){
            throw new NonExistingEntityException("Employer with Id='" + employerId + "' doesn't exist.");
        }

        var foundJob = jobRepo.findById(jobId);
        if (foundJob == null) {
            throw new NonExistingEntityException("Job with Id='" + jobId + "' doesn't exist.");
        }

        if (!(foundJob.getAuthor().getId().equals(employerId))) {
            throw new InvalidOperationException("You can check applications only for jobs you have created.");
        }

        var applications = foundJob.getApplications();

        return applications;
    }

    @Override
    public Application getApplicationById(Long id) throws NonExistingEntityException {
        var application = applicationRepo.findById(id);
        if(application == null){
            throw new NonExistingEntityException("Application with Id='" + id + "' doesn't exist.");
        }

        return application;
    }

    @Override
    public void deleteOwnApplication(Long applicantId, Long applicationId) throws NonExistingEntityException {
        var application = applicationRepo.findById(applicationId);
        if(application == null){
            throw new NonExistingEntityException("Application with Id='" + applicationId + "' doesn't exist.");
        }
        var applicantUser = userRepository.findById(applicantId);
        if(applicantUser == null){
            throw new NonExistingEntityException("Application with Id='" + applicationId + "' doesn't exist.");
        }

        if(!(applicantUser.getRole().equals(APPLICANT))){
            throw new NonExistingEntityException("Applicant with Id='" + applicantId + "' doesn't exist.");
        }

       var applicant = userRepository.findApplicantById(applicantId);

        var applications = applicant.getApplications();
        applications.remove(application);
        applicant.setApplications(applications);

        applicationRepo.deleteById(applicationId);
    }

    private void isUnique(Long applicationId) throws InvalidOperationException {

        var applications = applicationRepo.findAll().stream()
                .filter(application -> application.getId().equals(applicationId)).toList();

        if(!applications.isEmpty()){
            throw new InvalidOperationException("You have already applied for this job");
        }

    }

}
