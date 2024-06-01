package Class;

import java.time.LocalDateTime;

import Adapter.DisplayableItem;
import Enumerable.SessionStatus;

public class EmployeeHistoryDTO implements DisplayableItem {

    private int employeeSessionId;
    private String companyName;
    private String jobPosition;

    private LocalDateTime date;
    private SessionStatus status;
    public EmployeeHistoryDTO(int employeeSessionId, String companyName, String jobPosition, LocalDateTime date, SessionStatus status) {
        this.employeeSessionId = employeeSessionId;
        this.companyName = companyName;
        this.jobPosition = jobPosition;
        this.date = date;
        this.status = status;
    }
    public int getEmployeeSessionId() {
        return employeeSessionId;
    }

    public String getCompanyName() {
        return companyName;
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

    @Override
    public String getJobPosition() {
        return jobPosition;
    }

    @Override
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public SessionStatus getStatus() {
        return status;
    }

    @Override
    public int getId() {
        return 0;
    }
}
