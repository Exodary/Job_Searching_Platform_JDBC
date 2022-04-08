package dao;

public interface DaoFactory {
    CVRepository createCVRepository();
    CategoryRepository createCategoryRepository();
    JobRepository createJobRepository();
    FeedbackRepository createFeedbackRepository();
    ApplicationRepository createApplicationRepository();
    UserRepository createUserRepository();

}
