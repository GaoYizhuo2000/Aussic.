package au.edu.anu.Aussic.models.entity;

import java.util.List;

import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;

/**
 * @author: u7516507, Evan Cheung
 */

public class Session {
    public List<String> users;
    public List<MessageContent> history;
    public String name;

    public Session(String name) {
        this.name = name;
    }
    public List<String> getUsers() {
        return users;
    }

    public List<MessageContent> getHistory() {
        return history;
    }

    public String getName() {
        return name;
    }

    public void setSession(Session session){
        this.users = session.getUsers();
        this.history = session.getHistory();
        this.name = session.getName();
    }

    /**
     * Gets the peer user associated with this session.
     *
     * @return The peer user, or null if not found.
     * @author: Evan Cheung
     */
    public User getPeerUsr(){
        for(User user : RuntimeObserver.currentSessionsAvailableUsers){
            if (user.username.equals(this.users.get(0))) return user;
            else if(user.username.equals(this.users.get(1))) return user;
        }
        return null;
    }

    /**
     * Compares this Session object to another object to determine equality based on their names.
     *
     * @param object The object to compare to this Session.
     * @return true if the objects are equal (have the same name), false otherwise.
     *
     * @author: Evan Cheung
     */
    @Override
    public boolean equals(Object object){
        if(object instanceof Session) {
            if (((Session) object).getName().equals(this.name)) return true;
        }
        return false;
    }
}
