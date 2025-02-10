package com.electronics.store.controllers;
import com.electronics.store.dtos.ApiResponseMessage;
import com.electronics.store.dtos.ImageResponse;
import com.electronics.store.dtos.PageableResponse;
import com.electronics.store.dtos.UserDto;
import com.electronics.store.entities.Providers;
import com.electronics.store.service.FileService;
import com.electronics.store.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    // isko apun application.configuration me set karenge
    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    // create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        userDto.setProvider(Providers.SELF);
        UserDto userDto1 = userService.createUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    // update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String userId, @Valid @RequestBody UserDto userDto) {
        UserDto updatedUserDto = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    // delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);

        ApiResponseMessage mess = new ApiResponseMessage();
        mess.setMessage("Used Deleted is Successfully");
        mess.setSuccess(true);
        mess.setStatus(HttpStatus.OK);

        return new ResponseEntity<>(mess, HttpStatus.OK);
    }

    // get All users
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<UserDto> userDtoList = userService.getAllUser(pageSize, pageNumber, sortBy, sortDir);
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    // get single user
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
        UserDto userById = userService.getUserById(userId);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }


    // get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        UserDto userByEmail = userService.getUserByEmail(email);
        return new ResponseEntity<>(userByEmail, HttpStatus.OK);
    }

    // search user
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords) {
        List<UserDto> searched = userService.searchUser(keywords);
        return new ResponseEntity<>(searched, HttpStatus.OK);
    }

    // upload use image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws IOException {
        String imageName = fileService.uploadFile(image, imageUploadPath);

        // jab upload hojayega to hum user ka id leke usko bhi update kardenge
        UserDto user = userService.getUserById(userId);
        user.setImageName(imageName);
        UserDto userDto = userService.updateUser(user, userId);

        // imageName isko database me update bhi karna hai user ke account me
        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .success(true)
                .message("Image Uploaded is Successful !!")
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    // serve the image
    @GetMapping("image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {

        UserDto userById = userService.getUserById(userId);
        logger.info("User Image name: {} ",userById.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, userById.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}