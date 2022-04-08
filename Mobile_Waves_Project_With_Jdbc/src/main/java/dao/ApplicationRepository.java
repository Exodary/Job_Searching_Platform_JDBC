package dao;

import exception.EntityPersistenceException;
import exception.NonExistingEntityException;
import model.Application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public interface ApplicationRepository {
    Collection<Application> findAll();
    Application findById(Long id) throws NonExistingEntityException;
    Application create(Application entity, Long cvId, Long jobId, Long userId);
    Application deleteById(Long id) throws NonExistingEntityException;

}
