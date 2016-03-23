package pl.ais.zychu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.ais.zychu.model.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

}
