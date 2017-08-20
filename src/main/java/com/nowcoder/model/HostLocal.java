package com.nowcoder.model;

import org.springframework.stereotype.Component;

@Component
public class HostLocal {
    private static ThreadLocal<User> users = new ThreadLocal<>();
    public void setUsers(User user ){
        users.set(user);
    }
    public User getUsers(){
        return users.get();
    }
    public void clear(){
        users.remove();
    }
}
