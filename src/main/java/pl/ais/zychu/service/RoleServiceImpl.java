package pl.ais.zychu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ais.zychu.model.Role;
import pl.ais.zychu.repository.RoleRepository;

/**
 * Created by emagdnim on 2016-04-03.
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository repository;

    @Override
    public Role findOne(long id) {
        return repository.findOne(id);
    }

    @Override
    public Role save(Role role) {
        return repository.save(role);
    }
}
