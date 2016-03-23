package be.g00glen00b.repository;

import be.g00glen00b.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by emagdnim on 2016-03-23.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}