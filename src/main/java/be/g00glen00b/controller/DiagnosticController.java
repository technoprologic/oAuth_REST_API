package be.g00glen00b.controller;

import be.g00glen00b.service.AlbumService;
import be.g00glen00b.service.TagService;
import be.g00glen00b.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by emagdnim on 2016-03-21.
 */
@RestController
@RequestMapping("/status")
public class DiagnosticController {

    private UserService userService;
    private AlbumService albumService;
    private TagService tagService;

    @Autowired
    public DiagnosticController(UserService userService, AlbumService albumService, TagService tagService){
        this.userService = userService;
        this.albumService = albumService;
        this.tagService = tagService;

    }

    /** Endpoint* - returns all albums.
     *
     * curl -k https://localhost:8443/status/albums
     *
     * @return String
     */
    @RequestMapping(value = "/albums", method = RequestMethod.GET)
    public String getAlbums(){
        return albumService.findAll().toString();
    }

    /** Endpoint - displays specific user info.
     *
     * curl -k https:localhost:8443/status/user/{login}
     *
     * @param login
     * @return String
     */
    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET)
    public String takeUser(@PathVariable("login") final String login){
        return userService.findByLogin(login).toString();
    }

    /** Endpoint - displays all specific user info.
     *
     * curl -k https://localhost:8443/status/users -H "Authorization Bearer {token}"
     *
     * @return ResponseEntity<?>(object, HttpStatus)
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<?> getUsers(){
        return new ResponseEntity<>(userService.findAll().toString(), HttpStatus.OK);
    }

    /** Endpoint - allow to display all tags in database (just for test control).
     *
     * curl -k https://localhost:8443/status/tags
     *
     * @return ResponseEntity<?>(object, HttpStatus)
     */
    @RequestMapping(method =  RequestMethod.GET)
    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(tagService.findAll(), HttpStatus.OK);
    }
}
