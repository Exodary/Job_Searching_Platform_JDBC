package service.impl;

import dao.CategoryRepository;
import dao.JobRepository;
import dao.UserRepository;
import exception.ConstraintViolationException;
import exception.InvalidEntityDataException;
import exception.InvalidOperationException;
import exception.NonExistingEntityException;
import model.*;
import service.JobService;
import utils.JobValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final JobValidator jobValidator;


    public JobServiceImpl(JobRepository jobRepo, CategoryRepository categoryRepo, UserRepository userRepository) {
        this.jobRepository = jobRepo;
        this.categoryRepository = categoryRepo;
        this.userRepository = userRepository;
        this.jobValidator = new JobValidator();
    }


    @Override
    public Collection<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job updateOwnJob(Long oldJobId, Job newJob, Long EmployerId) throws NonExistingEntityException,
            InvalidOperationException, InvalidEntityDataException {

        getOwnJobById(EmployerId, oldJobId);

        try {
            jobValidator.validateUpdate(newJob);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error updating job '%s'", newJob.getTitle(),
                    ex));
        }

        return jobRepository.update(oldJobId, newJob);
    }

    @Override
    public Job updateJob(Long oldJobId, Job newJob) throws NonExistingEntityException, InvalidEntityDataException {

        var job = jobRepository.findById(oldJobId);

        if(job == null){
            throw new NonExistingEntityException("Job with Id='" + oldJobId + "' doesn't exist.");
        }

        try {
            jobValidator.validateUpdate(newJob);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error updating job '%s'", newJob.getTitle(),
                    ex));
        }

        return jobRepository.update(oldJobId, newJob);
    }

    @Override
    public void deleteOwnJobById(Long employerId, Long jobId) throws NonExistingEntityException, InvalidOperationException {

        getOwnJobById(employerId, jobId);

        var employer = userRepository.findEmployerById(employerId);

        var job = jobRepository.findById(jobId);

        var jobs = employer.getJobs();
        jobs.remove(job);
        employer.setJobs(jobs);

        jobRepository.deleteById(jobId);
    }

    @Override
    public void deleteJobById(Long jobId) throws NonExistingEntityException {
        var job = jobRepository.findById(jobId);

        if(job == null){
            throw new NonExistingEntityException("Job with Id='" + jobId + "' doesn't exist.");
        }

/*        var author = job.getAuthor();

        var jobs = author.getJobs();
        jobs.remove(job);
        author.setJobs(jobs);*/

        jobRepository.deleteById(jobId);
    }


    @Override
    public Job approveJob(Long jobId, Long AdministratorId) throws NonExistingEntityException, InvalidOperationException {
        var job = jobRepository.findById(jobId);

        if(job == null){
            throw new NonExistingEntityException("Job with Id='" + jobId + "' doesn't exist.");
        }

        if(job.getStatus().equals(Status.APPROVED)){
            throw new InvalidOperationException("Job is already approved.");
        }

        var admin = userRepository.findAdminById(AdministratorId);

        if(admin == null){
            throw new NonExistingEntityException("Admin with Id='" + AdministratorId + "' doesn't exist.");
        }

        if(!(admin.getRole().equals(Role.ADMIN))){
            throw new NonExistingEntityException("Admin with Id='" + AdministratorId + "' doesn't exist.");
        }

        job.setStatus(Status.APPROVED);

        jobRepository.approveJob(jobId);

        return job;
    }

    @Override
    public List<Job> getAllNotApprovedJobs() {
        return jobRepository.findAllJobsByStatus(Status.NOT_APPROVED);
    }

    @Override
    public List<Job> getAllApprovedJobs() {
        return jobRepository.findAllJobsByStatus(Status.APPROVED);
    }

    @Override
    public List<Job> findJobBySearchingForString(String word) {
        List<Job> allJobsList = getAllApprovedJobs();
        List<Job> foundJobs = new ArrayList<>();
        String searchWord = word.toLowerCase();
        for(Job job : allJobsList){
            if(job.getTitle().toLowerCase().contains(searchWord) ||
                    job.getJobDescription().toLowerCase().contains(searchWord) ||
                    job.getRequirements().toLowerCase().contains(searchWord)){
                foundJobs.add(job);
            }
        }

        return foundJobs;
    }

    @Override
        public List<Job> findJobByCategory(Long categoryId) throws NonExistingEntityException {
        var category = categoryRepository.findById(categoryId);

        if(category == null){
            throw new NonExistingEntityException("Category with ID='" + categoryId + "' doesn't exist.");
        }

        var foundJobs = jobRepository.findJobsByCategory(categoryId);

        return foundJobs;
    }

    @Override
    public Job getApprovedJobById(Long id) throws NonExistingEntityException {
        var job = jobRepository.findById(id);
        if(job == null){
            throw new NonExistingEntityException("Job with ID='" + id + "' doesn't exist.");
        }
        if(job.getStatus() != Status.APPROVED){
            throw new NonExistingEntityException("Job with ID='" + id + "' doesn't exist.");
        }

        return job;
    }

    @Override
    public Job getAnyJobById(Long id) throws NonExistingEntityException {
        var job = jobRepository.findById(id);
        if(job == null){
            throw new NonExistingEntityException("Job with ID='" + id + "' doesn't exist.");
        }

        return job;
    }

    @Override
    public Job getOwnJobById(Long employerId, Long jobId) throws NonExistingEntityException, InvalidOperationException {
        var job = jobRepository.findById(jobId);
        if(job == null){
            throw new NonExistingEntityException("Job with ID='" + jobId + "' doesn't exist.");
        }

        var employer = userRepository.findEmployerById(employerId);

        if(employer == null){
            throw new NonExistingEntityException("Employer with Id='" + employerId + "' doesn't exist.");
        }

        if(!(employer.getRole().equals(Role.EMPLOYER))){
            throw new NonExistingEntityException("Employer with Id='" + employerId + "' doesn't exist.");
        }

        var foundJob = new ArrayList<Job>();

        for(Job job1 : employer.getJobs()){
            if(job1.getId().equals(jobId)){
                foundJob.add(job1);
            }
        }

        if(foundJob.isEmpty()){
            throw new InvalidOperationException("You can get only your job.");
        }

        return job;
    }

    @Override
    public Job createJob(Job job, Long categoryId, Long employerId) throws InvalidEntityDataException {

        try {
            jobValidator.validateUpdate(job);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error updating job '%s'", job.getTitle(),
                    ex));
        }

        return jobRepository.createJobWithCategory(job, categoryId, employerId);
    }

    @Override
    public Job createJobWithoutCategory(Job job, Long employerId) throws InvalidEntityDataException {

        try {
            jobValidator.validateUpdate(job);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error updating job '%s'", job.getTitle(),
                    ex));
        }

        return jobRepository.createJobWithoutCategory(job, employerId);
    }

}
