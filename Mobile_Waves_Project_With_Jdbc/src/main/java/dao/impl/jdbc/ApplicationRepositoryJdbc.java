package dao.impl.jdbc;

import dao.ApplicationRepository;
import exception.EntityPersistenceException;
import exception.NonExistingEntityException;
import lombok.extern.slf4j.Slf4j;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class ApplicationRepositoryJdbc implements ApplicationRepository {
    private Connection connection;
    public static final String SELECT_ALL_APPLICATIONS = "SELECT applications.id, applications.firstName, applications.lastName FROM applications;";
    public static final String FIND_APPLICATION_BY_ID = "select applications.*, cvs.*, jobs.* ,genders.* ,users.*, roles.* from applications \n" +
            "join cvs on applications.cv_id=cvs.id\n" +
            "join jobs on applications.job_id=jobs.id\n" +
            "join users on cvs.user_id=users.id \n" +
            "join roles on users.role_id=roles.id \n" +
            "join genders on cvs.gender_id=genders.id \n" +
            "where applications.id = ?\n" +
            "\n" +
            "\n";
    public static final String CHECK_IF_APPLICATION_EXIST = "select applications.* from applications where id = ?";
    public static final String INSERT_NEW_APPLICATION = "INSERT INTO `applications` (`firstName`, `lastName`, `job_id`, `cv_id`, `user_id`) VALUES (?, ? , ?, ?, ?);";
    public static final String UPDATE_CV_JOB_IN_APPLICATION = "update applications set cv_id = ?, job_id = ? where id = ?";
    public static final String DELETE_FEEDBACK_BY_ID = "DELETE FROM applications WHERE id=?;";
    public static final String SELECT_CV_BY_APPLICATION_ID = "select applications.*, cvs.*, users.*, genders.name from applications join cvs on applications.cv_id = cvs.id\n" +
            "join users on cvs.user_id = users.id\n" +
            "            join genders on cvs.gender_id = genders.id\n" +
            "            where applications.id = ?";
    public static final String SELECT_JOB_BY_APPLICATION_ID = "select applications.*, jobs.* from applications\n" +
            "join jobs on applications.job_id = jobs.id\n" +
            "where applications.id = ?";

    public ApplicationRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Collection<Application> findAll() {
        try (var stmt = connection.prepareStatement(SELECT_ALL_APPLICATIONS)) {
            // 4. Set params and execute SQL query
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toApplications(rs);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_APPLICATIONS, ex);
        }
    }

    @Override
    public Application findById(Long id) throws NonExistingEntityException {
        try {

            var stmt = connection.prepareStatement(CHECK_IF_APPLICATION_EXIST);
            stmt.setLong(1, id);
            var rs = stmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                throw new NonExistingEntityException("Application with Id='" + id + "' doesn't exist");
            }

            rs.next();
            Application application;
            application = new Application(
                    rs.getLong(1),
                    rs.getString("firstName"),
                    rs.getString("lastName"));


            rs.getInt("cv_id");
            var bool = rs.wasNull();
            if (!bool) {
                var cvStmt = connection.prepareStatement(SELECT_CV_BY_APPLICATION_ID);
                cvStmt.setLong(1, id);
                var cvRs = cvStmt.executeQuery();
                cvRs.next();
                var cv = new CV(
                        cvRs.getLong(6),
                        cvRs.getString("firstName"),
                        cvRs.getString("lastName"),
                        cvRs.getString("email"),
                        cvRs.getDate("birthDate").toLocalDate(),
                        Gender.valueOf(cvRs.getString("genders.name")),
                        cvRs.getString("phoneNumber"),
                        cvRs.getString("location"),
                        cvRs.getString("education"),
                        cvRs.getString("workExperience"),
                        cvRs.getString("pictureUrl"));

                cv.setAuthor(new Applicant(
                        cvRs.getLong(18),
                        cvRs.getString("email"),
                        cvRs.getString("username"),
                        cvRs.getString("password")));

                application.setCv(cv);

            }

                rs.getInt("job_id");
            var jobBool = rs.wasNull();
            if (!jobBool) {
                var jobStmt = connection.prepareStatement(SELECT_JOB_BY_APPLICATION_ID);
                jobStmt.setLong(1, id);
                var jobRs = jobStmt.executeQuery();
                jobRs.next();
                var job = new Job(jobRs.getLong(6),
                        jobRs.getString("title"),
                        jobRs.getString("companyDescription"),
                        jobRs.getString("jobDescription"),
                        jobRs.getDouble("salary"),
                        jobRs.getString("requirements"));

                application.setJob(job);

            }

                return application;

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + FIND_APPLICATION_BY_ID, ex);
        }
    }

    @Override
    public Application create(Application entity, Long cvId, Long jobId, Long userId) {
        try (var stmt = connection.prepareStatement(INSERT_NEW_APPLICATION, Statement.RETURN_GENERATED_KEYS)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, entity.getFirstName());
            stmt.setString(2, entity.getLastName());
            stmt.setLong(3, jobId);
            stmt.setLong(4, cvId);
            stmt.setLong(5, userId);

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
                } else {
                    throw new EntityPersistenceException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new EntityPersistenceException("Error rolling back SQL query: " + INSERT_NEW_APPLICATION, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + INSERT_NEW_APPLICATION, ex);
        }
    }

    @Override
    public Application deleteById(Long id) throws NonExistingEntityException {
        try {
            PreparedStatement stmt = connection.prepareStatement(DELETE_FEEDBACK_BY_ID);

            var application = findById(id);

            if(findById(id) == null){
                throw new NonExistingEntityException("Category with id='" + id + "' doesn't exist.");
            }

            stmt.setLong(1, id);
            stmt.execute();

            return application;
        } catch (SQLException ex) {
            throw new EntityPersistenceException("Error executing SQL query: " + DELETE_FEEDBACK_BY_ID, ex);

        }
    }

    public List<Application> toApplications(ResultSet rs) throws SQLException {
        List<Application> results = new ArrayList<>();
        while (rs.next()) {
            results.add(new Application(
                    rs.getLong(1),
                    rs.getString("firstName"),
                    rs.getString("lastName")));
        }
        return results;
    }

}
