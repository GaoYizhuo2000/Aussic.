package au.edu.anu.Aussic.controller.loginPages;

import java.io.Serializable;

import au.edu.anu.Aussic.R;

/**
 * @author: u7603590, Jiawei Niu
 */
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
