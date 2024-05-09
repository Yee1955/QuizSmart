package API;

import com.google.gson.annotations.SerializedName;

public class ChatHistory {
    @SerializedName("User")
    private String user;
    @SerializedName("Llama")
    private String llama;
    public ChatHistory(String user, String llama) {
        this.user = user == null ? null : user;
        this.llama = llama == null ? null : llama;
    }
}
