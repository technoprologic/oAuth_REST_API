package be.g00glen00b.controller;

import be.g00glen00b.dto.AlbumDto;
import be.g00glen00b.model.Album;
import be.g00glen00b.model.User;
import be.g00glen00b.service.AlbumService;
import be.g00glen00b.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumController.class);

    @Autowired
    protected UserService userService;
    @Autowired
    protected AlbumService albumService;

    /** Endpoint* - Listing all user albums.
     *
     * curl -k https://localhost:8443/api/albums -H "Authorization: Bearer {token}"
     *
     * @param request
     * @return ResponseEntity<?>, HttpStatus)
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getUserAlbums(HttpServletRequest request) {
        User domainUser = userService.findByLogin(request.getRemoteUser());
        LOGGER.debug("########## - USER IS GETTING HIS ALBUMS as: {}", domainUser.toString());
        return new ResponseEntity<>(domainUser.getAlbums(), HttpStatus.OK);
    }

    /**
     * Endpoint for creating new album.
     *
     * curl -k https://localhost:8443/api/albums -X POST -H "Authorization: Bearer {token}" -H "Content-Type: application/json" -d "{\"title\": \"New Album\", \"description\": \"Album description\"}"
     *
     * @param album
     * @return ResponseEntity<Album, HttpStatus>
     */
    @RequestMapping( method = RequestMethod.POST)
    public ResponseEntity<?> createAlbum(@RequestBody @Valid AlbumDto album, HttpServletRequest request) {
        User domainUser = userService.findByLogin(request.getRemoteUser());
        Album newAlbum = new Album(album.getTitle(), album.getDescription());
        LOGGER.debug("########## - USER {} IS CREATING NEW ALBUM : {}", domainUser.getLogin(), newAlbum.getTitle());
        newAlbum.setUser(domainUser);
        newAlbum = albumService.save(newAlbum);
        LOGGER.debug("ALBUM: {}", newAlbum.toString());
        return new ResponseEntity<>(newAlbum, HttpStatus.OK);
    }

    /**
     * Endpoint* - Deleting User album.
     *
     * curl -k https://localhost:8443/api/albums/{albumId}/delete -X DELETE -H "Authorization: Bearer {token}"
     *
     * @param albumId
     * @param request
     * @return ResponseEntity<String, HttpStatus>
     */
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAlbum(@PathVariable("id") Long albumId, HttpServletRequest request) {
        User domainUser = userService.findByLogin(request.getRemoteUser());
        LOGGER.debug("########## - USER {} IS DELETING ALBUM WITH ID : {}", domainUser.getLogin(), albumId);
        Optional<Album> albumToDeleteOpt = domainUser.getAlbums().stream().filter(a -> a.getId().equals(albumId)).findFirst();
        if (!albumToDeleteOpt.isPresent()) {
            LOGGER.debug("########## - USER CAN'T DELETE ALBUM WITH ID : {}", albumId);
            return new ResponseEntity<>("User is unauthorized to delete album with id: " + albumId.toString() + ".", HttpStatus.UNAUTHORIZED);
        }
        Album albumToDelete = albumToDeleteOpt.get();
        albumService.delete(albumToDelete);
        LOGGER.debug("########## - USER DELETED ALBUM WITH ID : {}", albumId);
        return new ResponseEntity<>("User succesfully delete album with id: " + albumId, HttpStatus.OK);
    }

    /** Endpoint* - updates album.
     *
     * curl -k https://localhost:8443/api/albums/{albumId} -X PUT -H "Content-Type: application/json" -d "{\"title\": \"New Album Name\", \"description\": \"New description\"}" -H "Authorization: Bearer {token}"
     *
     * @param albumId
     * @param album
     * @param request
     * @return ResponseEntity<?>(?, HttpStatus)
     */
    @RequestMapping(value = "/{albumId}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<?> updateAlbum(@PathVariable("albumId") final Long albumId, @RequestBody @Valid final AlbumDto album, HttpServletRequest request) {
        if((null == album.getDescription() || album.getDescription().isEmpty()) && (null == album.getTitle() || album.getTitle().isEmpty())){
            return new ResponseEntity<>("Could not update album with given values", HttpStatus.BAD_REQUEST);
        }
        User domainUser = userService.findByLogin(request.getRemoteUser());
        Optional<Album> userAlbumOpt = domainUser.getAlbums().stream()
                .filter(a -> a.getId().equals(albumId))
                .findFirst();
        if (!userAlbumOpt.isPresent()) {
            return new ResponseEntity<>("User can't update album with given id", HttpStatus.UNAUTHORIZED);
        }
        Album albumToUpdate = userAlbumOpt.get();
        if(null != album.getDescription() && !album.getDescription().isEmpty()) {
            albumToUpdate.setTitle(album.getTitle());
        }
        if(null != album.getDescription() && !album.getDescription().isEmpty()) {
            albumToUpdate.setDescription(album.getDescription());
        }
        albumToUpdate = albumService.save(albumToUpdate);
        return new ResponseEntity<>(albumToUpdate, HttpStatus.OK);
    }

    /**
     * Endpoint - Return photo entries of given album albumId.
     *
     * curl -k https://localhost:8443/api/albums/{albumId}/photos -H "Authorization: Bearer {token}"
     *
     * @param id
     * @param request
     * @return
     */

    @RequestMapping(value = "/{albumId}/photos", method = RequestMethod.GET)
    public ResponseEntity<?> getPhotosByAlbumId(@PathVariable("albumId") Long id, HttpServletRequest request) {
        User domainUser = userService.findByLogin(request.getRemoteUser());
        Optional<Album> albumOpt = domainUser.getAlbums().stream().filter(a -> a.getId().equals(id)).findFirst();
        if (!albumOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Album album = albumOpt.get();
        return new ResponseEntity<>(album.getPhotos(), HttpStatus.OK);
    }

}
