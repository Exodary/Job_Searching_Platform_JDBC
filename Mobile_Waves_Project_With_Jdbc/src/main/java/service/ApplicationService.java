package service;

import exception.InvalidEntityDataException;
import exception.InvalidOperationException;
import exception.NonExistingEntityException;
import model.Application;

import java.util.Collection;
import java.util.List;

public interface ApplicationService {
    Collection<Application> getAllApplications();
    Application createApplicationWithEverything(Long applicantId, Long cvId ,Long jobId, Application application) throws InvalidEntityDataException;
    List<Application> getAllApplicationForJob(Long employerId, Long jobId) throws NonExistingEntityException, InvalidOperationException;
    Application getApplicationById(Long id) throws NonExistingEntityException;
    void deleteOwnApplication(Long applicantId, Long applicationId) throws NonExistingEntityException;
}
