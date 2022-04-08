package dao.impl.jdbc;

import dao.UserRepository;
import exception.EntityPersistenceException;
import exception.NonExistingEntityException;
import lombok.extern.slf4j.Slf4j;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class UserRepositoryJdbc implements UserRepository {
    public static final String SELECT_ALL_USERS =
            "select users.id, users.email, users.username, users.password, roles.name \n" +
                    "from users\n" +
                    "join roles on users.role_id=roles.id";
    public static final String INSERT_NEW_USER =
            "insert into `users` (`email`, `username`, `password`, `role_id`) values (?, ?, ?, ?);";
    public static final String FIND_ADMIN_BY_ID =
            "select users.id, users.email, users.username, users.password, roles.name, cv_id\n" +
                    "                    from users join roles on users.role_id=roles.id WHERE users.id LIKE ?;";
    public static final String FIND_APPLICANT_BY_ID =
            "SELECT users.*, roles.*, cvs.*,\n" +
                    "genders.name FROM users \n" +
                    "join roles on users.role_id=roles.id\n" +
                    "left join cvs ON cvs.user_id = users.id\n" +
                    "left join genders on cvs.gender_id=genders.id\n" +
                    "WHERE users.id LIKE ?";

    public static final String FIND_USER_BY_ID = "SELECT users.*, roles.* from users join roles on users.role_id = roles.id where users.id = ?";

    public static  final String GET_APPLICATIONS_BY_USER_ID = "SELECT applications.*, cvs.*, users.*, jobs.*,\n" +
            "genders.name FROM applications \n" +
            "join jobs on applications.job_id = jobs.id\n" +
            "join cvs on applications.cv_id=cvs.id\n" +
            "join genders on cvs.gender_id = genders.id\n" +
            "join users on cvs.user_id = users.id\n" +
            "WHERE users.id LIKE ?";
    public static final String UPDATE_USER_USERNAME_BY_ID = "update users set username = ? where id = ?;";
    public static final String UPDATE_USER_PASSWORD_BY_ID = "update users set password = ? where id = ?;";
    public static final String SELECT_USER_BY_EMAIL_AND_PASSWORD = "select users.id, users.email, users.username, users.password, roles.name \n" +
            "                    from users\n" +
            "                    join roles on users.role_id=roles.id\n" +
            "                    where email = ? and password = ?";
    public static final String GET_FEEDBACKS_BY_USER_ID = "SELECT * from feedbacks\n" +
            "where applicant_id = ?";
    public static final String GET_CATEGORIES_BY_USER_ID = "SELECT categories.*\n" +
            "from categories \n" +
            "where user_id = ?";
    public static final String GET_JOBS_BY_USER_ID = "SELECT jobs.*, status.name FROM jobs join status on jobs.status_id = status.id where user_id = ?";
    public static final String GET_FEEDBACKS_FOR_EMPLOYER_BY_USER_ID = "select feedbacks.* from feedbacks where employer_id = ?";
    public static final String GET_CV_BY_USER_ID = "select users.*, cvs.*, genders.name from cvs join users on cvs.user_id = users.id\n" +
            "join genders on cvs.gender_id = genders.id\n" +
            "where users.id = ?";
    private Connection connection;

    public UserRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Collection<User> findAll() {
        try (var stmt = connection.prepareStatement(SELECT_ALL_USERS)) {
            // 4. Set params and execute SQL query
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toUsers(rs);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_USERS, ex);
        }
    }

    @Override
    public User findById(Long id) {
        try {
            var stmt = connection.prepareStatement(FIND_USER_BY_ID);
            stmt.setLong(1, id);
            var rs = stmt.executeQuery();
            rs.next();
            User user;
            user = new User(
                    rs.getLong(1),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password"),
                    Role.valueOf(rs.getString("name")));

            return user;

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + FIND_USER_BY_ID, ex);
        }
    }

    @Override
    public Applicant findApplicantById(Long id) throws NonExistingEntityException {
        try {
            var stmt = connection.prepareStatement(FIND_USER_BY_ID);
            stmt.setLong(1, id);
            var rs = stmt.executeQuery();
            if(!rs.isBeforeFirst())
            {
                throw new NonExistingEntityException("Applicant with Id='" + id + "' doesn't exist");
            }
            rs.next();
            Applicant applicant;

            applicant = new Applicant(
                    rs.getLong(1),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password"),
                    Role.valueOf(rs.getString("name")));

            if(!applicant.getRole().equals(Role.APPLICANT)){
                throw new NonExistingEntityException("Applicant with Id='" + id + "' doesn't exist");
            }

            var cvStmt = connection.prepareStatement(GET_CV_BY_USER_ID);
            cvStmt.setLong(1, id);
            var cvRs = cvStmt.executeQuery();
            cvRs.next();
            cvRs.getInt("user_id");
            var bool = cvRs.wasNull();
            if (!bool) {
                applicant.setCv(new CV(
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
                        cvRs.getString("pictureUrl")));

            }

            var stmt1 = connection.prepareStatement(GET_FEEDBACKS_BY_USER_ID);
            stmt1.setLong(1, id);
            var rs1 = stmt1.executeQuery();
            rs1.next();
            rs1.getInt("applicant_id");
            var hasFeedbacks = rs1.wasNull();
            if (!hasFeedbacks) {
                List<Feedback> results = new ArrayList<>();
                while (rs1.next()) {
                    results.add(new Feedback(
                            rs1.getLong(1),
                            rs1.getString("description")
                    ));
                }

                applicant.setFeedbacks(results);
            }

            var stmt2 = connection.prepareStatement(GET_APPLICATIONS_BY_USER_ID);
            stmt2.setLong(1, id);
            var rs2 = stmt2.executeQuery();

            List<Application> resultsApplications = new ArrayList<>();
            Job job;
            Application application;
            while (rs2.next()) {
                resultsApplications.add(
                        application = new Application(
                                rs2.getLong(1),
                                rs2.getString("applications.firstName"),
                                rs2.getString("applications.lastName"),
                                applicant.getCv()));

                        job = new Job(
                                rs2.getLong(23),
                                rs2.getString("title"),
                                rs2.getString("companyDescription"),
                                rs2.getString("jobDescription"),
                                rs2.getDouble("salary"),
                                rs2.getString("requirements"));

                        application.setJob(job);
            }

            applicant.setApplications(resultsApplications);

            return applicant;

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + FIND_APPLICANT_BY_ID, ex);
        }
    }

    @Override
    public Administrator findAdminById(Long id) throws NonExistingEntityException {
        try {
            var stmt = connection.prepareStatement(FIND_USER_BY_ID);
            stmt.setLong(1, id);
            var rs = stmt.executeQuery();
            if(!rs.isBeforeFirst())
            {
                throw new NonExistingEntityException("Administrator with Id='" + id + "' doesn't exist");
            }
            rs.next();
            Administrator administrator;

            administrator = new Administrator(
                    rs.getLong(1),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password"),
                    Role.valueOf(rs.getString("name")));

            var stmt1 = connection.prepareStatement(GET_CATEGORIES_BY_USER_ID);
            stmt1.setLong(1, id);
            var rs1 = stmt1.executeQuery();


            List<Category> results = new ArrayList<>();
            while (rs1.next()) {
                results.add(new Category(
                        rs1.getLong(1),
                        rs1.getString("name")
                ));
            }

            administrator.setManagedCategories(results);

            return administrator;


        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + FIND_APPLICANT_BY_ID, ex);
        }
    }

    @Override
    public Employer findEmployerById(Long id) throws NonExistingEntityException {
        try {
            var stmt = connection.prepareStatement(FIND_USER_BY_ID);
            stmt.setLong(1, id);
            var rs = stmt.executeQuery();
            if(!rs.isBeforeFirst())
            {
                throw new NonExistingEntityException("Employer with Id='" + id + "' doesn't exist");
            }
            rs.next();
            Employer employer;

            employer = new Employer(
                    rs.getLong(1),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password"),
                    Role.valueOf(rs.getString("name")));

            var stmt1 = connection.prepareStatement(GET_JOBS_BY_USER_ID);
            stmt1.setLong(1, id);
            var rs1 = stmt1.executeQuery();


            List<Job> results = new ArrayList<>();
            while (rs1.next()) {
                results.add(new Job(
                        rs1.getLong(1),
                        rs1.getString("title"),
                        rs1.getString("companyDescription"),
                        rs1.getString("jobDescription"),
                        rs1.getDouble("salary"),
                        rs1.getString("requirements"),
                        Status.valueOf(rs1.getString("status.name"))));
            }

            employer.setJobs(results);

            var stmt2 = connection.prepareStatement(GET_FEEDBACKS_FOR_EMPLOYER_BY_USER_ID);
            stmt2.setLong(1, id);
            var rs2 = stmt2.executeQuery();


            List<Feedback> resultsFeedbacks = new ArrayList<>();
            while (rs2.next()) {
                resultsFeedbacks.add(new Feedback(
                        rs2.getLong(1),
                        rs2.getString("description")
                ));
            }

            employer.setOwnFeedbacks(resultsFeedbacks);

            return employer;

        } catch (SQLException  ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + FIND_USER_BY_ID, ex);
        }
    }

    @Override
    public User create(User entity) {
        try (var stmt = connection.prepareStatement(INSERT_NEW_USER, Statement.RETURN_GENERATED_KEYS)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, entity.getEmail());
            stmt.setString(2, entity.getUsername());
            stmt.setString(3, entity.getPassword());
            stmt.setInt(4, entity.getRole().getValue());
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
                throw new EntityPersistenceException("Error rolling back SQL query: " + SELECT_ALL_USERS, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_USERS, ex);
        }
    }

    @Override
    public User createEmployer(User entity) {
        try (var stmt = connection.prepareStatement(INSERT_NEW_USER, Statement.RETURN_GENERATED_KEYS)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, entity.getEmail());
            stmt.setString(2, entity.getUsername());
            stmt.setString(3, entity.getPassword());
            stmt.setInt(4, entity.getRole().ordinal() + 2);
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
                throw new EntityPersistenceException("Error rolling back SQL query: " + SELECT_ALL_USERS, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_USERS, ex);
        }
    }

    @Override
    public Applicant createApplicant(User entity) {
        try (var stmt = connection.prepareStatement(INSERT_NEW_USER, Statement.RETURN_GENERATED_KEYS)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, entity.getEmail());
            stmt.setString(2, entity.getUsername());
            stmt.setString(3, entity.getPassword());
            stmt.setInt(4, entity.getRole().ordinal());
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
                    return (Applicant) entity;
                } else {
                    throw new EntityPersistenceException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new EntityPersistenceException("Error rolling back SQL query: " + SELECT_ALL_USERS, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_USERS, ex);
        }
    }

    @Override
    public User update(Long oldUserId, User newUser) throws NonExistingEntityException {
        try {

            var stmt1 = connection.prepareStatement(UPDATE_USER_USERNAME_BY_ID);
            var stmt2 = connection.prepareStatement(UPDATE_USER_PASSWORD_BY_ID);


            var existingUser = findById(oldUserId);

            if (existingUser == null) {
                throw new NonExistingEntityException("User with ID='" + oldUserId + "' does not exist.");
            }

            String username = newUser.getUsername();
            String password = newUser.getPassword();

            if (username != null) {
                existingUser.setUsername(username);
                stmt1.setString(1, username);
                stmt1.setLong(2, oldUserId);
                stmt1.execute();
            }

            if (password != null) {
                existingUser.setPassword(password);
                stmt2.setString(1, password);
                stmt2.setLong(2, oldUserId);
                stmt2.execute();
            }

            return existingUser;

        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new EntityPersistenceException("Error rolling back SQL query: " + SELECT_ALL_USERS, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_USERS, ex);
        }

    }

    public List<User> toUsers(ResultSet rs) throws SQLException {
        List<User> results = new ArrayList<>();
        while (rs.next()) {
            results.add(new User(
                    rs.getLong(1),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password"),
                    Role.valueOf(rs.getString("name"))
            ));
        }
        return results;
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws NonExistingEntityException {
        try {

                var stmt = connection.prepareStatement(SELECT_USER_BY_EMAIL_AND_PASSWORD);

                stmt.setString(1, email);
                stmt.setString(2, password);
                var rs = stmt.executeQuery();
            if(!rs.isBeforeFirst())
            {
                throw new NonExistingEntityException("Wrong username or password");
            }
                rs.next();
                User user = new User(
                        rs.getLong(1),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("name")));

                var affectedRows = stmt.execute();

                return user;

            } catch (SQLException ex) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    throw new EntityPersistenceException("Error rolling back SQL query: " + SELECT_USER_BY_EMAIL_AND_PASSWORD, ex);
                }
                log.error("Error creating connection to DB", ex);
                throw new EntityPersistenceException("Error executing SQL query: " + SELECT_USER_BY_EMAIL_AND_PASSWORD, ex);
            }
        }
    }
