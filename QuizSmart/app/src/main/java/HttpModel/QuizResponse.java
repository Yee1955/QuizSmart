package HttpModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import Adapter.DisplayableItem;
import Class.*;
import Enumerable.SessionStatus;

public class QuizResponse implements Serializable, DisplayableItem {
    @SerializedName("question")
    private String question;
    @SerializedName("options")
    private List<String> options;

    public QuizResponse(String question, List<String> options) {
        this.question = question;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    @Override
    public void getEmployeeAsync(EmployeeSession.EmployeeCallback callback) {

    }

    @Override
    public float getAverageScore() {
        return 0;
    }

    @Override
    public String getJobPosition() {
        return null;
    }

    @Override
    public LocalDateTime getDate() {
        return null;
    }

    @Override
    public SessionStatus getStatus() {
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getCompanyName() {
        return null;
    }

    public List<String> getOptions() {
        return options;
    }
    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append(getQuestion());
        for (String option : getOptions()) {
            st.append(":::");
            st.append(option);
        }
        return st.toString();
    }
}
