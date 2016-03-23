package pl.ais.zychu.service;

import pl.ais.zychu.model.Album;

import java.util.List;

/**
 * Created by emagdnim on 2016-03-22.
 */
public interface AlbumService {
    Album save(Album album);
    void delete(Album album);
    void delete(Iterable<? extends Album> iterable);
    List<Album> findAll();
    Album findOne(Long id);
}