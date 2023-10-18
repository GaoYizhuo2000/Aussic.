package au.edu.anu.Aussic.models.entity;

/**
 * @author: u7516507, Evan Cheung
 */

import java.util.List;

import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;

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
    public User getPeerUsr(){
        for(User user : RuntimeObserver.currentSessionsAvailableUsers){
            if (user.username.equals(this.users.get(0))) return user;
            else if(user.username.equals(this.users.get(1))) return user;
        }
        return null;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Session) {
            if (((Session) object).getName().equals(this.name)) return true;
        }
        return false;
    }
}
