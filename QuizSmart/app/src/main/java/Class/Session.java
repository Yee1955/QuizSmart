package Class;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Enumerable.*;

public class Session implements Serializable {
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

    @SerializedName("questionString")
    private String questionString;

    public Session(int employerId, String jobPosition, String jobRequirement, String jobResponsibilities, String companyCulture, String questionString) {
        this.employerId = employerId;
        this.sessionCode = "";
        this.jobPosition = jobPosition;
        this.jobRequirement = jobRequirement;
        this.jobResponsibilities = jobResponsibilities;
        this.companyCulture = companyCulture;
        this.status = SessionStatus.Unspecified;
        this.questionString = questionString;
    }

    // Getters
    public int getId() {
        return id;
    }
    public int getEmployerId() {
        return employerId;
    }

    public String getSessionCode() {
        return sessionCode;
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

}

