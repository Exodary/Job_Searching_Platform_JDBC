package dao.impl.jdbc;

import dao.FeedbackRepository;
import exception.EntityPersistenceException;
import exception.NonExistingEntityException;
import lombok.extern.slf4j.Slf4j;
import model.Applicant;
import model.Category;
import model.Employer;
import model.Feedback;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class FeedbackRepositoryJdbc implements FeedbackRepository {
    private Connection connection;
    public static final String SELECT_ALL_FEEDBACKS = "SELECT feedbacks.id, feedbacks.description FROM feedbacks";
    public static final String UPDATE_FEEDBACK_BY_ID = "update feedbacks set description = ? where id = ?;";
    public static final String FIND_FEEDBACK_BY_ID = "select feedbacks.*, u.*, e.*\n" +
            "from feedbacks \n" +
            "join users u on feedbacks.employer_id=u.id  \n" +
            "join users e on feedbacks.applicant_id=e.id\n" +
            "where feedbacks.id = ?\n" +
            "\n";
    public static final String INSERT_NEW_FEEDBACK = "INSERT INTO `feedbacks` (`description`, `applicant_id`, `employer_id`) VALUES (?, ?, ?);";
    public static final String UPDATE_APPLICANT_EMPLOYER_IN_FEEDBACK = "update feedbacks set applicant_id = ?, employer_id = ? where id = ?";
    public static final String DELETE_FEEDBACK_BY_ID = "DELETE FROM feedbacks WHERE id=?;";
    public static final String SELECT_FEEDBACKS_FOR_EMPLOYER = "SELECT feedbacks.id, feedbacks.description from feedbacks WHERE employer_id = ?;";
    public static final String SELECT_FEEDBACKS_OF_APPLICANT_TO_EMPLOYER = "SELECT feedbacks.id, feedbacks.description from feedbacks WHERE applicant_id = ? and employer_id = ?;";


    public FeedbackRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Feedback update(Long oldFeedbackId, Feedback newFeedback) throws NonExistingEntityException {
        try{
        var stmt = connection.prepareStatement(UPDATE_FEEDBACK_BY_ID);

        var existingFeedback = findById(oldFeedbackId);

        if (existingFeedback == null) {
            throw new NonExistingEntityException("Feedback with ID='" + oldFeedbackId + "' does not exist.");
        }

        String description = newFeedback.getDescription();

        if (description != null) {
            stmt.setString(1, description);
            stmt.setLong(2, oldFeedbackId);
            stmt.execute();
        }

        return existingFeedback;
    } catch (SQLException ex) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new EntityPersistenceException("Error rolling back SQL query: " + ex);
        }
        log.error("Error creating connection to DB", ex);
        throw new EntityPersistenceException("Error executing SQL query: " + ex);
    }

}


    @Override
    public Collection<Feedback> findAll() {
        try (var stmt = connection.prepareStatement(SELECT_ALL_FEEDBACKS)) {
            // 4. Set params and execute SQL query
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toFeedbacks(rs);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_FEEDBACKS, ex);
        }
    }

    @Override
    public Feedback findById(Long id) throws NonExistingEntityException {
        try {

            var stmt = connection.prepareStatement(FIND_FEEDBACK_BY_ID);
            stmt.setLong(1, id);
            var rs = stmt.executeQuery();
            if(!rs.isBeforeFirst())
            {
                throw new NonExistingEntityException("Feedback with Id='" + id + "' doesn't exist");
            }
            rs.next();
            Feedback feedback;
            feedback = new Feedback(
                    rs.getLong(1),
                    rs.getString("description"));

            Employer employer = new Employer(
                    rs.getLong(5),
                    rs.getString("u.email"),
                    rs.getString("u.username"),
                    rs.getString("u.password"));

            Applicant applicant = new Applicant(
                    rs.getLong(10),
                    rs.getString("e.email"),
                    rs.getString("e.username"),
                    rs.getString("e.password"));


            feedback.setEmployer(employer);
            feedback.setApplicant(applicant);

            return feedback;

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + FIND_FEEDBACK_BY_ID, ex);
        }
    }

    @Override
    public Feedback createFeedback(Feedback entity, Long applicantId, Long employerId){
        try (var stmt = connection.prepareStatement(INSERT_NEW_FEEDBACK, Statement.RETURN_GENERATED_KEYS)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, entity.getDescription());
            stmt.setLong(2, applicantId);
            stmt.setLong(3, employerId);

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
                throw new EntityPersistenceException("Error rolling back SQL query: " + INSERT_NEW_FEEDBACK, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + INSERT_NEW_FEEDBACK, ex);
        }
    }

    @Override
    public Feedback deleteById(Long id) throws NonExistingEntityException {
        try {
            PreparedStatement stmt = connection.prepareStatement(DELETE_FEEDBACK_BY_ID);

            var feedback = findById(id);

            if (findById(id) == null) {
                throw new NonExistingEntityException("Feedback with id='" + id + "' doesn't exist.");
            }

            stmt.setLong(1, id);
            stmt.execute();

            return feedback;
        } catch (SQLException ex) {
            throw new EntityPersistenceException("Error executing SQL query: " + DELETE_FEEDBACK_BY_ID, ex);

        }
    }


    public List<Feedback> toFeedbacks(ResultSet rs) throws SQLException {
        List<Feedback> results = new ArrayList<>();
        while (rs.next()) {
            results.add(new Feedback(
                    rs.getLong(1),
                    rs.getString("description")));
        }
        return results;
    }

    @Override
    public List<Feedback> getFeedbacksForEmployer(Long employerId) {
        try (var stmt = connection.prepareStatement(SELECT_FEEDBACKS_FOR_EMPLOYER)) {
            // 4. Set params and execute SQL query
            stmt.setLong(1, employerId);
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toFeedbacks(rs);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_FEEDBACKS, ex);
        }
    }

    @Override
    public List<Feedback> findAllOwnFeedbacks(Long applicantId, Long employerId){
            try (var stmt = connection.prepareStatement(SELECT_FEEDBACKS_OF_APPLICANT_TO_EMPLOYER)) {
                // 4. Set params and execute SQL query
                stmt.setLong(1, applicantId);
                stmt.setLong(2, employerId);
                var rs = stmt.executeQuery();
                // 5. Transform ResultSet to Book
                return toFeedbacks(rs);
            } catch (SQLException ex) {
                log.error("Error creating connection to DB", ex);
                throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_FEEDBACKS, ex);
            }
        }


}
