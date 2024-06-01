package Class;

import java.io.Serializable;
import java.time.LocalDateTime;

import API.ApiClient;
import API.ApiService;
import Adapter.DisplayableItem;
import Enumerable.SessionStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeSession implements Serializable, DisplayableItem {
    private int id;
    private int employeeId;
    private int sessionId;
    private int progress;
    private SessionStatus status;
    private String answerString;
    private Float scoreAlignment;
    private Float scoreProblemSolving;
    private Float scoreCommunication;
    private Float scoreInnovation;
    private Float scoreTeamFit;
    private String summary;
    private Employee Employee; // Get async object
    private Session Session; // Get async object

    // No-argument constructor
    public EmployeeSession() {
    }

    // Constructor with all fields
    public EmployeeSession(int employeeId, int sessionId, int progress, SessionStatus status, String answerString,
                           Float scoreAlignment, Float scoreProblemSolving, Float scoreCommunication,
                           Float scoreInnovation, Float scoreTeamFit, String summary) {
        this.employeeId = employeeId;
        this.sessionId = sessionId;
        this.progress = progress;
        this.status = status;
        this.answerString = answerString;
        this.scoreAlignment = scoreAlignment;
        this.scoreProblemSolving = scoreProblemSolving;
        this.scoreCommunication = scoreCommunication;
        this.scoreInnovation = scoreInnovation;
        this.scoreTeamFit = scoreTeamFit;
        this.summary = summary;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    @Override
    public String getCompanyName() {
        return null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public String getAnswerString() {
        return answerString;
    }

    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }

    public Float getScoreAlignment() {
        return scoreAlignment;
    }

    public void setScoreAlignment(Float scoreAlignment) {
        this.scoreAlignment = scoreAlignment;
    }

    public Float getScoreProblemSolving() {
        return scoreProblemSolving;
    }

    public void setScoreProblemSolving(Float scoreProblemSolving) {
        this.scoreProblemSolving = scoreProblemSolving;
    }

    public Float getScoreCommunication() {
        return scoreCommunication;
    }

    public void setScoreCommunication(Float scoreCommunication) {
        this.scoreCommunication = scoreCommunication;
    }

    public Float getScoreInnovation() {
        return scoreInnovation;
    }

    public void setScoreInnovation(Float scoreInnovation) {
        this.scoreInnovation = scoreInnovation;
    }

    public Float getScoreTeamFit() {
        return scoreTeamFit;
    }

    public void setScoreTeamFit(Float scoreTeamFit) {
        this.scoreTeamFit = scoreTeamFit;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public float getAverageScore() {
        return (scoreAlignment + scoreCommunication + scoreInnovation + scoreProblemSolving + scoreTeamFit) / 5;
    }

    @Override
    public String getJobPosition() {
        return null;
    }

    @Override
    public LocalDateTime getDate() {
        return null;
    }


    public interface EmployeeCallback {
        void onEmployeeLoaded(Employee employee);
        void onError(String error);
    }

    public interface SessionCallback {
        void onSessionLoaded(Session session);
        void onError(String error);
    }

    @Override
    public void getEmployeeAsync(EmployeeCallback callback) {
        ApiService apiService = ApiClient.getApiService("DB");
        Call<Employee> call = apiService.getEmployee(employeeId);
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Employee = response.body();
                    callback.onEmployeeLoaded(Employee);
                } else {
                    callback.onError("Failed to fetch employee or empty response.");
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getSessionAsync(SessionCallback callback) {
        ApiService apiService = ApiClient.getApiService("DB");
        Call<Session> call = apiService.getSession(sessionId);
        call.enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Session = response.body();
                    callback.onSessionLoaded(Session);
                } else {
                    callback.onError("Failed to fetch employee or empty response.");
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public String getQuestion() {
        return null;
    }
}
