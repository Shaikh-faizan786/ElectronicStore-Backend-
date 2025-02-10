package com.electronics.store.service.impl;

import com.electronics.store.dtos.RefreshTokenDto;
import com.electronics.store.dtos.UserDto;
import com.electronics.store.entities.RefreshToken;
import com.electronics.store.entities.User;
import com.electronics.store.exceptions.ResourcseNotFoundException;
import com.electronics.store.repositories.RefreshTokenRepository;
import com.electronics.store.repositories.UserReposiory;
import com.electronics.store.security.JwtHelper;
import com.electronics.store.service.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private UserReposiory userReposiory;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public RefreshTokenDto createRefreshToken(String username) {
        // user nikala
        User user = userReposiory.findByEmail(username).orElseThrow(() -> new ResourcseNotFoundException("User is not found"));
        //  ba is use ke liye refresh token bnayange

        // ab jab user fetch kiya tha to pehle check karenge refresh token hai ya nhi
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(null);
        // agar refreshtoke null raha tab jake new refresh token generate karenge
        if(refreshToken==null){
            refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusSeconds(5*24*60*60))
                    .build();
        }// agar refesh token pehle se hi hoga to sorf expiry chnage kar denge
        else{
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusSeconds(5*24*60*60));
        }

        // token ko save bhi kiya
        RefreshToken savedToken  = refreshTokenRepository.save(refreshToken);
        // humko ye refresh token return kiya to ab isko conver karna hoga refreshTokeDto me
        return mapper.map(savedToken, RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto findByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new ResourcseNotFoundException("Token is not found"));
        return mapper.map(refreshToken,RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto verifyRefreshToken(RefreshTokenDto token) {

        var refreshToken = mapper.map(token, RefreshToken.class);
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token is expired");
        }
        return token;
    }

    @Override
    public UserDto getUser(RefreshTokenDto dto) {
        // pehla isne token nikala
        RefreshToken refreshToken = refreshTokenRepository.findByToken(dto.getToken()).orElseThrow(() -> new ResourcseNotFoundException("Token is not found"));
        // fir token se user nikala
        User user = refreshToken.getUser();
        return mapper.map(user,UserDto.class);
    }
}
