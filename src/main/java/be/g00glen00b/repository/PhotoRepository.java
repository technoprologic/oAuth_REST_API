package be.g00glen00b.repository;

import be.g00glen00b.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by emagdnim on 2016-03-23.
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
