package be.g00glen00b.service;

import be.g00glen00b.model.Photo;

import java.util.Set;

/**
 * Created by emagdnim on 2016-03-23.
 */
public interface PhotoService {
    Photo save(Photo photo);
    void delete(Photo photo);
    Photo getOne(Long id);
    Set<Photo> findAll();
}
