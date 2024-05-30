package Class;

import java.io.Serializable;

public class Session implements Serializable{
    private int id;
    private int employerId;
    private String sessionCode;
    private String jobPosition;
    private String jobRequirement;
    private String jobResponsibilities;
    private String companyCulture;
    private String status;
    private String questionString;

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

    public String getStatus() {
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setQuestionString(String questionString) {
        this.questionString = questionString;
    }
}

