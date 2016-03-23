package pl.ais.zychu.service;

import pl.ais.zychu.model.User;
import pl.ais.zychu.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by emagdnim on 2016-03-22.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(final UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public User save(@NotNull @Valid final User user) {
        LOGGER.debug("Creating {}", user);
        User existing = repository.findByLogin(user.getLogin());
        try{
            repository.saveAndFlush(existing);
        }catch (JpaSystemException e){
            LOGGER.debug("Could not save entity");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getList() {
        LOGGER.debug("Retrieving the list of all users");
        return repository.findAll();
    }

    @Override
    public User findByLogin(String name) {
        return repository.findByLogin(name);
    }

    @Override
    public void delete(User loggedUser) {
        repository.delete(loggedUser);
    }

    @Override
    public Set<User> findAll() {
        return repository.findAll().stream().collect(Collectors.toSet());
    }

}