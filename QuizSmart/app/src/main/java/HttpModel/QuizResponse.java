package HttpModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class QuizResponse implements Serializable {
    @SerializedName("question")
    private String question;
    @SerializedName("options")
    private List<String> options;

    public String getQuestion() {
        return question;
    }
    public List<String> getOptions() {
        return options;
    }
}
