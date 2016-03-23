package pl.ais.zychu.repository;

import pl.ais.zychu.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by emagdnim on 2016-03-23.
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
