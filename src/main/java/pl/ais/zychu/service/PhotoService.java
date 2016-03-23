package pl.ais.zychu.service;

import pl.ais.zychu.model.Photo;

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
