package model.mock;

import model.Feedback;

public class MockFeedbacks {
    public static final Feedback[] MOCK_FEEDBACKS;
    static {
        MOCK_FEEDBACKS = new Feedback[]{
                new Feedback("Very good employer. It was pleasure working for him!"),
                new Feedback("This employer was very rude. Everything was completed under stress!"),
                new Feedback("The job from this employer was great and salaries were on point. Definitely recommending him!"),
                new Feedback("I've been working for this employer for quite some time. This was best work experience i've ever had.")
        };
    }
}
