package pl.ais.zychu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by emagdnim on 2016-03-23.
 */
@Entity
@Table
public class Tag extends BaseEntity {

    @NotNull
    @Size(max = 128)
    @Column(name = "tag", nullable = false, unique = true)
    private String tagValue;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Set<Photo> photos;

    public Tag() {
    }

    public Tag(String tag) {
        this.tagValue = tag;
        photos = new HashSet<>();
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagValue='" + tagValue + '\'' +
                "photos= " + photos.size() +
                System.lineSeparator() +
                '}';
    }
}
