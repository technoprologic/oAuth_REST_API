package be.g00glen00b.controller;

import be.g00glen00b.model.User;
import be.g00glen00b.service.AlbumService;
import be.g00glen00b.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Created by emagdnim on 2016-03-22.
 */
public abstract class AbstractApiController {

    @Autowired
    protected UserService userService;
    @Autowired
    protected AlbumService albumService;

    protected User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByLogin(auth.getName());
    }
}
