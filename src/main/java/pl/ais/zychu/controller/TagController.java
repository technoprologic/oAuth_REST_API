package pl.ais.zychu.controller;

import pl.ais.zychu.model.Tag;
import pl.ais.zychu.model.User;
import pl.ais.zychu.service.AlbumService;
import pl.ais.zychu.service.TagService;
import pl.ais.zychu.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by emagdnim on 2016-03-23.
 */

@RestController
@RequestMapping(value = "/api/tag")
public class TagController  {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumController.class);
    private final AlbumService albumService;
    private final UserService userService;
    private final TagService tagService;

    @Autowired
    public TagController(final AlbumService albumService, final UserService userService, final TagService tagService) {
        this.albumService = albumService;
        this.userService = userService;
        this.tagService = tagService;
    }

    /** Endpoint* -  Adding new tag
     * curl -k -d "tag=tagValue" https://localhost:8443/api/tag -X POST -H "Authorization: Bearer {token}"
     *
     * @param tag
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> newTag(@RequestParam("tag") final String tag){
        Optional<Tag> existingTagOpt = tagService.findAll().stream()
                .filter(t -> t.getTagValue().equals(tag))
                .findFirst();
        if(existingTagOpt.isPresent()){
            return new ResponseEntity<>(existingTagOpt.get(), HttpStatus.ALREADY_REPORTED);
        }
        Tag newTag = new Tag(tag);
        newTag = tagService.save(newTag);
        return new ResponseEntity<>(newTag, HttpStatus.OK);
    }

    /** Endpoint* - Deleting tag.
     *
     * curl -k -d "tagId={tagId}" https://localhost:8443/api/tag -X DELETE -H "Authorization: Bearer {token}"
     *
     * @param tagId
     * @return ResponseEntity<?>(object, HttpStatus)
     */
    @RequestMapping( method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam("tagId") final Long tagId){
        Tag tag = tagService.getOne(tagId);
        if(null == tag){
            return new ResponseEntity<>("Tag doesn't exists", HttpStatus.BAD_REQUEST);
        }
        tagService.delete(tag);
        return new ResponseEntity<>("Tag removed succesfully", HttpStatus.OK);
    }

    /** Endpoint where user can take set(without repetition) of used tags.
     *
     * curl -k https://localhost:8443/api/tag/advise -H "Authorization: Bearer {token}"
     *
     * @param request
     * @return ResponseEntity<?>(object, HttpStatus)
     */
    @RequestMapping(value = "/advise", method = RequestMethod.GET)
    public ResponseEntity<?> usedTags(HttpServletRequest request){
        User user = userService.findByLogin(request.getRemoteUser());
        Set<Tag> usedTags =  user.getAlbums().stream()
                .flatMap(a -> a.getPhotos().stream())
                .flatMap(p -> p.getTags().stream())
                .collect(Collectors.toSet());
        return new ResponseEntity<>(usedTags, HttpStatus.OK);
    }

    /** Endpoint - Return all tags from database with given string preffix.
     *
     *  curl -k https://localhost:8443/api/tag/prefix/{prefix} -H "Authorization: Bearer {token}"
     *
     * @param preffix
     * @return ResponseEntity<object, HttpStatus>
     */
    @RequestMapping(value = "/prefix/{prefix}", method = RequestMethod.GET)
    public ResponseEntity<?> getByPrefix(@PathVariable("prefix") final String preffix){
        List<Tag> tags = tagService.findAll().stream()
                .filter(t -> t.getTagValue().startsWith(preffix))
                .collect(Collectors.toList());
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
}
