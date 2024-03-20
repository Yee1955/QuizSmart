package com.example.a31c;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String Name;
    private int Score;
    private int TotalNumber;

    public User(String name, int score) {
        this.Name = name;
        this.Score = score;
    }
    public User(String name, int score, int total) {
        this.Name = name;
        this.Score = score;
        this.TotalNumber = total;
    }

    public void addScoreByOne() {
        this.Score++;
    }

    public int getScore() {
        return this.Score;

    }

    public void setScore(int score) {
        this.Score = score;

    }

    public String getName() {
        return this.Name;
    }

    public int getTotalNumber() {
        return this.TotalNumber;
    }

    public void setTotalNumber(int total) {
        this.TotalNumber = total;
    }
}
