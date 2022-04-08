package dao;

import exception.NonExistingEntityException;
import model.Job;
import model.Status;

import java.util.Collection;
import java.util.List;

public interface JobRepository {
    Job approveJob(Long jobId);
    List<Job> findAllJobsByStatus(Status status);
    Job update(Long oldJobId, Job newJob) throws NonExistingEntityException;
    List<Job> findJobsByCategory(Long categoryId);
    void updateJobCategory(Long jobId, Long categoryId) throws NonExistingEntityException;
    Job createJobWithoutCategory(Job entity, Long employerId);
    Job createJobWithCategory(Job entity, Long categoryId, Long userId);
    Collection<Job> findAll();
    Job findById(Long id) throws NonExistingEntityException;
    Job deleteById(Long id) throws NonExistingEntityException;
}
