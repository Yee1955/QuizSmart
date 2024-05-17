package API;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import Class.*;

public class TaskResponse {
    @SerializedName("quiz")
    private List<Quiz> quizList;

    public List<Quiz> getQuizList() {
        return quizList;
    }
}
