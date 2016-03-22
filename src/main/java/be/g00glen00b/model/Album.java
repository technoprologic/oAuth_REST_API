package be.g00glen00b.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Album extends BaseEntity {

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_album", referencedColumnName = "id", nullable = false, updatable = true)
    private User user;


    Album(){}

    public Album(String title, String description){
        this.title = title;
        this.description = description;
        createdAt = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
