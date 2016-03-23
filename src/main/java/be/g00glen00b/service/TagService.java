package be.g00glen00b.service;

import be.g00glen00b.model.Photo;
import be.g00glen00b.model.Tag;

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