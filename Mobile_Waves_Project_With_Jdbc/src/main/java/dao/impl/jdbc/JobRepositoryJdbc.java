package dao.impl.jdbc;

import dao.JobRepository;
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
public class JobRepositoryJdbc implements JobRepository {
    private Connection connection;
    public static final String FIND_JOB_BY_ID = "select jobs.*, categories.*, users.*, applications.*,status.* from jobs\n" +
            "           join categories on jobs.category_id = categories.id\n" +
            "           join status on jobs.status_id = status.id\n" +
            "           join users on jobs.user_id = users.id\n" +
            "           join applications on applications.job_id = jobs.id\n" +
            "           where jobs.id = ?";
    public static final String SELECT_ALL_JOBS = "select jobs.id, jobs.title, jobs.companyDescription, jobs.jobDescription, jobs.salary, jobs.requirements, status.name\n" +
            "            from jobs\n" +
            "            join status on jobs.status_id=status.id";
    public static final String SELECT_ALL_JOBS_BY_STATUS = "select jobs.id, jobs.title, jobs.companyDescription, jobs.jobDescription, jobs.salary, jobs.requirements, status.name\n" +
            "            from jobs\n" +
            "            join status on jobs.status_id=status.id\n" +
            "            where status.name = ?";
    public static final String SELECT_ALL_APPLICATIONS_BY_JOB_ID = "select applications.id, applications.firstName, applications.lastName, applications.job_id, cvs.*, genders.*\n" +
            "            from applications join cvs on applications.cv_id=cvs.id\n" +
            "            join genders on cvs.gender_id = genders.id\n" +
            "            where applications.job_id = ?";
    public static final String UPDATE_JOB_STATUS = "update jobs set status_id = 1 where id = ?";
    public static final String DELETE_JOB_BY_ID = "DELETE FROM jobs WHERE id=?;";
    public static final String UPDATE_JOB_ADD_EMPLOYER_AND_CATEGORY = "update jobs set category_id = ?, user_id = ? where id = ?";
    public static final String UPDATE_JOB_ADD_EMPLOYER = "update jobs set user_id = ? where id = ?";
    public static final String SELECT_ALL_JOBS_BY_CATEGORY = "select jobs.id, jobs.title, jobs.companyDescription, jobs.jobDescription, jobs.salary, jobs.requirements, status.name\n" +
            "                        from jobs\n" +
            "                        join status on jobs.status_id=status.id\n" +
            "                        where category_id = ? and status_id = 1";
    public static final String UPDATE_JOB_TITLE_BY_ID = "update jobs set title = ? where id = ?;";
    public static final String UPDATE_JOB_COMPANY_DESCRIPTION_BY_ID = "update jobs set companyDescription = ? where id = ?;";
    public static final String UPDATE_JOB_JOB_DESCRIPTION_BY_ID = "update jobs set jobDescription = ? where id = ?;";
    public static final String UPDATE_JOB_SALARY_BY_ID = "update jobs set salary = ? where id = ?;";
    public static final String UPDATE_JOB_REQUIREMENTS_BY_ID = "update jobs set requirements = ? where id = ?;";
    public static final String INSERT_NEW_Job_WITH_CATEGORY = "insert into jobs (title, companyDescription, jobDescription, salary, requirements, status_id, category_id, user_id) values (?, ?, ?, ?, ?, 2, ?, ?)";
    public static final String INSERT_NEW_JOB_WITHOUT_CATEGORY = "insert into jobs (title, companyDescription, jobDescription, salary, requirements, status_id, user_id) values (?, ?, ?, ?, ?, 2, ?)";
    public static final String CHECK_IF_JOB_EXIST = "select jobs.* from jobs where id = ?";
    public static final String CHECK_STATUS_FOR_JOB = "select jobs.*, status.name from jobs join status on jobs.status_id = status.id where jobs.id = ?";
    public static final String CHECK_IF_JOB_HAS_APPLICATIONS = "select applications.*, jobs.* from applications join jobs on applications.job_id = jobs.id where jobs.id = ?";
    public static final String SELECT_CATEGORY_BY_JOB_ID = "select jobs.*, categories.* from jobs join categories on jobs.category_id = categories.id where jobs.id = ?";
    public static final String SELECT_USER_BY_JOB_ID = "select jobs.*, users.* from jobs join users on jobs.user_id = users.id where jobs.id = ?";
    public static final String UPDATE_JOB_CHANGE_CATEGORY = "update jobs set category_id = ? where id = ?;";

