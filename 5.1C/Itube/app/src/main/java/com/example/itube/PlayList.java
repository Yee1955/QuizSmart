package com.example.itube;

import java.io.Serializable;

public class PlayList implements Serializable {
    private String URL;
    public PlayList(String URL) {
        this.URL = URL;
    }
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    public String toString() {
        return URL;
    }
}
