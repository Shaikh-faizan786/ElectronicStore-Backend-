package com.electronics.store.controllers;
import com.electronics.store.dtos.*;
import com.electronics.store.entities.Providers;
import com.electronics.store.exceptions.ResourcseNotFoundException;
import com.electronics.store.service.RefreshTokenService;
import com.electronics.store.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.electronics.store.entities.User;
import com.electronics.store.exceptions.BadApiRequestException;
import com.electronics.store.security.JwtHelper;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private RefreshTokenService refreshTokenService;


    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${app.google.client_id}")
    private String googleClientId;

    @Value("${app.google.default_password}")
    private String googleProviderDefaultPassword;

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/regenerate-token")
    public ResponseEntity<JwtResponse> regenerateToken(@RequestBody RefreshTokenRequest request){
        // refresh token se agar new token generate karna hai to
        // pehle token nikalo then uske baad
        RefreshTokenDto refreshTokenDto = refreshTokenService.findByToken(request.getRefreshToken());
        // then uske baad verify karo token ko
        RefreshTokenDto refreshTokenDto1 = refreshTokenService.verifyRefreshToken(refreshTokenDto);
        // kis user ka ye token hai wo bhi batana jaroori hai
        UserDto user = refreshTokenService.getUser(refreshTokenDto1);
       // jab sab milgaya to refresh token se jwt token generat ekernge

        String jwtToken = jwtHelper.generateToken(modelMapper.map(user,User.class) );

        // ab jab jwt token ban gaya to jwt response me bhej denge
        JwtResponse response = JwtResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshTokenDto)
                .user(user)
                .build();
       return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login(@RequestBody  JwtRequest request){

        logger.info("Username {} , Password {} ",request.getEmail(),request.getPassword());
        this.doAuthenticate(request.getEmail(),request.getPassword());
        // userdetails service se pehla email lelunga or wo humko UserDeials dediya to ab type cast kiya User class me
        User user = (User)userDetailsService.loadUserByUsername(request.getEmail());
        // ab jwthelper ki madad se token generate karenge
        String token = jwtHelper.generateToken(user);
        // ab is token ko response e bhjena hai

        // ab yaha refreshtoken generate karenge
        RefreshTokenDto refreshToken = refreshTokenService.createRefreshToken(user.getEmail());


        JwtResponse response = JwtResponse.builder()
                .token(token)
                .user(modelMapper.map(user, UserDto.class))
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity.ok(response );
    }

    private void doAuthenticate(String email, String password) {

       Authentication authentication = new UsernamePasswordAuthenticationToken(email,password);
        try{
            manager.authenticate(authentication);
        }catch (BadCredentialsException e){
            throw  new BadApiRequestException(" Invalid Username or Password  !!");
        }
    }


    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        String name = principal.getName();
        return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(name), UserDto.class), HttpStatus.OK);

    }

    //handle  login with google

    //    {idToken}

    @PostMapping("/login-with-google")
    public ResponseEntity<JwtResponse> handleGoogleLogin(@RequestBody GoogleLoginRequest loginRequest) throws GeneralSecurityException, IOException {
        logger.info("Id  Token : {}", loginRequest.getIdToken());


        // token verify
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new ApacheHttpTransport(), new GsonFactory()).setAudience(List.of(googleClientId)).build();


        GoogleIdToken googleIdToken = verifier.verify(loginRequest.getIdToken());

        if (googleIdToken != null) {
            //token verified

            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            String email = payload.getEmail();
            String userName = payload.getSubject();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            logger.info("Name {}", name);
            logger.info("Email {}", email);
            logger.info("Picture {}", pictureUrl);
            logger.info("Username {}", userName);


            UserDto userDto = new UserDto();
            userDto.setName(name);
            userDto.setEmail(email);
            userDto.setImageName(pictureUrl);
            userDto.setPassword(googleProviderDefaultPassword);
            userDto.setAbout("user is created using google ");
            userDto.setProvider(Providers.GOOGLE);
            //

            UserDto user = null;
            try {

                logger.info("user is loaded from database");
                user = userService.getUserByEmail(userDto.getEmail());

                // logic implement
                //provider
                logger.info(user.getProvider().toString());
                if (user.getProvider().equals(userDto.getProvider())) {
                    //continue
                } else {
                    throw new BadCredentialsException("Your email is already registered !! Try to login with username and password ");
                }


            } catch (ResourcseNotFoundException ex) {
                logger.info("This time user created: because this is new user ");
                user = userService.createUser(userDto);
            }


            //
            this.doAuthenticate(user.getEmail(), userDto.getPassword());


            User user1 = modelMapper.map(user, User.class);


            String token = jwtHelper.generateToken(user1);
            //send karna hai response

            JwtResponse jwtResponse = JwtResponse.builder().token(token).user(user).build();

            return ResponseEntity.ok(jwtResponse);


        } else {
            logger.info("Token is invalid !!");
            throw new BadApiRequestException("Invalid Google User  !!");
        }


   }


}
