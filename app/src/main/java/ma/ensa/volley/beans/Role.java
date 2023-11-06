package ma.ensa.volley.beans;

import java.util.List;

public class Role {
    private Long id;
    private String name;
    private List<User> users;

    public Role() {
        super();
    }

    public Role(long id, String name) {
        this.id=id;
        this.name=name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}