package HttpModel;

public class LoginResponse {
    private String message;
    private String userType;
    private Object user;
    public String getMessage() {
        return message;
    }

    public String getUserType() {
        return userType;
    }

    public Object getUser() {
        return user;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUser(Object user) {
        this.user = user;
    }
}

