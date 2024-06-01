package HttpModel;

public class ChatRequest {
    private String inputMessage;

    public ChatRequest(String inputMessage) {
        this.inputMessage = inputMessage;
    }

    public String getInputMessage() {
        return inputMessage;
    }

    public void setInputMessage(String inputMessage) {
        this.inputMessage = inputMessage;
    }
}
