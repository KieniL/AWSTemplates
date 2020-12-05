package at.lernplattform.domain.course;

import at.lernplattform.application.tasks.CreateFeedbackCommand;

public class FeedbackFactory {


    public FeedbackFactory() {
    }

    public static Feedback create(String feedback) {
        return new Feedback(feedback);
    }
}
