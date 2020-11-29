package at.lernplattform.application.tasks;

public class CreateFeedbackCommand {

    private String taskid;
    private String courseid;
    private String solved;


    public CreateFeedbackCommand(String taskid, String courseid, String solved) {
        this.taskid = taskid;
        this.courseid = courseid;
        this.solved = solved;
    }


    public String getTaskid() {
        return taskid;
    }

    public String getCourseid() {
        return courseid;
    }

    public String getSolved() {
        return solved;
    }



}


