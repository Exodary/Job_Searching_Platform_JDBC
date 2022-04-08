package service;

import exception.InvalidEntityDataException;
import exception.InvalidOperationException;
import exception.NonExistingEntityException;
import model.CV;

import java.util.Collection;

public interface CVService {
    Collection<CV> getAllCVs();
    CV getCVById(Long id) throws NonExistingEntityException;
    CV updateCV(Long existingCv, CV newCV) throws NonExistingEntityException, InvalidOperationException, InvalidEntityDataException;
    void deleteCVById(Long cvId) throws NonExistingEntityException, InvalidOperationException;
    CV createCV(CV cv, Long applicantId) throws InvalidEntityDataException;
}
