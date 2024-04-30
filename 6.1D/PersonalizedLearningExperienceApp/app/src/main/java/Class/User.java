package Class;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String Id;
    private String Username;
    private String Email;
    private String Password;
    private String PhoneNumber;
    private List<Interest> _InterestList;

    public String getId() {
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

    public List<Interest> get_InterestList() {
        return _InterestList;
    }

    public void setId(String id) {
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

    public void set_InterestList(List<Interest> _InterestList) {
        this._InterestList = _InterestList;
    }
    public User() {

    }

    public User(String username, String email, String password, String phoneNumber, List<Interest> _InterestList) {
        Username = username;
        Email = email;
        Password = password;
        PhoneNumber = phoneNumber;
        this._InterestList = _InterestList;
    }
}
