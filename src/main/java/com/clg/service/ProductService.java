package com.clg.service;

import com.clg.dto.Product;
import com.clg.entity.UserInfo;
import com.clg.repository.UserInfoRepository;
import com.clg.sequence.SequenceGeneratorService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductService {

    List<Product> productList = null;

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @PostConstruct
    public void loadProductsFromDB() {
        productList = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> Product.builder()
                        .productId(i)
                        .name("product " + i)
                        .qty(new Random().nextInt(10))
                        .price(new Random().nextInt(5000)).build()
                ).collect(Collectors.toList());
    }


    public List<Product> getProducts() {
        return productList;
    }

    public Product getProduct(int id) {
        return productList.stream()
                .filter(product -> product.getProductId() == id)
                .findAny()
                .orElseThrow(() -> new RuntimeException("product " + id + " not found"));
    }


    public String addUser(UserInfo userInfo) {
        Optional<UserInfo> userinfomail =repository.findByEmail(userInfo.getEmail());
        if(userinfomail.isPresent()){
            return "user already exists with the email ";
        }
        userInfo.setId(sequenceGeneratorService.generateSequence(UserInfo.SEQUENCE_NAME));
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        if(userInfo.getEmail().endsWith("@nwmissouri.com")){
            userInfo.setRoles("ROLE_STUDENT");
        } else {
            userInfo.setRoles("ROLE_USER");
        }
        repository.save(userInfo);
        return "user added to system ";
    }

}
