package com.electronics.store.service;

import com.electronics.store.dtos.PageableResponse;
import com.electronics.store.dtos.UserDto;
import com.electronics.store.entities.User;


import java.util.List;
import java.util.Optional;

public interface UserService {

    // create
    UserDto createUser(UserDto userDto);

    // update
    UserDto updateUser(UserDto userDto, String userId);

    // delete
    void deleteUser(String userId);

    // get single user by id
    UserDto getUserById(String userId);

    // get single user by email
    UserDto getUserByEmail(String email);

    // get all user
    PageableResponse<UserDto> getAllUser(int pageSize, int pageNumber, String sortBy, String sortDir);

    // search user
    List<UserDto> searchUser(String keyword);


}
