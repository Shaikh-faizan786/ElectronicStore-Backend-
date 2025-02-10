package com.electronics.store.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Role {

    @Id
    private String roleId;
    private String name;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();
}
