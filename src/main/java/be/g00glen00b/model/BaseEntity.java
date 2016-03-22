package be.g00glen00b.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by emagdnim on 2016-03-21.
 */
@MappedSuperclass
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
