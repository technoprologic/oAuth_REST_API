package be.g00glen00b.service;

import be.g00glen00b.model.Photo;
import be.g00glen00b.model.Tag;
import be.g00glen00b.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emagdnim on 2016-03-23.
 */

@Service
public class TagServiceImpl implements TagService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagServiceImpl.class);
    private final TagRepository repository;

    @Autowired
    public TagServiceImpl(final TagRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Tag> findAll() {
        List<Tag> tags = new ArrayList<>();
        for(Tag t : repository.findAll()){
            tags.add(t);
        }
        return tags;
    }

    @Override
    public Tag save(Tag tag) {
        return repository.saveAndFlush(tag);
    }

    @Override
    public Tag getOne(Long tagId) {
        return repository.findOne(tagId);
    }

    @Override
    public void delete(Tag tag) {
        repository.delete(tag);
    }

    @Override
    public void untag(Photo photo, Tag tag) {
        photo.getTags().remove(tag);
        tag.getPhotos().remove(photo);
        repository.save(tag);
    }
}
