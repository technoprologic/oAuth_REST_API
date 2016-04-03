package pl.ais.zychu.service;

import pl.ais.zychu.model.Role;

/**
 * Created by emagdnim on 2016-04-03.
 */
public interface RoleService {


    Role findOne(long id);

    Role save(Role role);
}
