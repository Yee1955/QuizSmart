package Class;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import API.ApiClient;
import API.ApiService;
import Adapter.DisplayableItem;
import Enumerable.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Session implements Serializable, DisplayableItem {
    @SerializedName("id")
    private int id;
    @SerializedName("employerId")
    private int employerId;

    @SerializedName("sessionCode")
    private String sessionCode;

    @SerializedName("jobPosition")
    private String jobPosition;

    @SerializedName("jobRequirement")
    private String jobRequirement;

    @SerializedName("jobResponsibilities")
    private String jobResponsibilities;

    @SerializedName("companyCulture")
    private String companyCulture;

    @SerializedName("status")
    private SessionStatus status;
    @SerializedName("date")
    private LocalDateTime date;

    @SerializedName("questionString")
    private String questionString;
    private Employer Employer;  // Async purpose

    public Session(int employerId, String jobPosition, String jobRequirement, String jobResponsibilities, String companyCulture, String questionString) {
        this.employerId = employerId;
        this.sessionCode = "";
        this.jobPosition = jobPosition;
        this.jobRequirement = jobRequirement;
        this.jobResponsibilities = jobResponsibilities;
        this.companyCulture = companyCulture;
        this.status = SessionStatus.Unspecified;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.date = LocalDateTime.now();
        }
        this.questionString = questionString;
    }

    // Getters
    public int getId() {
        return id;
    }

    @Override
    public String getCompanyName() {
        return null;
    }

    public int getEmployerId() {
        return employerId;
    }

    public String getSessionCode() {
        return sessionCode;
    }

    @Override
    public String getQuestion() {
        return null;
    }

    @Override
    public void getEmployeeAsync(EmployeeSession.EmployeeCallback callback) {

    }

    @Override
    public float getAverageScore() {
        return 0;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public String getJobRequirement() {
        return jobRequirement;
    }

    public String getJobResponsibilities() {
        return jobResponsibilities;
    }

    public String getCompanyCulture() {
        return companyCulture;
    }

    public SessionStatus getStatus() {
        return status;
    }
    public LocalDateTime getDate() {
        return date;
    }

    public String getQuestionString() {
        return questionString;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setEmployerId(int employerId) {
        this.employerId = employerId;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public void setJobRequirement(String jobRequirement) {
        this.jobRequirement = jobRequirement;
    }

    public void setJobResponsibilities(String jobResponsibilities) {
        this.jobResponsibilities = jobResponsibilities;
    }

    public void setCompanyCulture(String companyCulture) {
        this.companyCulture = companyCulture;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public void setQuestionString(String questionString) {
        this.questionString = questionString;
    }
    public interface EmployerCallback {
        void onEmployerLoaded(Employer employer);
        void onError(String error);
    }

    public void getEmployerAsync(EmployerCallback callback) {
        ApiService apiService = ApiClient.getApiService("DB");
        Call<Employer> call = apiService.getEmployer(employerId);
        call.enqueue(new Callback<Employer>() {
            @Override
            public void onResponse(Call<Employer> call, Response<Employer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Employer = response.body();
                    callback.onEmployerLoaded(Employer);
                } else {
                    callback.onError("Failed to fetch employee or empty response.");
                }
            }

            @Override
            public void onFailure(Call<Employer> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

}

