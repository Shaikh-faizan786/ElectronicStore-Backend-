package com.electronics.store.repositories;

import com.electronics.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserReposiory extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);
    Optional <User> findByEmailAndPassword(String emial,String password);
   List<User> findByNameContaining(String keywords);

}
