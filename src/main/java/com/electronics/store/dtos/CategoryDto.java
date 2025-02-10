package com.electronics.store.dtos;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private String categoryId;

    @NotBlank(message = "Title Required !!")
    @Size(min = 4,message = "Title must be of minimum 4 character ")
    private String title;

    @NotBlank(message = "Description Required !!")
    @Size(min = 10,message = "Description must be of minimum 10 character ")
    private String description;

    private String coverImage ;
}
