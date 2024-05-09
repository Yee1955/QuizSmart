package API;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Chat {
    @SerializedName("userMessage")
    private String userMessage;
    @SerializedName("chatHistory")
    List<ChatHistory> chatHistory;

    public String getUserMessage() {
        return userMessage;
    }

    public List<ChatHistory> getChatHistory() {
        return chatHistory;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public void setChatHistory(List<ChatHistory> chatHistory) {
        this.chatHistory = chatHistory;
    }
    public Chat() {

    }
    public Chat(String userMessage, List<ChatHistory> chatHistory) {
        this.userMessage = userMessage;
        this.chatHistory = chatHistory;
    }
}
