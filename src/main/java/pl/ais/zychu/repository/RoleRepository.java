package pl.ais.zychu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ais.zychu.model.Role;

/**
 * Created by emagdnim on 2016-04-03.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
