package be.g00glen00b.controller;

import be.g00glen00b.model.Album;
import be.g00glen00b.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/albums")
public class AlbumController extends AbstractApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumController.class);

    /**
     * curl -k https://localhost:8443/api/albums -H "Authorization: Bearer {token}"
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Set<Album> getUserAlbums() {
        User domainUser = getUser();
        LOGGER.debug("########## - USER IS GETTING HIS ALBUMS as: {}", domainUser.toString());
        return domainUser.getAlbums();
    }

    /**
     * Endpoint for creating new album.
     * <p/>
     * curl -k https://localhost:8443/api/albums/create -X POST -H "Authorization: Bearer {token}" -H "Content-Type: application/json" -d "{\"name\": \"New Album\"}"
     *
     * @param album
     * @return ResponseEntity<Album, HttpStatus>
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Album> createAlbum(@RequestBody @Valid Album album) {
        User domainUser = getUser();
        Album newAlbum = new Album(album.getTitle(), album.getDescription());
        LOGGER.debug("########## - USER {} IS CREATING NEW ALBUM : {}", domainUser.getLogin(), newAlbum.getTitle());
        newAlbum.setUser(domainUser);
        newAlbum = albumService.save(newAlbum);
        LOGGER.debug("ALBUM: {}", newAlbum.toString());
        return new ResponseEntity<Album>(newAlbum, HttpStatus.OK);
    }

    /**
     * Deleting User album.
     * <p/>
     * curl -k https://localhost:8443/api/albums/delete/{albumId} -X DELETE -H "Authorization: Bearer {token}"
     *
     * @param albumId
     * @return ResponseEntity<String, HttpStatus>
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAlbum(@PathVariable("id") Long albumId) {
        User domainUser = getUser();
        LOGGER.debug("########## - USER {} IS DELETING ALBUM WITH ID : {}", domainUser.getLogin(), albumId);
        Optional<Album> albumToDeleteOpt = domainUser.getAlbums().stream().filter(a -> a.getId().equals(albumId)).findFirst();
        if (!albumToDeleteOpt.isPresent()) {
            LOGGER.debug("########## - USER CAN'T DELETE ALBUM WITH ID : {}", albumId);
            return new ResponseEntity<String>("User is unauthorized to delete album with id: " + albumId.toString() + ".", HttpStatus.UNAUTHORIZED);
        }
        Album albumToDelete = albumToDeleteOpt.get();
        albumService.delete(albumToDelete);
        LOGGER.debug("########## - USER DELETED ALBUM WITH ID : {}", albumId);
        return new ResponseEntity<String>("User succesfully delete album with id: " + albumId, HttpStatus.OK);
    }

    /**
     * Endpoint - user can delete all his albums.
     * <p/>
     * <p/>
     * curl -k https://localhost:8443/api/albums/delete -X DELETE -H "Authorization: Bearer {token}"
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllAlbum() {
        try {
            User domainUser = getUser();
            Set<Album> albums = domainUser.getAlbums();
            albums.stream().forEach(a -> albumService.delete(a));
            LOGGER.debug("########## - USER {} DELETED ALL HIS ALBUMS", domainUser.getLogin());
            return new ResponseEntity<String>("User deleted all his albums", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>("User couldn't deleted all his albums", HttpStatus.OK);
    }

    /**
     * curl https://localhost:8443/api/albums/update -X PUT -H "Content-Type: application/json" -d "{\"id\": \"0\", \"name\": \"New Album Name\"}"
     *
     * @param album
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Album> updateAlbum(@RequestBody @Valid final Album album) {

        if (null == album.getId()) {
            album.setId(null);
            return new ResponseEntity<Album>(album, HttpStatus.BAD_REQUEST);
        }
        User domainUser = getUser();
        Optional<Album> userAlbumOpt = domainUser.getAlbums().stream()
                .filter(a -> a.getId().equals(album.getId()))
                .findFirst();
        if (!userAlbumOpt.isPresent()) {
            album.setId(null);
            return new ResponseEntity<Album>(album, HttpStatus.UNAUTHORIZED);
        }
        Album albumToUpdate = userAlbumOpt.get();
        albumToUpdate.setTitle(album.getTitle());
        albumToUpdate.setDescription(album.getDescription());
        albumService.save(albumToUpdate);
        return new ResponseEntity<Album>(albumToUpdate, HttpStatus.OK);
    }


    /**
     * Endpoint - Return photo entries of given album albumId.
     * <p/>
     * curl https://localhost:8443/api/albums/{albumId}/photos
     *
     * @param id
     * @return
     */

//    @RequestMapping(value = "/{albumId}/photos", method = RequestMethod.GET)
//    public ResponseEntity<List<Photo>> getPhotosByAlbumId(@PathVariable("albumId") Long id) {
//        User domainUser = userService.findByLogin(getUserLogin());
//        Optional<Album> albumOpt = domainUser.getUserAlbums().stream().filter(a -> a.getId().equals(id)).findFirst();
//        if (!albumOpt.isPresent()) {
//            return new ResponseEntity<List<Photo>>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
//        }
//        Album album = albumOpt.get();
//        return new ResponseEntity<List<Photo>>(album.getPhotoEntries(), HttpStatus.OK);
//    }


    /**
     * Endpoint - Listing all albums from database.
     * <p/>
     * curl https://localhost:8443/api/albums/all
     *
     * @return ResponseEntity<List<Album>, HttpStatus>
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Album>> getAll() {
        List<Album> albums = albumService.findAll();
        return new ResponseEntity<List<Album>>(albums, HttpStatus.OK);
    }

}
