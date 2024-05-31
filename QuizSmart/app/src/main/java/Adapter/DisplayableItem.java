package Adapter;
import Class.*;

public interface DisplayableItem {
    public String getQuestion();
    public void getEmployeeAsync(EmployeeSession.EmployeeCallback callback);


    public float getAverageScore();
}
