package HttpModel;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class EvaluationResponse {

    @SerializedName("scores")
    private Map<String, Float> scores;

    @SerializedName("summary")
    private String summary;

    // Constructors
    public EvaluationResponse(Map<String, Float> scores, String summary) {
        this.scores = scores;
        this.summary = summary;
    }

    // Getter and setter for scores
    public Map<String, Float> getScores() {
        return scores;
    }

    public void setScores(Map<String, Float> scores) {
        this.scores = scores;
    }

    // Getter and setter for summary
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}

