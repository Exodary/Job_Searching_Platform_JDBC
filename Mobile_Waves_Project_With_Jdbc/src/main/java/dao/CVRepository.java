package dao;

import exception.NonExistingEntityException;
import model.CV;

import java.util.Collection;

public interface CVRepository {
    CV findById(Long id) throws NonExistingEntityException;
    CV deleteById(Long id) throws NonExistingEntityException;
    CV update(Long oldCvId, CV newCv) throws NonExistingEntityException;
    CV create(CV entity, Long userId);
    Collection<CV> findAll();
}
