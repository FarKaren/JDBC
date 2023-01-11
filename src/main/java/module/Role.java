package module;


import lombok.Data;

@Data
public class Role {
    private long id;
    private String role;
    private long userId;
}
