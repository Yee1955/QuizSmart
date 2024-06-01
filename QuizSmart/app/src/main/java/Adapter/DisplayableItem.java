package Adapter;
import java.time.LocalDateTime;

import Class.*;
import Enumerable.SessionStatus;

public interface DisplayableItem {
    public String getQuestion();
    public void getEmployeeAsync(EmployeeSession.EmployeeCallback callback);
    public float getAverageScore();
    public String getJobPosition();
    public LocalDateTime getDate();
    public SessionStatus getStatus();
    public int getId();
    public String getCompanyName();
}
