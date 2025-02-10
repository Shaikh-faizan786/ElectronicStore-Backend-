package com.electronics.store.service;

import com.electronics.store.dtos.RefreshTokenDto;
import com.electronics.store.dtos.UserDto;
import com.electronics.store.entities.RefreshToken;

public interface RefreshTokenService {

    // create
    RefreshTokenDto createRefreshToken(String username );

    // find by token
    RefreshTokenDto findByToken(String token);

    // verify
    RefreshTokenDto verifyRefreshToken(RefreshTokenDto token);

    UserDto getUser(RefreshTokenDto dto);
}
