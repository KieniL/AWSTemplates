package at.lernplattform.domain.course;

public class Feedback {


    private String feedback;

    public Feedback(String feedback) {

        this.feedback = feedback;
    }


    public void create(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }
}
