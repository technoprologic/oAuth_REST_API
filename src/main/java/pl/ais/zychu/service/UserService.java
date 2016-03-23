package pl.ais.zychu.service;

import pl.ais.zychu.model.User;

import java.util.List;
import java.util.Set;

/**
 * Created by emagdnim on 2016-03-22.
 */
public interface UserService {
    User save(User user);
    List<User> getList();
    User findByLogin(String login);
    void delete(User loggedUser);
    Set<User> findAll();

}
