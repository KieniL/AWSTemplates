package at.lernplattform.rest.courses;


import at.lernplattform.domain.course.Feedback;
import at.lernplattform.rest.api.model.FeedbackResponseModel;

public class FeedbackAdapter {

    private Feedback feedback;

    public FeedbackAdapter(Feedback feedback) {
        this.feedback = feedback;
    }

    public FeedbackResponseModel createJson() {
        return new FeedbackResponseModel().feedback(feedback.getFeedback());
    }
}
