package HttpModel;

public class EvaluationRequest {
    private String questionString;
    private String answerString;
    private String jobDetailsString;
    public EvaluationRequest(String questionString, String answerString, String jobDetailsString) {
        this.questionString = questionString;
        this.answerString = answerString;
        this.jobDetailsString = jobDetailsString;
    }
}
