import controller.*;
import dao.*;
import dao.impl.jdbc.*;
import jdbc.JdbcDemo;
import lombok.extern.slf4j.Slf4j;
import service.*;
import service.impl.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static utils.JdbcUtils.closeConnection;
import static utils.JdbcUtils.createDbConnection;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Properties props = new Properties();
        String dbConfigPath = JdbcDemo.class.getClassLoader()
                .getResource("jdbc.properties").getPath();
        props.load(new FileInputStream(dbConfigPath));

        Connection conn = createDbConnection(props);
        UserRepository userRepository = new UserRepositoryJdbc(conn);
        CVRepository cvRepository = new CVRepositoryJdbc(conn);
        CategoryRepository categoryRepository = new CategoryRepositoryJdbc(conn);
        FeedbackRepository feedbackRepository = new FeedbackRepositoryJdbc(conn);
        ApplicationRepository applicationRepository = new ApplicationRepositoryJdbc(conn);
        JobRepository jobRepository = new JobRepositoryJdbc(conn);
        ApplicationService applicationService = new ApplicationServiceImpl(applicationRepository, userRepository, jobRepository);
        FeedbackService feedbackService = new FeedbackServiceImpl(feedbackRepository, userRepository);
        JobService jobService= new JobServiceImpl(jobRepository, categoryRepository, userRepository);
        CVService cvService = new CVServiceImpl(cvRepository);
        UserService userService = new UserServiceImpl(userRepository, cvRepository, categoryRepository, feedbackRepository);
        CategoryService categoryService = new CategoryServiceImpl(categoryRepository, userRepository, jobRepository);


        var categoryController = new CategoryController(categoryService, userService);
        var jobController = new JobController(jobService, applicationService, userService, categoryService);
        var cvController = new CVController(cvService, userService);
        var profileController = new ProfileController(userService, cvController, applicationService);
        var feedbackController = new FeedbackController(feedbackService, userService);

        var mainController = new MainController(userService, jobService, categoryController,
                jobController, profileController, feedbackController);
        mainController.init();


        closeConnection(conn);
    }
}

