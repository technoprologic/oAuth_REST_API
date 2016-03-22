package be.g00glen00b.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.g00glen00b.model.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

}
