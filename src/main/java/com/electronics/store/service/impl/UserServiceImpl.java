package com.electronics.store.service.impl;
import com.electronics.store.dtos.PageableResponse;
import com.electronics.store.dtos.UserDto;
import com.electronics.store.entities.Role;
import com.electronics.store.entities.User;
import com.electronics.store.exceptions.ResourcseNotFoundException;
import com.electronics.store.helper.Helper;
import com.electronics.store.repositories.RoleRepository;
import com.electronics.store.repositories.UserReposiory;
import com.electronics.store.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserReposiory userReposiory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;


    @Override
    public UserDto createUser(UserDto userDto) {

        // generate unique id in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);



        // dto -> entity
        User user=dtoToEntity(userDto);
        // set password encoder jo bhi user create karega uska passowrd usko miljayega then second time se login karne ke liye same passowrd use karna padega
         user.setPassword(passwordEncoder.encode(user.getPassword()));
        // fetch role of normal and set it to the user
        Role normal = Role.builder().roleId(UUID.randomUUID().toString()).name("ROLE_NORMAL").build();
        // agar user new create hoga to ye normal role declare hojayega
        Role role = roleRepository.findByName("ROLE_NORMAL").orElse(normal);
        user.setRoles(List.of(role));
        User savedUser = userReposiory.save(user);

        // entity -> dto
        UserDto newDto = entityToDto(savedUser);
        return newDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userReposiory.findById(userId).orElseThrow(() -> new ResourcseNotFoundException("User Not Found With The Given Id !!"));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role normalRole = roleRepository.findByName("ROLE_NORMAL")
                .orElseThrow(() -> new ResourcseNotFoundException("Role Not Found: ROLE_NORMAL"));
        List<Role> roles = new ArrayList<>();
        roles.add(normalRole); // Replace existing roles with normal role
        user.setRoles(roles);


        User updatedUser = userReposiory.save(user);
        // ye conversion kiya entity to Dto me
        UserDto updatedDto = entityToDto(updatedUser);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userReposiory.findById(userId).orElseThrow(() -> new ResourcseNotFoundException("User Not Found With The Given Id !!"));

        // delete user profile image
        //imageuploadpath = C:/Users/admin/Desktop/Master Spring Boot With Project/ElectronicStore/images/users/ abc.png
        String fullPath = imageUploadPath + user.getImageName();
        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException e) {
            logger.info("User Image not Found in folder");
            e.printStackTrace();
        }catch(IOException e ) {
            e.printStackTrace();
        }

        // delete user image name



        userReposiory.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userReposiory.findById(userId).orElseThrow(() -> new ResourcseNotFoundException("User Not Found With The Given Id !!"));
        return entityToDto(user);

    }

    @Override
    public UserDto getUserByEmail(String email) {
        User  user = userReposiory.findByEmail(email).orElseThrow(()-> new ResourcseNotFoundException("User Not Found With Given Email Id And Password "));
        return entityToDto(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageSize, int pageNumber, String sortBy, String sortDir) {

        // sort class ka inbuilt method hai Sort.by(sortBy)
        // Conditional Operator (? :):
        //Agar sortDir "desc" ho, to Sort.by(sortBy).descending() use karega.
        //Agar "desc" nahi ho, ta Sort.by(sortBy).ascending() karega.
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        // PageRequest implementation class hai Pageable ka
       Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        // yaha page dedega but list nhi dega to uske liye
        Page<User> page = userReposiory.findAll(pageable);
        // page.getContect() ko daalenge wo hume list dega
        /*
        // ye jo maine comment kiya hai code ye direct leke gaye Helper class me or waha users ke jaga <U> and dtoObject ki jagah <V> use kar  rahe hai taki us class ka object bana ke yaha use kar sake baar baar ye code likhne ki jaroorat na pade
        List<User> users = page.getContent();
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        PageableResponse response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPges(page.getTotalPages());
        response.setLastPages(page.isLast());
         */
        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);
        return response;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userReposiory.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }



    private UserDto entityToDto(User savedUser) {
//        UserDto userDto = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .gender(savedUser.getGender())
//                .about(savedUser.getAbout())
//                .imageName(savedUser.getImageName())
//                .build();
        return mapper.map(savedUser,UserDto.class);

    }

    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .gender(userDto.getGender())
//                .about(userDto.getAbout())
//                .imageName(userDto.getImageName())
//                .build();
        return mapper.map(userDto,User.class);
    }



}
