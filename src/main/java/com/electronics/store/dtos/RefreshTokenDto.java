package com.electronics.store.dtos;

import com.electronics.store.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenDto {

        private int id ;
        private String token;
        private Instant expiryDate;

}
