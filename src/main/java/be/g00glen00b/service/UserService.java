package be.g00glen00b.service;

import be.g00glen00b.model.User;

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
