package com.clg.service;

import com.clg.SpringSecurityLatestApplication;
import com.clg.dto.Product;
import com.clg.entity.UserInfo;
import com.clg.repository.UserInfoRepository;
import com.clg.sequence.SequenceGeneratorService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    private JavaMailSender emailSender;

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
       if(userInfo.getRoles() == null || userInfo.getRoles().isEmpty()) {
           if (userInfo.getEmail().endsWith("@nwmissouri.com")) {
               userInfo.setRoles("ROLE_STUDENT");
           } else {
               userInfo.setRoles("ROLE_USER");
           }
       }
        repository.save(userInfo);
        return "user added to system ";
    }

    public String getuserandSendCode(UserInfo userInfo, boolean bypass) {
        Optional<UserInfo> userinfomail = repository.findByEmail(userInfo.getEmail());
        if (userinfomail.isPresent() && bypass) {
            return "user already exists with the email ";
        }
        if(!bypass && !userinfomail.isPresent()){
            return "user doesn't exists ";
        }
            Random random = new Random();
            String id = String.format("%04d", random.nextInt(10000));
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("pblgdp2@gmail.com");
            message.setTo(userInfo.getEmail());
            message.setSubject("Login Code");
            message.setText("Hi!!, Please use this code to login "+id);
            emailSender.send(message);
            SpringSecurityLatestApplication.codes.put(userInfo.getEmail(),id);
            return "sent email with id";

    }

    public List<UserInfo> getUsers() {
        return repository.findAll();
    }
}
