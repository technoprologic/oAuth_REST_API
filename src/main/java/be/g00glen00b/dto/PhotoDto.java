package be.g00glen00b.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by emagdnim on 2016-03-23.
 */
public class PhotoDto {

    private String description;

    @NotNull
    private Date captureDate;
}
