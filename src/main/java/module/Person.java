package module;
import lombok.Data;
import lombok.NonNull;


import java.util.HashSet;
import java.util.Set;


@Data
public class Person {
    private long id;
    private String name;
    private Set<Role> roles;

    public void setRole(Set<Role> roles){
        this.roles = roles;
    }

    public void addRole(Role newRole){
        if(roles == null)
            roles = new HashSet<Role>();
        roles.add(newRole);
    }

}
