package module;


import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Role {
    private long id;
    private String role;
    private Set<Person> users;

    public void addUser(Person user){
        if(users == null)
            users = new HashSet<>();
        users.add(user);
    }
}
