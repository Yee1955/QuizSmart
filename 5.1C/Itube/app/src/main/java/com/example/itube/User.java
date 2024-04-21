package com.example.itube;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String Id;
    private String fullName;
    private String userName;
    private String password;
    private List<PlayList> playList;
    public User(String id, String fullName, String userName, String password) {
        this.Id = id;
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
        this.playList = new ArrayList<PlayList>();
    }
    public String getId() {
        return Id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public List<PlayList> getPlayList() {
        return playList;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setPlayList(List<PlayList> playList) {
        this.playList = playList;
    }
}
