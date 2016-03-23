package be.g00glen00b.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * Created by emagdnim on 2016-03-23.
 */
@Entity
@Table
public class Photo extends BaseEntity {

    @JsonIgnore
    @Transient
    private static final String DEFAULT_DESCRIPTION = "Photo description";

    @Lob
    @JsonIgnore
    @Column(length = 20971520, nullable = true)
    private byte[] data;


    @Size(max = 2048)
    @Column(nullable = true)
    private String description;


    @NotNull
    @Column
    @Temporal(TemporalType.DATE)
    private Date captureDate;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "album_photo", referencedColumnName = "id", updatable = true)
    private Album album;



    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "photo_tag", joinColumns = { @JoinColumn(name = "photo_id") }, inverseJoinColumns = { @JoinColumn(name = "tag_id") })
    private Set<Tag> tags;

    public Photo() {
    }

    public Photo(byte[] data, String description, Date captureDate) {
        this.data = data;
        this.description = null == description || description.isEmpty()? DEFAULT_DESCRIPTION : description;
        this.captureDate = captureDate;
    }

    public byte[] getData() {

        //return data;
        /*
        just for test display
         */
        return null;
    }

    public void setData(String dataString) {

        this.data = Base64.decodeBase64(dataString);
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }


    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCaptureDate() {
        return captureDate;
    }

    public void setCaptureDate(Date captureDate) {
        this.captureDate = captureDate;
    }


    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }


    public void unsetTag(Tag tag) {
        tag.getPhotos().remove(this);
        tags.remove(tag);
    }

    @Override
    public String toString() {
        return "Photo{" +
                "album=" + album.getId() +
                ", photoTags=" + tags.size() +
                ", captureDate=" + captureDate +
                ", albumId=" + album.getId() +
                ", description='" + description + '\'' +
                System.lineSeparator() +
                '}';
    }
}
