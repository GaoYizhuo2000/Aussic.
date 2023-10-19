package au.edu.anu.Aussic.controller.loginPages;

import java.io.Serializable;

import au.edu.anu.Aussic.R;

/**
 * @author: u7603590, Jiawei Niu
 */
public class LoginDetails implements Serializable {

    private int id;
    private String username;

    /**
     * Constructs a new LoginDetails object with the specified id and username.
     *
     * @param id       Unique identifier for the user.
     * @param username Username of the user.
     */
    public LoginDetails(int id, String username) {
        this.id = id;
        this.username = username;
    }

    /**
     * Returns the username of the user.
     *
     * @return A string representing the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets or updates the username of the user.
     *
     * @param username New username to be set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

}
