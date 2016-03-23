package be.g00glen00b.service;

import be.g00glen00b.model.Album;
import be.g00glen00b.repository.AlbumRepository;
import be.g00glen00b.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import java.util.List;

/**
 * Created by emagdnim on 2016-03-22.
 */
@Service
public class AlbumServiceImpl implements AlbumService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;

    @Autowired
    public AlbumServiceImpl(final AlbumRepository albumRepository, final UserRepository userRepository) {
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Album save(Album album) {
        return albumRepository.saveAndFlush(album);
    }

    @Override
    @Transactional
    public void delete(Album albumToDelete) {
        try{
        albumToDelete.getUser().getAlbums().remove(albumToDelete);
        albumRepository.delete(albumToDelete);
        } catch (Throwable ex) {
            Throwable cause = ex.getCause();
            try {
                throw new NestedServletException("Request processing failed", cause); //line 583
            } catch (NestedServletException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional
    public void delete(Iterable<? extends Album> albums){
        for (Album a: albums)
            albumRepository.delete(a);
    }

    @Override
    public List<Album> findAll() {
        return albumRepository.findAll();
    }

    @Override
    public Album findOne(Long id) {
        return albumRepository.findOne(id);
    }
}