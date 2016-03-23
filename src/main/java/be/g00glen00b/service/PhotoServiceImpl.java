package be.g00glen00b.service;

import be.g00glen00b.model.Photo;
import be.g00glen00b.repository.PhotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.NestedServletException;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by emagdnim on 2016-03-23.
 */
@Service
public class PhotoServiceImpl implements PhotoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoServiceImpl.class);
    private final PhotoRepository repository;

    @Autowired
    public PhotoServiceImpl(final PhotoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Photo save(Photo photo) {
        return repository.saveAndFlush(photo);
    }

    @Override
    public void delete(Photo photo) {

        try{
            photo.getAlbum().getPhotos().remove(photo);
            repository.delete(photo);
        } catch (Throwable ex) {
            Throwable cause = ex.getCause();
            try {
                throw new NestedServletException("Request processing failed", cause);
            } catch (NestedServletException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Photo getOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public Set<Photo> findAll() {
        Set<Photo> photos = repository.findAll().stream().collect(Collectors.toSet());
        return photos;
    }
}