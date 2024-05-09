package com.example.llama2chatbot;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private String Content;
    private String Sender;
    private LocalDateTime DateTime;
    public String getContent() {
        return Content;
    }

    public String getSender() {
        return Sender;
    }

    public LocalDateTime getDateTime() {
        return DateTime;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public void setSender(String sender) {
        this.Sender = sender;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.DateTime = dateTime;
    }

    public Message(String content, String sender, LocalDateTime dateTime) {
        this.Content = content;
        this.Sender = sender;
        this.DateTime = dateTime;
    }
}
