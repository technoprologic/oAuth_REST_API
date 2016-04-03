package pl.ais.zychu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by emagdnim on 2016-03-21.
 */
@Entity
@Table
public class User extends BaseEntity{

    @Column(unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @Column
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles = new HashSet<Role>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Album> albums;

    User(){
    }

    public User(User user) {
        super();
        this.setId(user.getId());
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.albums = user.getAlbums();
    }

    public User(String login, String password, Set<Role> roles){
        super();
        this.login = login;
        this.password  = password;
        this.roles = roles;
        this.albums= new HashSet<>();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    public Set<Role> getRoles() {
        return roles;
    }


    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", roles=" + roles +
                ", albumsSize=" + (null==albums? " Empty" : albums.size()) +
                System.lineSeparator() +
                '}';
    }
}
