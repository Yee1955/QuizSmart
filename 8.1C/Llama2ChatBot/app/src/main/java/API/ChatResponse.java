package API;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatResponse {
    @SerializedName("message")
    private String response;

    public String getResponse() {
        return response;
    }
}
