package Class;

import java.io.Serializable;

public class Employer implements Serializable {
    private int id;
    private String email;
    private String companyName;
    private String password;
    public Employer(int id, String email, String companyName, String password) {
        this.id = id;
        this.email = email;
        this.companyName = companyName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
