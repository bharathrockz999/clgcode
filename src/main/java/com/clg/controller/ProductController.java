package com.clg.controller;

import com.clg.SpringSecurityLatestApplication;
import com.clg.config.UserInfoUserDetailsService;
import com.clg.dto.AuthRequest;
import com.clg.dto.AuthResponse;
import com.clg.dto.Product;
import com.clg.entity.UserInfo;
import com.clg.repository.UserInfoRepository;
import com.clg.service.JwtService;
import com.clg.service.ProductService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class ProductController {

    @Autowired
    private ProductService service;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserInfoUserDetailsService userInfoUserDetailsService;

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/new")
    public ResponseEntity<String> addNewUser(@RequestBody UserInfo userInfo) {
        String code = SpringSecurityLatestApplication.codes.get(userInfo.getEmail());
        if(!userInfo.getCode().toString().equalsIgnoreCase(code)){
            return new ResponseEntity<String>("check security code", HttpStatus.BAD_REQUEST);
        }
        String msg = service.addUser(userInfo);
        if(msg.contains("exists")){
            return new ResponseEntity<String>("email already exists", HttpStatus.FOUND);
        }
        SpringSecurityLatestApplication.codes.remove(userInfo.getEmail());
        return new ResponseEntity<String>("user created successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deletee(@PathVariable String email) {
        repository.deleteByEmail(email);
        return new ResponseEntity<String>("deleted", HttpStatus.OK);
    }


//    @GetMapping("/all")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public List<Product> getAllTheProducts() {
//        return service.getProducts();
//    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Product getProductById(@PathVariable int id) {
        return service.getProduct(id);
    }


    @PostMapping("/authenticate")
    public AuthResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            Optional<UserInfo> userInfo = repository.findByEmail(authRequest.getUsername());
            AuthResponse response = new AuthResponse();
            response.setToken(jwtService.generateToken(authRequest.getUsername()));
            response.setUsername(userInfo.get().getEmail());
            response.setName(userInfo.get().getName());
            response.setAuthorities(userInfo.get().getRoles());
            return response;

        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }

    }
    @PostMapping("/sendCode")
    public ResponseEntity<String> sendCode(@RequestBody UserInfo userInfo) {
        String msg = service.getuserandSendCode(userInfo,true);
        if(msg.contains("exists")){
            return new ResponseEntity<String>("email already exists", HttpStatus.FOUND);
        }
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgot(@RequestBody UserInfo userInfo) {
        String msg = service.getuserandSendCode(userInfo,false);
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }
    @PostMapping("/changePassword/{code}/{email}/{newpassword}")
    public ResponseEntity<String> changePassword(@PathVariable("code") String code,@PathVariable("email") String email,@PathVariable("newpassword") String newpassword) {
        String sCode = SpringSecurityLatestApplication.codes.get(email);
        if(sCode!= null && !code.equalsIgnoreCase(sCode)){
            return new ResponseEntity<String>("code doesn't match", HttpStatus.OK);
        }
        String enc = passwordEncoder.encode(newpassword);
        UserInfo userInfo= repository.findByEmail(email).get();
        userInfo.setPassword(enc);
        repository.save(userInfo);
        SpringSecurityLatestApplication.codes.remove(email);
        return new ResponseEntity<String>("changed password", HttpStatus.OK);
    }
     @GetMapping("/all")
     @PreAuthorize("hasAuthority('ROLE_ADMIN')")
     public ResponseEntity<List<UserInfo>> getAllUsers() {
       return new ResponseEntity<List<UserInfo>>(service.getUsers(), HttpStatus.OK);
    }

}
