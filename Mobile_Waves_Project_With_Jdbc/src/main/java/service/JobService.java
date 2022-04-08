package service;

import exception.InvalidEntityDataException;
import exception.InvalidOperationException;
import exception.NonExistingEntityException;
import model.Job;

import java.util.Collection;
import java.util.List;

public interface JobService {
    Collection<Job> getAllJobs();
    Job updateOwnJob(Long oldJobId, Job newJob, Long EmployerId) throws NonExistingEntityException, InvalidOperationException, InvalidEntityDataException;
    Job updateJob(Long oldJobId, Job newJob) throws NonExistingEntityException, InvalidEntityDataException;
    void deleteOwnJobById(Long employerId, Long jobId) throws NonExistingEntityException, InvalidOperationException;
    void deleteJobById(Long jobId) throws NonExistingEntityException;
    Job approveJob(Long jobId, Long AdministratorId) throws NonExistingEntityException, InvalidOperationException;
    List<Job> getAllNotApprovedJobs();
    List<Job> getAllApprovedJobs();
    List<Job> findJobBySearchingForString(String word);
    List<Job> findJobByCategory(Long categoryId) throws NonExistingEntityException;
    Job getApprovedJobById(Long id) throws NonExistingEntityException;
    Job getAnyJobById(Long id) throws NonExistingEntityException;
    Job getOwnJobById(Long employerId, Long jobId) throws NonExistingEntityException, InvalidOperationException;
    Job createJob(Job job, Long categoryId, Long employerId) throws InvalidEntityDataException;
    Job createJobWithoutCategory(Job job, Long employerId) throws InvalidEntityDataException;
}
