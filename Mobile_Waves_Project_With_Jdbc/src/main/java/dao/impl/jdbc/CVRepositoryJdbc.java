package dao.impl.jdbc;

import dao.CVRepository;
import exception.EntityPersistenceException;
import exception.NonExistingEntityException;
import lombok.extern.slf4j.Slf4j;
import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class CVRepositoryJdbc implements CVRepository {
    private Connection connection;
    public static final String SELECT_ALL_CVS =
            "select cvs.id, cvs.firstName, cvs.lastName, cvs.email, cvs.birthDate, \n" +
                    "genders.name, cvs.phoneNumber, cvs.location, cvs.education, cvs. workExperience, cvs.pictureUrl\n" +
                    "from cvs join genders on cvs.gender_id=genders.id";
    public static final String DOES_CV_EXIST = "select cvs.* from cvs where cvs.id = ?";
    public static final String FIND_CV_BY_ID = "select cvs.*, users.*, genders.name from cvs join genders on cvs.gender_id = genders.id left join users on cvs.user_id = users.id where cvs.id = ?\n";
    public static final String UPDATE_CV_FIRSTNAME_BY_ID = "update cvs set firstName = ? where id = ?;";
    public static final String UPDATE_CV_LASTNAME_BY_ID = "update cvs set lastName = ? where id = ?;";
    public static final String UPDATE_CV_EMAIL_BY_ID = "update cvs set email = ? where id = ?;";
    public static final String UPDATE_CV_BIRTHDATE_BY_ID = "update cvs set birthDate = ? where id = ?;";
    public static final String UPDATE_CV_GENDER_BY_ID = "update cvs set gender_id = ? where id = ?;";
    public static final String UPDATE_CV_PHONE_NUMBER_BY_ID = "update cvs set phoneNumber = ? where id = ?;";
    public static final String UPDATE_CV_LOCATION_BY_ID = "update cvs set location = ? where id = ?;";
    public static final String UPDATE_CV_EDUCATION_BY_ID = "update cvs set education = ? where id = ?;";
    public static final String UPDATE_CV_WORK_EXPERIENCE_BY_ID = "update cvs set workExperience = ? where id = ?;";
    public static final String UPDATE_CV_PICTURE_URL_BY_ID = "update cvs set pictureUrl = ? where id = ?;";
    public static final String DELETE_CV_BY_ID = "DELETE FROM cvs WHERE id=?;";
    public static final String INSERT_NEW_CV =
            "INSERT INTO `cvs` (`firstName`, `lastName`, `email`, `birthDate`, `gender_id`, `phoneNumber`, `location`, `education`, `workExperience`, `pictureUrl`, `user_id`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";


    public CVRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public CV update(Long oldCvId, CV newCv) throws NonExistingEntityException {

        try {

            var stmt1 = connection.prepareStatement(UPDATE_CV_FIRSTNAME_BY_ID);
            var stmt2 = connection.prepareStatement(UPDATE_CV_LASTNAME_BY_ID);
            var stmt3 = connection.prepareStatement(UPDATE_CV_EMAIL_BY_ID);
            var stmt4 = connection.prepareStatement(UPDATE_CV_BIRTHDATE_BY_ID);
            var stmt5 = connection.prepareStatement(UPDATE_CV_GENDER_BY_ID);
            var stmt6 = connection.prepareStatement(UPDATE_CV_PHONE_NUMBER_BY_ID);
            var stmt7 = connection.prepareStatement(UPDATE_CV_LOCATION_BY_ID);
            var stmt8 = connection.prepareStatement(UPDATE_CV_EDUCATION_BY_ID);
            var stmt9 = connection.prepareStatement(UPDATE_CV_WORK_EXPERIENCE_BY_ID);
            var stmt10 = connection.prepareStatement(UPDATE_CV_PICTURE_URL_BY_ID);


            var existingCV = findById(oldCvId);

            if(existingCV == null) {
                throw new NonExistingEntityException("User with ID='" + oldCvId + "' does not exist.");
            }

            String firstName = newCv.getFirstName();
            String lastName = newCv.getLastName();
            String email = newCv.getEmail();
            LocalDate birthDate = newCv.getBirthDate();
            Gender gender = newCv.getGender();
            String phoneNumber = newCv.getPhoneNumber();
            String location = newCv.getLocation();
            String education = newCv.getEducation();
            String workExperience = newCv.getWorkExperience();
            String pictureUrl = newCv.getPictureUrl();

            if(firstName != null){
                existingCV.setFirstName(firstName);
                stmt1.setString(1, firstName);
                stmt1.setLong(2, oldCvId);
                stmt1.execute();
            }

            if(lastName != null){
                existingCV.setLastName(lastName);
                stmt2.setString(1, lastName);
                stmt2.setLong(2, oldCvId);
                stmt2.execute();
            }

            if(email != null){
                existingCV.setEmail(email);
                stmt3.setString(1, email);
                stmt3.setLong(2, oldCvId);
                stmt3.execute();
            }
            
            if(birthDate != null){
                existingCV.setBirthDate(birthDate);
                stmt4.setDate(1, Date.valueOf(birthDate));
                stmt4.setLong(2, oldCvId);
                stmt4.execute();
            }

            if(gender != null){
                existingCV.setGender(gender);
                if(gender.equals(Gender.MALE)){
                    stmt5.setInt(1, gender.getValue());
                    stmt5.setLong(2, oldCvId);
                    stmt5.execute();
                }
                else if(gender.equals(Gender.FEMALE)){
                    stmt5.setInt(1, gender.ordinal() + 1);
                    stmt5.setLong(2, oldCvId);
                    stmt5.execute();
                }
            }

            if(phoneNumber != null){
                existingCV.setPhoneNumber(phoneNumber);
                stmt6.setString(1, phoneNumber);
                stmt6.setLong(2, oldCvId);
                stmt6.execute();
            }

            if(location != null){
                existingCV.setLocation(location);
                stmt7.setString(1, location);
                stmt7.setLong(2, oldCvId);
                stmt7.execute();
            }
            
            if(education != null){
                existingCV.setEducation(education);
                stmt8.setString(1, education);
                stmt8.setLong(2, oldCvId);
                stmt8.execute();
            }

            if(workExperience != null){
                existingCV.setWorkExperience(workExperience);
                stmt9.setString(1, workExperience);
                stmt9.setLong(2, oldCvId);
                stmt9.execute();
            }
            
            if(pictureUrl != null){
                existingCV.setPictureUrl(pictureUrl);
                stmt10.setString(1, pictureUrl);
                stmt10.setLong(2, oldCvId);
                stmt10.execute();
            }
            

            return existingCV;

        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new EntityPersistenceException("Error rolling back SQL query: " + ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " +  ex);
        }
    }

    @Override
    public Collection<CV> findAll() {
        try(var stmt = connection.prepareStatement(SELECT_ALL_CVS)) {
            // 4. Set params and execute SQL query
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toCVs(rs);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_CVS, ex);
        }
    }

    @Override
    public CV findById(Long id) throws NonExistingEntityException {
        try {

            var newStmt = connection.prepareStatement(DOES_CV_EXIST);
            newStmt.setLong(1, id);
            var newRs = newStmt.executeQuery();
            if(!newRs.isBeforeFirst())
            {
                throw new NonExistingEntityException("CV with Id='" + id + "' doesn't exist");
            }
            newRs.next();
            CV cv;
            cv = new CV(
                    newRs.getLong(1),
                    newRs.getString("firstName"),
                    newRs.getString("lastName"),
                    newRs.getString("email"),
                    newRs.getDate("birthDate").toLocalDate(),
                    Gender.valueOf(newRs.getString("name")),
                    newRs.getString("phoneNumber"),
                    newRs.getString("location"),
                    newRs.getString("education"),
                    newRs.getString("workExperience"),
                    newRs.getString("pictureUrl"));

            newRs.getInt("user_id");
            var bool = newRs.wasNull();
            if (!bool) {
                var stmt = connection.prepareStatement(FIND_CV_BY_ID);
                stmt.setLong(1, id);
                var rs = stmt.executeQuery();
                rs.next();

                cv.setAuthor(new Applicant(
                        rs.getLong(13),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password")));
            }

            return cv;

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + FIND_CV_BY_ID, ex);
        }
    }

    @Override
    public CV create(CV entity, Long userId) {
        try(var stmt = connection.prepareStatement(INSERT_NEW_CV, Statement.RETURN_GENERATED_KEYS)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, entity.getFirstName());
            stmt.setString(2, entity.getLastName());
            stmt.setString(3, entity.getEmail());
            stmt.setDate(4,   Date.valueOf(entity.getBirthDate()));
            stmt.setString(6, entity.getPhoneNumber());
            stmt.setString(7, entity.getLocation());
            stmt.setString(8, entity.getEducation());
            stmt.setString(9, entity.getWorkExperience());
            stmt.setString(10, entity.getPictureUrl());
            stmt.setLong(11, userId);

            if(entity.getGender().equals(Gender.MALE)){
                stmt.setInt(5, entity.getGender().getValue());
            }
            else if(entity.getGender().equals(Gender.FEMALE)){
                stmt.setInt(5, entity.getGender().ordinal()+1);
            }
            // 5. Execute insert statement
            connection.setAutoCommit(false);
            var affectedRows = stmt.executeUpdate();
            // more updates here ...
            connection.commit();
            connection.setAutoCommit(true);

            // 6. Check results and Get generated primary ke
            if (affectedRows == 0) {
                throw new EntityPersistenceException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                    return entity;
                }
                else {
                    throw new EntityPersistenceException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new EntityPersistenceException("Error rolling back SQL query: " + INSERT_NEW_CV, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + INSERT_NEW_CV, ex);
        }
    }

    @Override
    public CV deleteById(Long id) throws NonExistingEntityException {
        try {
            PreparedStatement stmt = connection.prepareStatement(DELETE_CV_BY_ID);
            CV cv = findById(id);

            stmt.setLong(1, id);
            stmt.execute();

            return cv;
            } catch (SQLException ex) {
                throw new EntityPersistenceException("Error executing SQL query: " + DELETE_CV_BY_ID, ex);

            }
    }

    public List<CV> toCVs(ResultSet rs) throws SQLException {
        List<CV> results = new ArrayList<>();
        while (rs.next()) {
            results.add(new CV(
                    rs.getLong(1),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("email"),
                    rs.getDate("birthDate").toLocalDate(),
                    Gender.valueOf(rs.getString("name")),
                    rs.getString("phoneNumber"),
                    rs.getString("location"),
                    rs.getString("education"),
                    rs.getString("workExperience"),
                    rs.getString("pictureUrl")));
        }
        return results;
    }
}
