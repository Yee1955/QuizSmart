package Class;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import Adapter.*;

public class Task implements Serializable, DisplayableItem {

    private String Title;
    private String Description;

    private List<Quiz> QuizList;
    private boolean Completed;
    public Task(String title, String description, List<Quiz> quizList, boolean completed) {
        Title = title;
        Description = description;
        this.QuizList = (quizList != null) ? quizList : new ArrayList<>();
        Completed = completed;
    }
    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public List<Quiz> getQuizList() {
        return this.QuizList;
    }

    public boolean getCompleted() {
        return this.Completed;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setQuizList(List<Quiz> quizList) {
        QuizList = quizList;
    }

    public void setCompleted(boolean completed) {
        Completed = completed;
    }

    // Unused methods
    @Override
    public String getQuestion() {
        return null;
    }

    @Override
    public String[] getOptions() {
        return new String[0];
    }
}
