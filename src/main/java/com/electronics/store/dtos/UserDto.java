package com.electronics.store.dtos;
import com.electronics.store.entities.Providers;
import com.electronics.store.validate.ImageNameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String userId;


    @Size(min=3,max=15,message = "Invalid name !!")
    private String name;


    @Email(message = "Invalid User Email !!")
    @Pattern(regexp = "^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$",message = "Invalid User Email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=8,max=15,message = " Enter Password between 8 to 15 character !!")
    private String password;

    @Size(min=4,max=6,message = "Invalid Gender !!")
    private String gender;

    @NotBlank(message = "Write Something About Yourself !!")
    private String about;

    private Providers provider;

     //@Pattern
    //@Custom Validator

    @ImageNameValid
    private String imageName;

    private List<RoleDto> roles;


}
