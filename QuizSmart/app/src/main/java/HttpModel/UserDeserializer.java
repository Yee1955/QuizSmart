package HttpModel;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;

import Class.*;

public class UserDeserializer implements JsonDeserializer<LoginResponse> {
    @Override
    public LoginResponse deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement userTypeElement = json.getAsJsonObject().get("userType");
        String userType = userTypeElement != null ? userTypeElement.getAsString() : null;
        Object user = null;

        if ("Employer".equals(userType)) {
            user = context.deserialize(json.getAsJsonObject().get("user"), Employer.class);
        } else if ("Employee".equals(userType)) {
            user = context.deserialize(json.getAsJsonObject().get("user"), Employee.class);
        } else {
            throw new JsonParseException("Unknown user type");
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserType(userType);
        loginResponse.setUser(user);
        JsonElement messageElement = json.getAsJsonObject().get("message");
        if (messageElement != null) {
            loginResponse.setMessage(messageElement.getAsString());
        }

        return loginResponse;
    }
}

