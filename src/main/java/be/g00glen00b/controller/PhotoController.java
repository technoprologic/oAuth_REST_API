package be.g00glen00b.controller;

import be.g00glen00b.model.Album;
import be.g00glen00b.model.Photo;
import be.g00glen00b.model.Tag;
import be.g00glen00b.model.User;
import be.g00glen00b.service.AlbumService;
import be.g00glen00b.service.PhotoService;
import be.g00glen00b.service.TagService;
import be.g00glen00b.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by emagdnim on 2016-03-23.
 */

@RestController
@RequestMapping("/api/photo")
public class PhotoController  {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumController.class);
    private final AlbumService albumService;
    private final UserService userService;
    private final PhotoService photoService;
    private final TagService tagService;
    private final List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "bmp", "gif");

    @Autowired
    public PhotoController(final AlbumService albumService,
                           final UserService userService,
                           final PhotoService photoService,
                           final TagService tagService
    ) {
        this.albumService = albumService;
        this.userService = userService;
        this.photoService = photoService;
        this.tagService = tagService;
    }

    /** Endpoint* - allows upload image file with extensions("jpg", "jpeg", "bmp", "gif").
     *
     * curl -k -v -F description=Description -F created=19-03-2016 -F "file=@D:\plik.jpg" -X POST https://localhost:8443/api/photo/upload/{albumId} -H "Authorization: Bearer {token}"
     *
     * @param albumId
     * @param description
     * @param creationDate
     * @param file
     * @param request
     * @return ResponseEntity
     */
    @RequestMapping(value = "/upload/{albumId}", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFile(@PathVariable("albumId") Long albumId,
                                             @RequestParam("description") String description,
                                             @RequestParam("created") @DateTimeFormat(pattern = "dd-MM-yyyy") Date creationDate,
                                             @RequestParam("file") MultipartFile file ,
                                             HttpServletRequest request) {
        String fileExt = file.getOriginalFilename().split("\\.")[1];
        LOGGER.debug("File extension is: {}", fileExt);
        if (!allowedExtensions.contains(fileExt.toLowerCase())) {
            return new ResponseEntity<>("Bad file extension", HttpStatus.BAD_REQUEST);
        }
        User user = userService.findByLogin(request.getRemoteUser());
        Optional<Album> albumOpt = user.getAlbums().stream().filter(a -> a.getId().equals(albumId)).findFirst();
        if (!albumOpt.isPresent()) {
            return new ResponseEntity<>("File not uploaded successfully! You specified wrong albumId",
                    HttpStatus.BAD_REQUEST);
        }
        Photo newPhoto = null;
        try {
            newPhoto = new Photo(file.getBytes(), description, creationDate);
        } catch (IOException e) {
            e.printStackTrace();
            new ResponseEntity<>("Could not read bytes from given file", HttpStatus.BAD_REQUEST);
        }
        Album album = albumOpt.get();
        if (null == album.getPhotos()) {
            album.setPhotos(new HashSet<>());
        }
        newPhoto.setAlbum(album);
        photoService.save(newPhoto);
        return new ResponseEntity<>("File uploaded successfully!", HttpStatus.OK);
    }




    /** Endpoint* - allows to delete photo by given photoId
     *
     * curl -k https://localhost:8443/api/photo/delete/{photoId} -X DELETE -H "Authorization: Bearer {token}"
     *
     * @param photoId
     * @param request
     * @return ResponseEntity<String message, HttpStatus>
     */
    @RequestMapping(value = "/delete/{photoId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("photoId") Long photoId, HttpServletRequest request) {
        User user = userService.findByLogin(request.getRemoteUser());
        Optional<Photo> photoOpt = user.getAlbums().stream()
                .flatMap(a -> a.getPhotos().stream())
                .filter(p -> p.getId().equals(photoId))
                .findFirst();
        if (!photoOpt.isPresent()) {
            return new ResponseEntity<>("Desired photo could not be deleted!", HttpStatus.UNAUTHORIZED);
        }
        Photo photo = photoOpt.get();
        photoService.delete(photo);
        return new ResponseEntity<>("Desired photo succesfully deleted.", HttpStatus.OK);
    }



    /** Endpoint* - returns all photo entries by given albumId.
     *
     * curl -k https://localhost:8443/api/photo/all/{albumId} -H "Authorization: Bearer {token}"
     *
     * @param albumId
     * @param request
     * @return ResponseEntity<?, HttpStatus>
     */
    @RequestMapping(value = "/all/{albumId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAlbumPhotos(@PathVariable("albumId") Long albumId, HttpServletRequest request) {
        User user = userService.findByLogin(request.getRemoteUser());
        Optional<Album> albumOpt = user.getAlbums().stream()
                .filter(a -> a.getId().equals(albumId))
                .findFirst();
        if (!albumOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Album album = albumOpt.get();
        return new ResponseEntity<>(album.getPhotos(), HttpStatus.OK);
    }

    /** Endpoint* - allows to add unique tag to photo entry.
     * If tag doesn't exists in repository then adds one.
     *
     * curl -k -d "tag=newTag" https://localhost:8443/api/photo/{photoId}/tag -X POST -H "Authorization: Bearer {token}"
     *
     * @param photoId
     * @param tag
     * @param request
     * @return
     */
    @RequestMapping(value = "/{photoId}/tag", method = RequestMethod.POST)
    public ResponseEntity<Photo> tagPhoto(@PathVariable("photoId") final Long photoId,
                                          @RequestParam("tag") final String tag,
                                          HttpServletRequest request) {
        User user = userService.findByLogin(request.getRemoteUser());
        Optional<Photo> photoOpt = user.getAlbums().stream()
                .flatMap(a -> a.getPhotos().stream())
                .filter(p -> p.getId().equals(photoId))
                .findFirst();
        LOGGER.debug("FILTERED PHOTO");
        if (!photoOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Tag> tagOpt = tagService.findAll().stream()
                .filter(t -> t.getTagValue().equals(tag))
                .findFirst();
        LOGGER.debug("FILTERED TAG");
        Tag photoTag = null;
        if (tagOpt.isPresent()) {
            photoTag = tagOpt.get();
        } else {
            photoTag = new Tag(tag);
        }
        tagService.save(photoTag);

        Photo photo = photoOpt.get();
        LOGGER.debug("PHOTO: {}", photo.toString());
        photo.getTags().add(photoTag);
        LOGGER.debug("PHOTO: {}", photo.toString());
        photo = photoService.save(photo);
        return new ResponseEntity<Photo>(photo, HttpStatus.OK);
    }

    /** Endpoint* - Removes tag from photo.
     *
     * curl -k -d "tagId={tagId}" https://localhost:8443/api/photo/{photoId}/untag -X POST -H "Authorization: Bearer {token}"
     *
     * @param photoId
     * @param tagId
     * @param request
     * @return ResponseEntity<Photo>
     */
    @RequestMapping(value = "/{photoId}/untag", method = RequestMethod.POST)
    public ResponseEntity<?> untagPhoto(@PathVariable("photoId") final Long photoId,
                                            @RequestParam("tagId") final Long tagId,
                                            HttpServletRequest request) {
        User user = userService.findByLogin(request.getRemoteUser());
        Optional<Tag> tagOpt = user.getAlbums().stream()
                .flatMap(a -> a.getPhotos().stream())
                .flatMap(p -> p.getTags().stream())
                .filter(t -> t.getId().equals(tagId))
                .findFirst();
        Optional<Photo> photoOpt = user.getAlbums().stream()
                .flatMap(a -> a.getPhotos().stream())
                .filter(p -> p.getId().equals(photoId))
                .findFirst();
        if(!tagOpt.isPresent() || !photoOpt.isPresent()){
            return new ResponseEntity<>("Could not remove tag from given photo", HttpStatus.CONFLICT);
        }
        Photo photo = photoOpt.get();
        Tag tag = tagOpt.get();
        tagService.untag(photo, tag);

        return new ResponseEntity<>(photo, HttpStatus.OK);
    }


    /** Endpoint* - Allows to get all tags by photo id.
     *
     * curl -k https://localhost:8443/api/photo/{photoId}/tags -H "Authorization: Bearer {token}"
     *
     * @param photoId
     * @return ResponseEntity<List<Tag>, HttpStatus>
     */
    @RequestMapping(value = "/{photoId}/tags", method = RequestMethod.GET)
    public ResponseEntity<?> photoTags(@PathVariable("photoId") final Long photoId, HttpServletRequest request){
        User user = userService.findByLogin(request.getRemoteUser());
        Optional<Photo> optional = user.getAlbums().stream()
                .flatMap(a -> a.getPhotos().stream())
                .filter(p -> p.getId().equals(photoId))
                .findFirst();
        if(!optional.isPresent()){
            LOGGER.debug("User choose wrong photoId");
            return new ResponseEntity<>(new HashSet<>(), HttpStatus.UNAUTHORIZED);
        }
        Photo photo = optional.get();
        LOGGER.debug("User photo tags: " + photo.getTags());
        return new ResponseEntity<>(photo.getTags(), HttpStatus.OK);
    }

    /** Endpoint* - allows to get all users photos
     *
     * curl -k https://localhost:8443/api/photo/all -H "Authorization: Bearer {token}"
     *
     * @return ResponseEntity<List<Photo>, HttpStatus>
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(photoService.findAll(), HttpStatus.OK);
    }

}
