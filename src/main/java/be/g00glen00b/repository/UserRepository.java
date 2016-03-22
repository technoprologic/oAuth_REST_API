package be.g00glen00b.repository;

import be.g00glen00b.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by emagdnim on 2016-03-21.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
}
