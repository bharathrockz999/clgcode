package com.clg.controller;

import com.clg.config.UserInfoUserDetailsService;
import com.clg.dto.AuthRequest;
import com.clg.dto.AuthResponse;
import com.clg.dto.Product;
import com.clg.entity.UserInfo;
import com.clg.repository.UserInfoRepository;
import com.clg.service.JwtService;
import com.clg.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/new")
    public ResponseEntity<String> addNewUser(@RequestBody UserInfo userInfo) {
        String msg = service.addUser(userInfo);
        if(msg.contains("exists")){
            return new ResponseEntity<String>("email already exists", HttpStatus.FOUND);
        }
        return new ResponseEntity<String>("user created successfully", HttpStatus.OK);
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Product> getAllTheProducts() {
        return service.getProducts();
    }

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
}
