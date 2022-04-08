package service.impl;

import dao.CVRepository;
import dao.UserRepository;
import exception.ConstraintViolationException;
import exception.InvalidEntityDataException;
import exception.InvalidOperationException;
import exception.NonExistingEntityException;
import model.Applicant;
import model.CV;
import model.Role;
import service.CVService;
import utils.CVValidator;

import java.util.Collection;

public class CVServiceImpl implements CVService {
    private final CVRepository cvRepo;
    private final CVValidator cvValidator;

    public CVServiceImpl(CVRepository cvRepo) {
        this.cvRepo = cvRepo;
        this.cvValidator = new CVValidator();

    }


    @Override
    public Collection<CV> getAllCVs() {
        return cvRepo.findAll();
    }

    @Override
    public CV getCVById(Long id) throws NonExistingEntityException {
        var cv = cvRepo.findById(id);
        if(cv == null){
            throw new NonExistingEntityException("CV with ID='" + id + "' doesn't exist.");
        }

        return cv;
    }

    @Override
    public CV updateCV(Long existingCv, CV newCV) throws NonExistingEntityException, InvalidEntityDataException {

        var cv = cvRepo.findById(existingCv);
        if(cv == null){
            throw new NonExistingEntityException("CV with Id='" + existingCv + "' doesn't exist.");
        }

        try {
            cvValidator.validateUpdate(newCV);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error updating cv with email '%s'", cv.getEmail(),
                    ex));
        }

        return cvRepo.update(existingCv, newCV);
    }

    @Override
    public void deleteCVById(Long cvId) throws NonExistingEntityException {

        var cv = cvRepo.findById(cvId);
        if(cv == null){
            throw new NonExistingEntityException("CV with Id='" + cvId + "' doesn't exist.");
        }

        cvRepo.deleteById(cvId);

    }

    @Override
    public CV createCV(CV cv, Long applicantId) throws InvalidEntityDataException {

        try {
            cvValidator.validateUpdate(cv);
        } catch (ConstraintViolationException ex) {
            throw new InvalidEntityDataException(String.format("Error updating cv with email '%s'", cv.getEmail(),
                    ex));
        }

        return cvRepo.create(cv, applicantId);
    }

}