    public JobRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Job> findAllJobsByStatus(Status status) {
        try (var stmt = connection.prepareStatement(SELECT_ALL_JOBS_BY_STATUS)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, status.name());
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toJobs(rs);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_JOBS, ex);
        }
    }

    @Override
    public Job update(Long oldJobId, Job newJob) throws NonExistingEntityException {
        try {

            var stmt1 = connection.prepareStatement(UPDATE_JOB_TITLE_BY_ID);
            var stmt2 = connection.prepareStatement(UPDATE_JOB_COMPANY_DESCRIPTION_BY_ID);
            var stmt3 = connection.prepareStatement(UPDATE_JOB_JOB_DESCRIPTION_BY_ID);
            var stmt4 = connection.prepareStatement(UPDATE_JOB_SALARY_BY_ID);
            var stmt5 = connection.prepareStatement(UPDATE_JOB_REQUIREMENTS_BY_ID);

            var existingJob = findById(oldJobId);

            if (existingJob == null) {
                throw new NonExistingEntityException("User with ID='" + oldJobId + "' does not exist.");
            }

            String title = newJob.getTitle();
            String companyDescription = newJob.getCompanyDescription();
            String jobDescription = newJob.getJobDescription();
            Double salary = newJob.getSalary();
            String requirements = newJob.getRequirements();

            if (title != null) {
                existingJob.setTitle(title);
                stmt1.setString(1, title);
                stmt1.setLong(2, oldJobId);
                stmt1.execute();
            }

            if (companyDescription != null) {
                existingJob.setCompanyDescription(companyDescription);
                stmt2.setString(1, companyDescription);
                stmt2.setLong(2, oldJobId);
                stmt2.execute();
            }

            if (jobDescription != null) {
                existingJob.setJobDescription(jobDescription);
                stmt3.setString(1, jobDescription);
                stmt3.setLong(2, oldJobId);
                stmt3.execute();
            }

            if (salary != null) {
                existingJob.setSalary(salary);
                stmt4.setDouble(1, salary);
                stmt4.setLong(2, oldJobId);
                stmt4.execute();
            }

            if (requirements != null) {
                existingJob.setRequirements(requirements);
                stmt5.setString(1, requirements);
                stmt5.setLong(2, oldJobId);
                stmt5.execute();
            }

            return existingJob;

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
    public Collection<Job> findAll() {
        try (var stmt = connection.prepareStatement(SELECT_ALL_JOBS)) {
            // 4. Set params and execute SQL query
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toJobs(rs);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_JOBS, ex);
        }
    }

    @Override
    public Job findById(Long id) throws NonExistingEntityException {
        try {
            var existStmt = connection.prepareStatement(CHECK_IF_JOB_EXIST);
            existStmt.setLong(1, id);
            var rsExist = existStmt.executeQuery();
            if (!rsExist.isBeforeFirst()) {
                throw new NonExistingEntityException("Job with Id='" + id + "' doesn't exist");
            }

            var stmt = connection.prepareStatement(CHECK_STATUS_FOR_JOB);
            stmt.setLong(1, id);
            var rs = stmt.executeQuery();
            rs.next();
            Job job = new Job(
                    rs.getLong(1),
                    rs.getString("title"),
                    rs.getString("companyDescription"),
                    rs.getString("jobDescription"),
                    rs.getDouble("salary"),
                    rs.getString("requirements"),
                    Status.valueOf(rs.getString("status.name")));

            rs.getInt("category_id");
            var bool = rs.wasNull();
            if (!bool) {
                var categoryStmt = connection.prepareStatement(SELECT_CATEGORY_BY_JOB_ID);
                categoryStmt.setLong(1, id);
                var categoryRs = categoryStmt.executeQuery();
                categoryRs.next();
                var category = new Category(categoryRs.getLong(10),
                        categoryRs.getString("categories.name"));
                job.setCategory(category);
            }

            rs.getInt("jobs.user_id");
            var anotherBool = rs.wasNull();
            if (!anotherBool) {
                var employerStmt = connection.prepareStatement(SELECT_USER_BY_JOB_ID);
                employerStmt.setLong(1, id);
                var employerRs = employerStmt.executeQuery();
                employerRs.next();
                var employer = new Employer(
                        employerRs.getLong(10),
                        employerRs.getString("email"),
                        employerRs.getString("username"),
                        employerRs.getString("password"));

                job.setAuthor(employer);
            }

            var stmt2 = connection.prepareStatement(CHECK_IF_JOB_HAS_APPLICATIONS);
            stmt2.setLong(1, id);
            var rs2 = stmt2.executeQuery();
            if (rs2.isBeforeFirst()) {
                rs2.next();
                rs2.getInt("applications.job_id");
                var applicationExist = rs2.wasNull();
                var stmt1 = connection.prepareStatement(SELECT_ALL_APPLICATIONS_BY_JOB_ID);
                stmt1.setLong(1, id);
                var rs1 = stmt1.executeQuery();
                if (!applicationExist) {
                    List<Application> results = new ArrayList<>();
                    while (rs1.next()) {
                        Application application;
                        results.add(
                                application = new Application(
                                        rs1.getLong(1),
                                        rs1.getString("applications.firstName"),
                                        rs1.getString("applications.lastName")));

                        var cv = new CV(
                                rs1.getLong(4),
                                rs1.getString("firstName"),
                                rs1.getString("lastName"),
                                rs1.getString("email"),
                                rs1.getDate("birthDate").toLocalDate(),
                                Gender.valueOf(rs1.getString("genders.name")),
                                rs1.getString("phoneNumber"),
                                rs1.getString("location"),
                                rs1.getString("education"),
                                rs1.getString("workExperience"),
                                rs1.getString("pictureUrl"));

                        application.setCv(cv);

                    }

                    job.setApplications(results);

                }
            }

            return job;

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + FIND_JOB_BY_ID, ex);
        }
    }

    @Override
    public Job deleteById(Long id) throws NonExistingEntityException {
        try {
            PreparedStatement stmt = connection.prepareStatement(DELETE_JOB_BY_ID);
            Job job = findById(id);

            stmt.setLong(1, id);
            stmt.execute();

            return job;
        } catch (SQLException ex) {
            throw new EntityPersistenceException("Error executing SQL query: " + DELETE_JOB_BY_ID, ex);

        }
    }

    public List<Job> toJobs(ResultSet rs) throws SQLException {
        List<Job> results = new ArrayList<>();
        while (rs.next()) {
            results.add(new Job(
                    rs.getLong(1),
                    rs.getString("title"),
                    rs.getString("companyDescription"),
                    rs.getString("jobDescription"),
                    rs.getDouble("salary"),
                    rs.getString("requirements"),
                    Status.valueOf(rs.getString("name"))));
        }
        return results;
    }

    @Override
    public Job approveJob(Long jobId) {
        try {
            var stmt = connection.prepareStatement(UPDATE_JOB_STATUS);

            var job = findById(jobId);

            if (job == null) {
                throw new NonExistingEntityException("Job with ID='" + jobId + "' does not exist.");
            }

            var existingCategory = findById(jobId);

            stmt.setLong(1, jobId);

            var affectedRows = stmt.execute();

            return job;

        } catch (SQLException | NonExistingEntityException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new EntityPersistenceException("Error rolling back SQL query: " + UPDATE_JOB_STATUS, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + UPDATE_JOB_STATUS, ex);
        }
    }

    @Override
    public List<Job> findJobsByCategory(Long categoryId) {
        try (var stmt = connection.prepareStatement(SELECT_ALL_JOBS_BY_CATEGORY)) {
            // 4. Set params and execute SQL query
            stmt.setLong(1, categoryId);
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toJobs(rs);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_JOBS, ex);
        }
    }

    @Override
    public void updateJobCategory(Long jobId, Long categoryId) throws NonExistingEntityException {

        try {
            var stmt = connection.prepareStatement(UPDATE_JOB_CHANGE_CATEGORY);
            var job = findById(jobId);

            stmt.setLong(1, categoryId);
            stmt.setLong(2, jobId);

            var affectedRows = stmt.execute();

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + UPDATE_JOB_CHANGE_CATEGORY, ex);
        }
    }

    @Override
    public Job createJobWithCategory(Job entity, Long categoryId, Long userId) {
        try (var stmt = connection.prepareStatement(INSERT_NEW_Job_WITH_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getTitle());
            stmt.setString(2, entity.getCompanyDescription());
            stmt.setString(3, entity.getJobDescription());
            stmt.setDouble(4, entity.getSalary());
            stmt.setString(5, entity.getRequirements());
            stmt.setLong(6, categoryId);
            stmt.setLong(7, userId);

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
                throw new EntityPersistenceException("Error rolling back SQL query: " + INSERT_NEW_Job_WITH_CATEGORY, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + INSERT_NEW_Job_WITH_CATEGORY, ex);

        }
    }

    @Override
    public Job createJobWithoutCategory(Job entity, Long employerId) {
        try (var stmt = connection.prepareStatement(INSERT_NEW_JOB_WITHOUT_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getTitle());
            stmt.setString(2, entity.getCompanyDescription());
            stmt.setString(3, entity.getJobDescription());
            stmt.setDouble(4, entity.getSalary());
            stmt.setString(5, entity.getRequirements());
            stmt.setLong(6, employerId);

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
                throw new EntityPersistenceException("Error rolling back SQL query: " + INSERT_NEW_JOB_WITHOUT_CATEGORY, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + INSERT_NEW_JOB_WITHOUT_CATEGORY, ex);

        }
    }
}
