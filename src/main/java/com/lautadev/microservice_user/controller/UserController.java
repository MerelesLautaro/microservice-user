package com.lautadev.microservice_user.controller;

import com.lautadev.microservice_user.dto.BenefitDTO;
import com.lautadev.microservice_user.dto.UserDTO;
import com.lautadev.microservice_user.model.User;
import com.lautadev.microservice_user.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private IUserService userServ;

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody User user){
        userServ.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok(userServ.getUsers());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> findUser(@PathVariable Long id){
        User user = userServ.findUser(id);

        if(user == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(user);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<UserDTO> findUserAndBenefit(@PathVariable Long id){
        return ResponseEntity.ok(userServ.findUserAndBenefit(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userServ.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit")
    public ResponseEntity<User> editUser(@Valid @RequestBody User user){
        userServ.editUser(user);
        return ResponseEntity.ok(userServ.findUser(user.getIdUser()));
    }
}
