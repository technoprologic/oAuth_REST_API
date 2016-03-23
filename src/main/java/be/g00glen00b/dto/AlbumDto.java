package be.g00glen00b.dto;

/**
 * Created by emagdnim on 2016-03-23.
 */
public class AlbumDto {


    private String title;


    private String description;

    AlbumDto(){}

    public AlbumDto(String title, String description){
        this.title = title;
        this.description = description;
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
}
