package com.example.a31c;

public class Question {
    private String questionTitle;
    private String questionDetails;
    private String correctAnswer;
    private String wrongAnswer1;
    private String wrongAnswer2;

    public Question(String title, String details, String correctAnswer1, String wrongAnswer2, String wrongAnswer3) {
        this.questionTitle = title;
        this.questionDetails = details;
        this.correctAnswer = correctAnswer1;
        this.wrongAnswer1 = wrongAnswer2;
        this.wrongAnswer2 = wrongAnswer3;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public String getQuestionDetails() {
        return questionDetails;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getWrongAnswer1() {
        return wrongAnswer1;
    }

    public String getWrongAnswer2() {
        return wrongAnswer2;
    }

    public boolean isCorrect(String inputAnswer) {
        if (inputAnswer == correctAnswer) {
            return true;
        } else {
            return false;
        }
    }
}
