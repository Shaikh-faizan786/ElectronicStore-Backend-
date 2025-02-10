package com.electronics.store.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtResponse {
    private String token;
    private UserDto user;
    private RefreshTokenDto refreshToken;
}
