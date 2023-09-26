package au.edu.anu.Aussic.controller;

import java.io.Serializable;

import au.edu.anu.Aussic.R;

public class LoginDetails implements Serializable {


    private int id;
    private String username;

    public LoginDetails(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
