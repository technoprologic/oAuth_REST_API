package pl.ais.zychu.service;

import pl.ais.zychu.model.Photo;
import pl.ais.zychu.model.Tag;

import java.util.List;

/**
 * Created by emagdnim on 2016-03-23.
 */
public interface TagService {
    List<Tag> findAll();
    Tag save(Tag tag);
    Tag getOne(Long tagId);
    void delete(Tag tag);

    void untag(Photo photo, Tag tag);
}