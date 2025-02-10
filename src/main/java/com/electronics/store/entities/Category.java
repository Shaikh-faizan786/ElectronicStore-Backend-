package com.electronics.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="categories")
public class Category {

    @Id
    @Column(name = "id")
    private String categoryId;

    @Column(name = "category_title",length = 200,nullable = false)
    private String title;

    @Column(name = "category_desciption",length = 500)
    private String description;

    private String coverImage ;


    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> products= new ArrayList<>();


}
