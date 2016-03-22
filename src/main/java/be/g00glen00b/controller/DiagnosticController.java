package be.g00glen00b.controller;

import be.g00glen00b.model.Album;
import be.g00glen00b.model.User;
import be.g00glen00b.repository.AlbumRepository;
import be.g00glen00b.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by emagdnim on 2016-03-21.
 */
@RestController
@RequestMapping("/status")
public class DiagnosticController {

    private UserRepository userRepository;
    private AlbumRepository albumRepository;

    @Autowired
    public DiagnosticController(UserRepository userRepository, AlbumRepository albumRepository){
        this.userRepository = userRepository;
        this.albumRepository = albumRepository;
    }

    /**
     * ALBUMS
     *
     * @return
     */
    @RequestMapping(value = "/albums", method = RequestMethod.GET)
    public List<Album> getAlbums(){
        return albumRepository.findAll();
    }

    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET)
    public String takeUser(@PathVariable("login") final String login){
        return userRepository.findByLogin(login).toString();
    }

    /**
     * USERS
     *
     * @return
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getUsers(){
        return userRepository.findAll();
    }
}
