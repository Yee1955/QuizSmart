package Class;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private Long Id;
    private String Username;
    private String Email;
    private String Password;
    private String PhoneNumber;
    private List<Interest> Interests;
    private List<Task> Tasks;
    private int TotalQuestions;
    private int CorrectlyAnswered;
    private int IncorrectAnswers;

    // Getters
    public Long getId() {
        return Id;
    }

    public String getUsername() {
        return Username;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public List<Interest> getInterests() {
        return Interests;
    }

    public List<Task> getTasks() {
        if (this.Tasks == null) {
            this.Tasks = new ArrayList<>();
        }
        return Tasks;
    }

    public int getTotalQuestions() {
        return TotalQuestions;
    }

    public int getCorrectlyAnswered() {
        return CorrectlyAnswered;
    }

    public int getIncorrectAnswers() {
        return IncorrectAnswers;
    }

    // Setters
    public void setId(Long id) {
        Id = id;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setInterests(List<Interest> interestList) {
        this.Interests = interestList;
    }

    public void setTasks(List<Task> tasks) {
        this.Tasks = tasks;
    }

    public void setTotalQuestions(int totalQuestions) {
        TotalQuestions = totalQuestions;
    }

    public void setCorrectlyAnswered(int correctlyAnswered) {
        CorrectlyAnswered = correctlyAnswered;
    }

    public void setIncorrectAnswers(int incorrectAnswers) {
        IncorrectAnswers = incorrectAnswers;
    }

    // Constructors
    public User() {
        this.Interests = new ArrayList<>();
        this.Tasks = new ArrayList<>();
        this.TotalQuestions = 0;
        this.CorrectlyAnswered = 0;
        this.IncorrectAnswers = 0;
    }

    public User(String username, String email, String password, String phoneNumber, List<Interest> interests, List<Task> tasks) {
        Username = username;
        Email = email;
        Password = password;
        PhoneNumber = phoneNumber;
        this.Interests = (interests != null) ? interests : new ArrayList<>();
        this.Tasks = (tasks != null) ? tasks : new ArrayList<>();
        this.TotalQuestions = 0;
        this.CorrectlyAnswered = 0;
        this.IncorrectAnswers = 0;
    }
}
