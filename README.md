# oAuth_REST_API

REST service that provides a RESTful endpoints protected by OAuth 2. Allows users to manage their photo albums, photos and photo  tags.

## Used Spring Projects 

* http://projects.spring.io/spring-boot/[Spring Boot]
* http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html[Spring MVC]
* http://projects.spring.io/spring-security/[Spring Security]
* http://projects.spring.io/spring-security-oauth/[Spring Security OAuth]
* http://projects.spring.io/spring-data-jpa/[Spring Data JPA]


## Installation

git clone https://github.com/technoprologic/oAuth_REST_API.git

cd oAuth_REST_API/

mvn clean package spring-boot:run

# Usage

## Getting acces_token

curl -X POST -k -vu clientapp:123456 https://localhost:8443/oauth/token -H "Accept: application/json" -d "password=sp
ring&username=admin&grant_type=password&scope=read%20write&client_secret=123456&client_id=clientapp"

## Albums management
### Creating new album ( {token} - should be replaced with valid token )
curl -k https://localhost:8443/api/albums -X POST -H "Authorization: Bearer {token}" -H "Content-Type: application/json" -d "{\"title\": \"New Album\", \"description\": \"Album description\"}"

### Deleting album (variable to change: {albumId})
curl -k https://localhost:8443/api/albums/{albumId}/delete -X DELETE -H "Authorization: Bearer {token}"

### Updating album (variables to change: {albumId}, {token})
curl -k https://localhost:8443/api/albums/{albumId} -X PUT -H "Content-Type: application/json" -d "{\"title\": \"New Album Name\", \"description\": \"New description\"}" -H "Authorization: Bearer {token}"

### Return photo entries of given album albumId.
curl -k https://localhost:8443/api/albums/{albumId}/photos -H "Authorization: Bearer {token}"

### Listing all user albums (variable: {token})
curl -k https://localhost:8443/api/albums -H "Authorization: Bearer {token}"

## Photo management
### Uploading image file with allowed extensions("jpg", "jpeg", "bmp", "gif") - EXAMPLE: file.jpg
curl -k -v -F description=Description -F created=19-03-2016 -F "file=@D:\file.jpg" -X POST https://localhost:8443/api/photo/upload/{albumId} -H "Authorization: Bearer {token}"

### Delete photo by given photoId
curl -k https://localhost:8443/api/photo/delete/{photoId} -X DELETE -H "Authorization: Bearer {token}"

### Returning all photo entries by given albumId
curl -k https://localhost:8443/api/photo/all/{albumId} -H "Authorization: Bearer {token}"

### Adding unique tag to photo entry. If tag doesn't exists in repository then adds one.
curl -k -d "tag=newTag" https://localhost:8443/api/photo/{photoId}/tag -X POST -H "Authorization: Bearer {token}"

### Removing tag from photo
curl -k -d "tagId={tagId}" https://localhost:8443/api/photo/{photoId}/untag -X POST -H "Authorization: Bearer {token}"

### Getting all tags by photo id
curl -k https://localhost:8443/api/photo/{photoId}/tags -H "Authorization: Bearer {token}"

### Getting all users photos
curl -k https://localhost:8443/api/photo/all -H "Authorization: Bearer {token}"

## Tag management

### Creating new tag
curl -k -d "tag=tagValue" https://localhost:8443/api/tag -X POST -H "Authorization: Bearer {token}"

### Deleting tag
curl -k -d "tagId={tagId}" https://localhost:8443/api/tag -X DELETE -H "Authorization: Bearer {token}"

### Listing used tags by user
curl -k https://localhost:8443/api/tag/advise -H "Authorization: Bearer {token}"

### Return all tags from database with given string preffix
curl -k https://localhost:8443/api/tag/prefix/{prefix} -H "Authorization: Bearer {token}"

## Fast diagnostic endpoints - made just for quick hand testing

### Returning all albums as String
curl -k https://localhost:8443/status/albums

### Displays specific user info
curl -k https:localhost:8443/status/user/{login}

### Displays all specific user info
curl -k https://localhost:8443/status/users

### Displays all tags
curl -k https://localhost:8443/status/tags





