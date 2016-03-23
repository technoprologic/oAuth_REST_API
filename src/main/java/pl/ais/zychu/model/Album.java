package pl.ais.zychu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Album extends BaseEntity {

    @JsonIgnore
    @Transient
    private static final String DEFAULT_TITLE = "Album title";

    @JsonIgnore
    @Transient
    private static final String DEFAULT_DESCRIPTION = "Album description";

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_album", referencedColumnName = "id", nullable = false, updatable = true)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Photo> photos = new HashSet<>();

    Album() {
        this.title = DEFAULT_TITLE;
        this.description = DEFAULT_DESCRIPTION;
        createdAt = new Date();
        photos = new HashSet<>();
    }


    public Album(String title, String description) {
        this.title = null==title || title.isEmpty() ? DEFAULT_TITLE : title;
        this.description = null==description || description.isEmpty() ? DEFAULT_DESCRIPTION : description;
        createdAt = new Date();
        photos = new HashSet<>();
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

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "Album{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                //", user=" + user.getId() +
                ", photos=" + (null==photos ? "empty" : photos.size()) +
                System.lineSeparator() +
                '}';
    }
}
